package com.chess.application.client.ui;

import com.chess.application.client.components.*;
import com.chess.common.ENUM.RESIGN_TYPE;
import com.chess.common.Member;
import com.chess.common.Resign;
import com.chess.common.Transfer;
import com.chess.framework.client.NFrameworkClient;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.net.InetAddress;

public class ChessUI extends Application
{
    public String username;
    public int user_ID;
    public Stage primaryStage;
    public Scene scene;
    public BorderPane root;
    public InternetError internetError;
    public SignInPage signInPage;
    public SignUpPage signUpPage;
    public ChessMenuBar chessMenuBar;
    public ChessTopBar chessTopBar;
    public HomePage homePage;
    public SocialPage socialPage;
    public ChessPage chessPage;
    public ProfilePage profilePage;
    public NFrameworkClient client;
    public Member member=null;

    public void start(Stage stage) throws Exception
    {
        primaryStage=stage;

        client=new NFrameworkClient();

        makeAllPages();

        root=new BorderPane();
        root.setStyle("-fx-background-color: #302e2b;");

        try
        {
            InetAddress address = InetAddress.getByName("www.google.com");
            if (address.isReachable(5000))
            {
                if (!signInPage.preferences.get("username","").isEmpty() && !signInPage.preferences.get("password","").isEmpty())
                {
                    username=signInPage.preferences.get("username","");

                    getProfileDetails();

                    chessTopBar=new ChessTopBar(this);
                    chessMenuBar=new ChessMenuBar(this);

                    homePage=new HomePage(this);
                    chessPage=new ChessPage(this);
                    socialPage=new SocialPage(this);
                    profilePage=new ProfilePage(this);

                    root.setTop(chessTopBar);
                    root.setLeft(chessMenuBar);
                    root.setCenter(homePage);
                }
                else
                {
                    root.setCenter(signInPage);
                }

//                root.setCenter(signInPage);
            }
            else
            {
                root.setCenter(internetError);
            }
        }
        catch (Throwable throwable)
        {
            root.setCenter(internetError);
        }
//        chessTopBar=new ChessTopBar(this);
//        root.setTop(chessTopBar);
//        chessMenuBar=new ChessMenuBar(this);
//        root.setLeft(chessMenuBar);
//        chessPage=new ChessPage(this);
//        root.setCenter(chessPage);
//        root.setCenter(signUpPage);

        scene = new Scene(root, 800, 800);

        Image logoImage = new Image(SignInPage.class.getResource("/com/chess/application/client/components/Default/AppLogo/chess logo.png").toExternalForm());

        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Chess");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            public void handle(WindowEvent event)
            {
                if (chessTopBar!=null)
                {
                    if (chessTopBar.isPlaying)
                    {
                        try
                        {
                            Resign resign=new Resign();
                            resign.setFrom(user_ID);
                            resign.setTo(chessTopBar.opponent_user_ID);
                            resign.setResignType(RESIGN_TYPE.RESIGN_AS_BUTTON);

                            Transfer transfer=new Transfer();
                            transfer.resign=resign;

                            client.execute("/Chess_Server/Send_Resign",transfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }

                        Member player=chessTopBar.getMemberDetailsUsingID(user_ID);
                        Member opponent=chessTopBar.getMemberDetailsUsingID(chessTopBar.opponent_user_ID);

                        int playerRating = player.getElo();
                        int opponentRating = opponent.getElo();

                        int k=EloCalculator.calculateDynamicK(player.getGamePlayed());

                        double actualScore = 0;

                        // Calculate expected scores
                        double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);

                        // Calculate new ratings
                        int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);

                        try
                        {
                            Transfer transfer=new Transfer();
                            transfer.user_ID=user_ID;
                            transfer.elo=newPlayerRating;

                            client.execute("/Chess_Server/Update_Player_Elo",transfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }

                        chessTopBar.isPlaying=false;
                        chessTopBar.opponent_user_ID=0;

                        chessTopBar.opponent_elo=0;
                        chessTopBar.player_elo=0;

                        chessPage.whiteChessBoard.timerForMove.stop();
                        chessPage.whiteChessBoard.timerForResign.stop();

                        chessPage.blackChessBoard.timerForMove.stop();
                        chessPage.blackChessBoard.timerForResign.stop();

                        chessPage.chessPane.getChildren().clear();
                        chessPage.chessPane.getChildren().add(chessPage.inviteAndPlay);

                        chessPage.whiteChessBoard=new WhiteChessBoard(ChessUI.this);
                        AnchorPane.setTopAnchor(chessPage.whiteChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessPage.whiteChessBoard,60.0);

                        chessPage.blackChessBoard=new BlackChessBoard(ChessUI.this);
                        AnchorPane.setTopAnchor(chessPage.blackChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessPage.blackChessBoard,60.0);

                        chessMenuBar.home.setDisable(false);
                        chessMenuBar.social.setDisable(false);
                        chessMenuBar.profile.setDisable(false);
                        chessMenuBar.home.fire();

                        try
                        {
                            Transfer transfer=new Transfer();
                            transfer.user_ID=user_ID;

                            client.execute("/Chess_Server/Update_Game_Played",transfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                    }
                    else
                    {
                        if (chessTopBar.opponent_user_ID!=0)
                        {
                            try
                            {
                                Transfer transfer=new Transfer();
                                transfer.user_ID=chessTopBar.opponent_user_ID;
                                transfer.opponent_ID=user_ID;
                                client.execute("/Chess_Server/Reject_Invitation",transfer);

                            }catch (Throwable t)
                            {
                                System.out.println("Member List : "+t);
                            }
                            chessPage.chessPane.getChildren().clear();
                            chessPage.chessPane.getChildren().add(chessPage.inviteAndPlay);
                            chessTopBar.opponent_user_ID=0;

                            chessMenuBar.home.setDisable(false);
                            chessMenuBar.social.setDisable(false);
                            chessMenuBar.profile.setDisable(false);
                            chessMenuBar.home.fire();
                        }
                        try
                        {
                            Transfer transfer=new Transfer();
                            transfer.user_ID=chessTopBar.opponent_user_ID;
                            transfer.opponent_ID=user_ID;
                            client.execute("/Chess_Server/Reject_Invitation",transfer);

                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                    }
                }
            }
        });
    }

    public void makeAllPages()
    {
        internetError=new InternetError(this);
        signInPage=new SignInPage(this);
        signUpPage=new SignUpPage(this);

    }

    public void showUI()
    {
        launch();
    }

    public void getProfileDetails()
    {
        try
        {
            Transfer transfer =new Transfer();
            transfer.username=username;
            Object object = (Object)client.execute("/Chess_Server/Get_Member_Details",transfer);
            if (object != null)
            {
                Gson gson1 = new Gson();
                String json = gson1.toJson(object);
                member = gson1.fromJson(json, Member.class);
                user_ID= member.getUserID();
            }
        } catch (Throwable e)
        {
            System.out.println("Failed to load member details : "+e.getMessage());
        }
    }

}
