package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import com.chess.common.ENUM.GAME_TYPE;
import com.chess.common.ENUM.RESIGN_TYPE;
import com.chess.common.Member;
import com.chess.common.Resign;
import com.chess.common.Transfer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

public class ChessPage extends AnchorPane
{
    private Image homeImage,blurWhiteChessBoard,blurBlackChessBoard,robot,whiteChessImage,blackChessImage;
    public ImageView homeImageView,blurWhiteChessBoardView,blurBlackChessBoardView,robotImageView,whiteChessImageView,blackChessImageView;
    public ChoiceBox<String> choiceBox;
    public Button resign,resignForStockfish,cancelInvitation,whiteChessButton,blackChessButton;
    public BoxBlur blurForResign,blurForHomeImage;
    public ButtonType yesResign,noResign,okResign;
    public Stage alertStage,alertStageForOpponent;
    public Alert resignAlert,resignAlertForOpponent;
    public AnchorPane chessPane,chessPiecesCapturePaneForPlayer,chessPiecesCapturePaneForOpponent,cancelPane;
    public Label inviteAndPlay,waitingForOpponent,opponentName,opponentElo,timerForOppnent,playerName,playerElo,timerForPlayer;
    public Image defaultProfileForPlayer,defaultProfileForOpponent;
    public ImageView defaultProfileForPlayerImageView,defaultProfileForOpponentImageView;
    public WhiteChessBoard whiteChessBoard;
    public BlackChessBoard blackChessBoard;
    public WhiteChessBoardStockFish whiteChessBoardStockFish;
    public BlackChessBoardStockFish blackChessBoardStockFish;
    public ChessUI chessUI;
    public HBox playerBox,opponentBox;
    public int elapsedSecondsForPlayer = 0;
    public int elapsedSecondsForOpponent = 0;
    public Timeline timelineForPlayer,timelineForOpponent;
    public Timer tempTimer;


    public ChessPage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        makeAllChessComponents();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b");
        setPrefWidth(1920);
        setPrefHeight(1080);
    }

    private void addListeners()
    {
        whiteChessButton.setOnAction(events ->
        {
            int level=0;
            if (choiceBox.getValue().contains("10%"))
            {
                level=3;
            }else if (choiceBox.getValue().contains("20%"))
            {
                level=6;
            }else if (choiceBox.getValue().contains("30%"))
            {
                level=9;
            }else if (choiceBox.getValue().contains("40%"))
            {
                level=12;
            }else if (choiceBox.getValue().contains("50%"))
            {
                level=15;
            }else if (choiceBox.getValue().contains("60%"))
            {
                level=18;
            }else if (choiceBox.getValue().contains("70%"))
            {
                level=21;
            }else if (choiceBox.getValue().contains("80%"))
            {
                level=24;
            }else if (choiceBox.getValue().contains("90%"))
            {
                level=27;
            }else if (choiceBox.getValue().contains("100%"))
            {
                level=30;
            }


            if (chessUI.member.getProfile()!=null)
            {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(chessUI.member.getProfile());
                // Create an Image object from the InputStream
                defaultProfileForPlayer = new Image(inputStream);
            }
            else
            {
                defaultProfileForPlayer = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
            }

            defaultProfileForPlayerImageView = new ImageView(defaultProfileForPlayer);
            defaultProfileForPlayerImageView.setFitHeight(45);
            defaultProfileForPlayerImageView.setFitWidth(45);
            defaultProfileForPlayerImageView.setLayoutX(60);
            defaultProfileForPlayerImageView.setLayoutY(602);
            defaultProfileForPlayerImageView.setSmooth(true);

            defaultProfileForOpponent = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/ChessPage/Robot/robot.png").toExternalForm());
            defaultProfileForOpponentImageView = new ImageView(defaultProfileForOpponent);
            defaultProfileForOpponentImageView.setFitHeight(45);
            defaultProfileForOpponentImageView.setFitWidth(45);
            defaultProfileForOpponentImageView.setLayoutX(60);
            defaultProfileForOpponentImageView.setLayoutY(2);
            defaultProfileForOpponentImageView.setSmooth(true);

            playerName.setText(chessUI.member.getUsername());
            opponentName.setText("Stockfish");
            playerElo.setText("("+chessUI.member.getElo()+")");

            whiteChessBoardStockFish=new WhiteChessBoardStockFish(chessUI,level);
            AnchorPane.setTopAnchor(whiteChessBoardStockFish,50.0);
            AnchorPane.setLeftAnchor(whiteChessBoardStockFish,60.0);
            chessPane.getChildren().clear();
            chessPane.getChildren().addAll(whiteChessBoardStockFish,defaultProfileForPlayerImageView,defaultProfileForOpponentImageView,playerBox,opponentBox,resignForStockfish);
        });

        resignForStockfish.setOnAction( actionEvent ->
        {
            Alert alertForStockfish = new Alert(Alert.AlertType.CONFIRMATION);
            alertForStockfish.setHeaderText("");
            alertForStockfish.setTitle("Resign");
            alertForStockfish.setContentText("Are You Sure ? ");
            alertForStockfish.getDialogPane().setStyle("-fx-background-color: lightgray;");
            alertForStockfish.getButtonTypes().setAll(yesResign=new ButtonType("Yes"),noResign=new ButtonType("No"));
            alertForStockfish.setOnShowing(e ->
            {
                Stage alertStage = (Stage) alertForStockfish.getDialogPane().getScene().getWindow();
                alertStage.setX(564);
                alertStage.setY(384);
            });

            java.util.Optional<ButtonType> result = alertForStockfish.showAndWait();

            if (result.get() == yesResign)
            {

                chessPane.getChildren().clear();
                chessPane.getChildren().addAll(inviteAndPlay,robotImageView,choiceBox,whiteChessButton,blackChessButton);
                chessUI.chessMenuBar.home.fire();
            }


        });

        blackChessButton.setOnAction(events ->
        {
//            Alert alertForStockfish = new Alert(Alert.AlertType.INFORMATION);
//            alertForStockfish.setHeaderText("");
//            alertForStockfish.setTitle("Info");
//            alertForStockfish.setContentText("This Feature ( Play As Black ) Is Not Implemented Yet \n\n ( You Can Play As White )");
//            alertForStockfish.getDialogPane().setStyle("-fx-background-color: lightgray;");
////            alertForStockfish.getButtonTypes().setAll(yesResign=new ButtonType("Yes"),noResign=new ButtonType("No"));
//            alertForStockfish.setOnShowing(e ->
//            {
//                Stage alertStage = (Stage) alertForStockfish.getDialogPane().getScene().getWindow();
//                alertStage.setX(564);
//                alertStage.setY(384);
//            });
//            alertForStockfish.showAndWait();
            int level=0;
            if (choiceBox.getValue().contains("10%"))
            {
                level=3;
            }else if (choiceBox.getValue().contains("20%"))
            {
                level=6;
            }else if (choiceBox.getValue().contains("30%"))
            {
                level=9;
            }else if (choiceBox.getValue().contains("40%"))
            {
                level=12;
            }else if (choiceBox.getValue().contains("50%"))
            {
                level=15;
            }else if (choiceBox.getValue().contains("60%"))
            {
                level=18;
            }else if (choiceBox.getValue().contains("70%"))
            {
                level=21;
            }else if (choiceBox.getValue().contains("80%"))
            {
                level=24;
            }else if (choiceBox.getValue().contains("90%"))
            {
                level=27;
            }else if (choiceBox.getValue().contains("100%"))
            {
                level=30;
            }


            if (chessUI.member.getProfile()!=null)
            {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(chessUI.member.getProfile());
                // Create an Image object from the InputStream
                defaultProfileForPlayer = new Image(inputStream);
            }
            else
            {
                defaultProfileForPlayer = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
            }

            defaultProfileForPlayerImageView = new ImageView(defaultProfileForPlayer);
            defaultProfileForPlayerImageView.setFitHeight(45);
            defaultProfileForPlayerImageView.setFitWidth(45);
            defaultProfileForPlayerImageView.setLayoutX(60);
            defaultProfileForPlayerImageView.setLayoutY(602);
            defaultProfileForPlayerImageView.setSmooth(true);

            defaultProfileForOpponent = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/ChessPage/Robot/robot.png").toExternalForm());
            defaultProfileForOpponentImageView = new ImageView(defaultProfileForOpponent);
            defaultProfileForOpponentImageView.setFitHeight(45);
            defaultProfileForOpponentImageView.setFitWidth(45);
            defaultProfileForOpponentImageView.setLayoutX(60);
            defaultProfileForOpponentImageView.setLayoutY(2);
            defaultProfileForOpponentImageView.setSmooth(true);

            playerName.setText(chessUI.member.getUsername());
            opponentName.setText("Stockfish");
            playerElo.setText("("+chessUI.member.getElo()+")");

            blackChessBoardStockFish=new BlackChessBoardStockFish(chessUI,level);
            AnchorPane.setTopAnchor(blackChessBoardStockFish,50.0);
            AnchorPane.setLeftAnchor(blackChessBoardStockFish,60.0);
            chessPane.getChildren().clear();
            chessPane.getChildren().addAll(blackChessBoardStockFish,defaultProfileForPlayerImageView,defaultProfileForOpponentImageView,playerBox,opponentBox,resignForStockfish);
        });

        resign.setOnAction(actionEvent ->
        {
            java.util.Optional<ButtonType> result = resignAlert.showAndWait();

            if (result.get() == yesResign)
            {
                if (chessUI.chessTopBar.isPlaying)
                {
                    try
                    {
                        Resign resign=new Resign();
                        resign.setFrom(chessUI.user_ID);
                        resign.setTo(chessUI.chessTopBar.opponent_user_ID);
                        resign.setResignType(RESIGN_TYPE.RESIGN_AS_BUTTON);

                        Transfer transfer=new Transfer();
                        transfer.resign=resign;

                        chessUI.client.execute("/Chess_Server/Send_Resign",transfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }
                }

                Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);

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
                    transfer.user_ID=chessUI.user_ID;
                    transfer.elo=newPlayerRating;

                    chessUI.client.execute("/Chess_Server/Update_Player_Elo",transfer);
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }

                chessUI.chessTopBar.isPlaying=false;
                chessUI.chessTopBar.opponent_user_ID=0;

                chessUI.chessTopBar.opponent_elo=0;
                chessUI.chessTopBar.player_elo=0;

                chessUI.chessPage.whiteChessBoard.timerForMove.stop();
                chessUI.chessPage.whiteChessBoard.timerForResign.stop();

                chessUI.chessPage.blackChessBoard.timerForMove.stop();
                chessUI.chessPage.blackChessBoard.timerForResign.stop();

                chessUI.chessPage.chessPane.getChildren().clear();
                chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);

                chessUI.chessPage.whiteChessBoard=new WhiteChessBoard(chessUI);
                AnchorPane.setTopAnchor(chessUI.chessPage.whiteChessBoard,50.0);
                AnchorPane.setLeftAnchor(chessUI.chessPage.whiteChessBoard,60.0);

                chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);

                chessUI.chessMenuBar.home.setDisable(false);
                chessUI.chessMenuBar.social.setDisable(false);
                chessUI.chessMenuBar.profile.setDisable(false);
                chessUI.chessMenuBar.home.fire();

                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }

            }

        });

        cancelInvitation.setOnAction( actionEvent ->
        {
            try
            {
                Transfer transfer=new Transfer();
                transfer.user_ID=chessUI.chessTopBar.opponent_user_ID;
                transfer.opponent_ID=chessUI.user_ID;
                chessUI.client.execute("/Chess_Server/Reject_Invitation",transfer);

            }catch (Throwable t)
            {
                System.out.println("Member List : "+t);
            }
            chessUI.chessTopBar.opponent_user_ID=0;

            chessPane.getChildren().clear();
            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
            chessUI.chessMenuBar.home.setDisable(false);
            chessUI.chessMenuBar.social.setDisable(false);
            chessUI.chessMenuBar.profile.setDisable(false);

            chessUI.chessMenuBar.home.fire();
        });
    }
    public void makeAllChessComponents()
    {
        chessPane=new AnchorPane();
        chessPane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);"); //#302e2b
        AnchorPane.setTopAnchor(chessPane, 40.0);
        AnchorPane.setBottomAnchor(chessPane, 60.0);
        AnchorPane.setLeftAnchor(chessPane, 350.0);
        AnchorPane.setRightAnchor(chessPane, 840.0);


        playerName=new Label("player name");
        Font PlayerNameFont = Font.font("Sage UI",20);
        playerName.setFont(PlayerNameFont);
        playerName.setTextFill(Color.WHITE);
//        playerName.setLayoutX(110);
//        playerName.setLayoutY(608.0);

        playerElo=new Label("(500)");
        Font playerEloFont = Font.font("Sage UI",20);
        playerElo.setFont(playerEloFont);
        playerElo.setTextFill(Color.WHITE);

        playerBox=new HBox(10);
        playerBox.getChildren().addAll(playerName,playerElo);
        playerBox.setLayoutX(110);
        playerBox.setLayoutY(608.0);

        timerForPlayer=new Label("00:00");
        Font timerForPlayerFont = Font.font("Sage UI",20);
        timerForPlayer.setFont(timerForPlayerFont);
        timerForPlayer.setTextFill(Color.WHITE);
        timerForPlayer.setLayoutX(607.0);
        timerForPlayer.setLayoutY(610.0);


        timelineForPlayer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                elapsedSecondsForPlayer--;
                int minutes = elapsedSecondsForPlayer / 60;
                int seconds = elapsedSecondsForPlayer % 60;
                String timeText = String.format("%02d:%02d", minutes, seconds);
                timerForPlayer.setText(timeText);

                if (elapsedSecondsForPlayer==0)
                {
                    try
                    {
                        Resign resign=new Resign();
                        resign.setFrom(chessUI.user_ID);
                        resign.setTo(chessUI.chessTopBar.opponent_user_ID);
                        resign.setResignType(RESIGN_TYPE.RESIGN_AS_TIMEOUT);

                        Transfer transfer=new Transfer();
                        transfer.resign=resign;

                        chessUI.client.execute("/Chess_Server/Send_Resign",transfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }
                    timelineForPlayer.stop();
                }
            }
        }));

        defaultProfileForPlayer = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        defaultProfileForPlayerImageView = new ImageView(defaultProfileForPlayer);
        defaultProfileForPlayerImageView.setFitHeight(45);
        defaultProfileForPlayerImageView.setFitWidth(45);
        defaultProfileForPlayerImageView.setLayoutX(60);
        defaultProfileForPlayerImageView.setLayoutY(602);
        defaultProfileForPlayerImageView.setSmooth(true);

        opponentName=new Label("opponent name");
        Font opponentNameFont = Font.font("Sage UI",20);
        opponentName.setFont(opponentNameFont);
        opponentName.setTextFill(Color.WHITE);

        opponentElo=new Label("(3000)");
        Font opponentEloFont = Font.font("Sage UI",20);
        opponentElo.setFont(opponentEloFont);
        opponentElo.setTextFill(Color.WHITE);

        opponentBox=new HBox(10);
        opponentBox.getChildren().addAll(opponentName,opponentElo);
        opponentBox.setLayoutX(110);
        opponentBox.setLayoutY(8);

        timerForOppnent=new Label("00:00");
        Font timerForOppnentFont = Font.font("Sage UI",20);
        timerForOppnent.setFont(timerForOppnentFont);
        timerForOppnent.setTextFill(Color.WHITE);
        timerForOppnent.setLayoutX(607.0);
        timerForOppnent.setLayoutY(10);


        timelineForOpponent = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                elapsedSecondsForOpponent--;
                int minutes = elapsedSecondsForOpponent / 60;
                int seconds = elapsedSecondsForOpponent % 60;
                String timeText = String.format("%02d:%02d", minutes, seconds);
                timerForOppnent.setText(timeText);
                if (elapsedSecondsForOpponent==0)
                {
                    timelineForOpponent.stop();
                }
            }
        }));

        tempTimer=new Timer(1000, new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent ae)
            {
                Platform.runLater(() ->
                {
                    if (elapsedSecondsForPlayer==0)
                    {
                        timelineForPlayer.stop();
                        timelineForOpponent.stop();
                        tempTimer.stop();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("");
                        alert.setTitle("Lose");
                        alert.setContentText("Time Out You Loos :(");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        ButtonType okWin=new ButtonType("Ok");
                        alert.getButtonTypes().setAll(okWin);
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(564);
                        alertStage.setY(384);
                        java.util.Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == okWin)
                        {
                            chessUI.chessPage.whiteChessBoard=new WhiteChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.whiteChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.whiteChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                        else
                        {
                            chessUI.chessPage.whiteChessBoard=new WhiteChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.whiteChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.whiteChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                    }
                });

                if (elapsedSecondsForOpponent==0)
                {
                    timelineForPlayer.stop();
                    timelineForOpponent.stop();
                    tempTimer.stop();
                }
            }
        });


        defaultProfileForOpponent = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        defaultProfileForOpponentImageView = new ImageView(defaultProfileForOpponent);
        defaultProfileForOpponentImageView.setFitHeight(45);
        defaultProfileForOpponentImageView.setFitWidth(45);
        defaultProfileForOpponentImageView.setLayoutX(60);
        defaultProfileForOpponentImageView.setLayoutY(2);
        defaultProfileForOpponentImageView.setSmooth(true);

        whiteChessBoard=new WhiteChessBoard(chessUI);
        AnchorPane.setTopAnchor(whiteChessBoard,50.0);
        AnchorPane.setLeftAnchor(whiteChessBoard,60.0);

        blackChessBoard=new BlackChessBoard(chessUI);
        AnchorPane.setTopAnchor(blackChessBoard,50.0);
        AnchorPane.setLeftAnchor(blackChessBoard,60.0);

        chessPiecesCapturePaneForPlayer=new AnchorPane();
        chessPiecesCapturePaneForPlayer.setStyle("-fx-background-color: white;"); //#302e2b
        chessPiecesCapturePaneForPlayer.setPrefHeight(24);
        chessPiecesCapturePaneForPlayer.setPrefWidth(250);
        chessPiecesCapturePaneForPlayer.setLayoutX(110);
        chessPiecesCapturePaneForPlayer.setLayoutY(623);

        chessPiecesCapturePaneForOpponent=new AnchorPane();
        chessPiecesCapturePaneForOpponent.setStyle("-fx-background-color: white;"); //#302e2b
        chessPiecesCapturePaneForOpponent.setPrefHeight(24);
        chessPiecesCapturePaneForOpponent.setPrefWidth(250);
        chessPiecesCapturePaneForOpponent.setLayoutX(110);
        chessPiecesCapturePaneForOpponent.setLayoutY(23);

        blurForResign = new BoxBlur();
        blurForResign.setWidth(10);
        blurForResign.setHeight(10);

        resignAlert = new Alert(Alert.AlertType.CONFIRMATION);
        resignAlert.setHeaderText("");
        resignAlert.setTitle("Resign");
        resignAlert.setContentText("Are You Sure ? ");
        resignAlert.getDialogPane().setStyle("-fx-background-color: lightgray;");
        resignAlert.getButtonTypes().setAll(yesResign=new ButtonType("Yes"),noResign=new ButtonType("No"));
        alertStage = (Stage) resignAlert.getDialogPane().getScene().getWindow();
        alertStage.setOnShowing(e ->
        {
            Stage alertStage = (Stage) resignAlert.getDialogPane().getScene().getWindow();
            alertStage.setX(564);
            alertStage.setY(384);
        });

//        resign=new Button("Resign");
//        Font resignFont = Font.font("Sage UI",17.45);
//        resign.setFont(resignFont);
//        resign.setLayoutX(592.5);
//        resign.setLayoutY(605);

        resign=new Button("Resign");
        Font resignFont = Font.font("Sage UI",13);
        resign.setFont(resignFont);
        resign.setTextFill(Color.BLACK);
        resign.setStyle("-fx-background-color: #9a9a9a;");
        resign.setPrefHeight(68.5);
        resign.setLayoutX(671.0);
        resign.setLayoutY(529.0);

        resignForStockfish=new Button("Resign");
        Font resignForStockfishFont = Font.font("Sage UI",13);
        resignForStockfish.setFont(resignForStockfishFont);
        resignForStockfish.setTextFill(Color.BLACK);
        resignForStockfish.setStyle("-fx-background-color: #9a9a9a;");
        resignForStockfish.setPrefHeight(68.5);
        resignForStockfish.setLayoutX(671.0);
        resignForStockfish.setLayoutY(529.0);

        resignAlertForOpponent = new Alert(Alert.AlertType.INFORMATION);
        resignAlertForOpponent.setHeaderText("");
        resignAlertForOpponent.setTitle("Win");
        resignAlertForOpponent.setContentText("You Win By Resiging");
        resignAlertForOpponent.getDialogPane().setStyle("-fx-background-color: lightgray;");
        resignAlertForOpponent.getButtonTypes().setAll(okResign=new ButtonType("Ok"));
        alertStageForOpponent = (Stage) resignAlertForOpponent.getDialogPane().getScene().getWindow();
        alertStageForOpponent.setOnShowing(e ->
        {
            Stage alertStage = (Stage) resignAlertForOpponent.getDialogPane().getScene().getWindow();
            alertStage.setX(564);
            alertStage.setY(384);
        });

        BoxBlur blurChessBoard = new BoxBlur();
        blurChessBoard.setWidth(10);
        blurChessBoard.setHeight(10);

        blurWhiteChessBoard = new javafx.scene.image.Image(ChessPage.class.getResource("/com/chess/application/client/components/ChessPage/BlurChessBoard/white chess board.png").toExternalForm());
        blurWhiteChessBoardView=new ImageView(blurWhiteChessBoard);
        blurWhiteChessBoardView.setFitHeight(548);
        blurWhiteChessBoardView.setFitWidth(609);
        blurWhiteChessBoardView.setOpacity(0.8);
        blurWhiteChessBoardView.setEffect(blurChessBoard);
        AnchorPane.setTopAnchor(blurWhiteChessBoardView,50.0);
        AnchorPane.setLeftAnchor(blurWhiteChessBoardView,60.0);

        blurBlackChessBoard = new javafx.scene.image.Image(ChessPage.class.getResource("/com/chess/application/client/components/ChessPage/BlurChessBoard/black chess board.png").toExternalForm());
        blurBlackChessBoardView=new ImageView(blurBlackChessBoard);
        blurBlackChessBoardView.setFitHeight(548);
        blurBlackChessBoardView.setFitWidth(609);
        blurBlackChessBoardView.setOpacity(0.8);
        blurBlackChessBoardView.setEffect(blurChessBoard);
        AnchorPane.setTopAnchor(blurBlackChessBoardView,50.0);
        AnchorPane.setLeftAnchor(blurBlackChessBoardView,60.0);

        inviteAndPlay=new Label("Invite And Play Or Play With Computer");
        Font inviteAndPlayFont = Font.font("Sage UI",18);
        inviteAndPlay.setFont(inviteAndPlayFont);
        inviteAndPlay.setTextFill(Color.web("#9a9a9a"));
        inviteAndPlay.setLayoutX(210);
        inviteAndPlay.setLayoutY(600);


        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.BLACK);
        glowEffect.setRadius(5);
        glowEffect.setSpread(0.1);

        waitingForOpponent=new Label("Waiting For Opponent");
        Font waitingForOpponentFont = Font.font("Sage UI",40);
        waitingForOpponent.setFont(waitingForOpponentFont);
        waitingForOpponent.setEffect(glowEffect);
        waitingForOpponent.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(waitingForOpponent,50.0);
        AnchorPane.setLeftAnchor(waitingForOpponent,25.0);

        cancelInvitation=new Button("Cancel");
        Font cancelInvitationFont = Font.font("Sage UI",20);
        cancelInvitation.setFont(cancelInvitationFont);
        AnchorPane.setTopAnchor(cancelInvitation,150.0);
        AnchorPane.setLeftAnchor(cancelInvitation,160.0);
        AnchorPane.setRightAnchor(cancelInvitation,160.0);
        AnchorPane.setBottomAnchor(cancelInvitation,50.0);

        cancelPane=new AnchorPane();
        cancelPane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);"); //#302e2b
        cancelPane.setLayoutX(140);
        cancelPane.setLayoutY(225);
        cancelPane.setPrefWidth(450);
        cancelPane.setPrefHeight(200);
        cancelPane.getChildren().addAll(waitingForOpponent,cancelInvitation);

        whiteChessBoardStockFish=new WhiteChessBoardStockFish(chessUI,0);
        AnchorPane.setTopAnchor(whiteChessBoardStockFish,50.0);
        AnchorPane.setLeftAnchor(whiteChessBoardStockFish,60.0);

        robot = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/ChessPage/Robot/robot.png").toExternalForm());
        robotImageView=new ImageView(robot);
        robotImageView.setFitHeight(100);
        robotImageView.setFitWidth(100);
        robotImageView.setLayoutX(315);
        robotImageView.setLayoutY(200);

        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(
                "Level 10%",
                "Level 20%",
                "Level 30%",
                "Level 40%",
                "Level 50%",
                "Level 60%",
                "Level 70%",
                "Level 80%",
                "Level 90%",
                "Level 100%"
        );
        choiceBox.setValue("Level 10%");
        choiceBox.setStyle("-fx-background-color: #9a9a9a;");
        choiceBox.setLayoutX(290);
        choiceBox.setLayoutY(310);
        choiceBox.setPrefHeight(50);
        choiceBox.setPrefWidth(150);

        whiteChessImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/White Pawn.png").toExternalForm());
        whiteChessImageView = new ImageView(whiteChessImage);
        whiteChessImageView.setFitHeight(50);
        whiteChessImageView.setFitWidth(50);
        whiteChessImageView.setSmooth(true);

        whiteChessButton=new Button();
        whiteChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        whiteChessButton.setGraphic(whiteChessImageView);
        whiteChessButton.setLayoutX(290);
        whiteChessButton.setLayoutY(380);
        whiteChessButton.setFocusTraversable(false);


        blackChessImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/Black Pawn.png").toExternalForm());
        blackChessImageView = new ImageView(blackChessImage);
        blackChessImageView.setFitHeight(50);
        blackChessImageView.setFitWidth(50);
        blackChessImageView.setSmooth(true);

        blackChessButton=new Button();
        blackChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        blackChessButton.setGraphic(blackChessImageView);
        blackChessButton.setLayoutX(373);
        blackChessButton.setLayoutY(380);
        blackChessButton.setFocusTraversable(false);




        chessPane.getChildren().addAll(inviteAndPlay,robotImageView,choiceBox,whiteChessButton,blackChessButton);
//        chessPane.getChildren().add(playerBox);
//        chessPane.getChildren().add(timerForPlayer);
//        chessPane.getChildren().add(defaultProfileForPlayerImageView);
//        chessPane.getChildren().add(chessPiecesCapturePaneForPlayer);
//        chessPane.getChildren().add(opponentBox);
//        chessPane.getChildren().add(timerForOppnent);
//        chessPane.getChildren().add(defaultProfileForOpponentImageView);
//        chessPane.getChildren().add(chessPiecesCapturePaneForOpponent);
//        chessPane.getChildren().add(whiteChessBoard);
//        chessPane.getChildren().add(blackChessBoard);
//        chessPane.getChildren().add(whiteChessBoardStockFish);

//        chessPane.getChildren().add(resign);
//        chessPane.getChildren().add(blurWhiteChessBoardView);
//        chessPane.getChildren().add(blurWhiteChessBoardView);

        blurForHomeImage = new BoxBlur();
        blurForHomeImage.setWidth(5);
        blurForHomeImage.setHeight(5);
        homeImage = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/HomePage/main page 1.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(750);
        homeImageView.setFitWidth(1470);
        homeImageView.setOpacity(0.6);
        homeImageView.setEffect(blurForHomeImage);

        getChildren().add(homeImageView);
        getChildren().add(chessPane);
    }
}
