package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import com.chess.common.*;
import com.chess.common.ENUM.GAME_TYPE;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SocialPage extends AnchorPane
{
    private AnchorPane friendsPane,noFriendsPane,noFriendsPaneForSearch;
    private Image homeImage;
    private ImageView homeImageView;
    private Label friends,noFriendsPaneLabel,noFriendsPaneLabelForSearch;
    private TextField searchFriends;
    private Button resetSearchFriends;
    private ScrollPane friendScrollPane;
    private VBox friendsPaneTable;
    private VBox tempFriendsPaneTable;
    private Timer getFriendsTimer;
    private List<Friend> friendsList;
    private List<Integer> tempUserID;
    public ChessUI chessUI;
    public SocialPage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
        startTimerForGetFriends();
    }

    private void initComponents()
    {
        makeFriendsPane();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b;");
    }

    public void addListeners()
    {
        searchFriends.setOnAction(event ->
        {
            tempFriendsPaneTable.getChildren().clear();
            Member member=getMemberDetailsUsingID(chessUI.user_ID);

            boolean friendFound=false;
            String searchUsername = searchFriends.getText();
            for (Friend friend:member.getFriends())
            {
                Member tempMember=getMemberDetailsUsingID(friend.getID());
                if (tempMember.getUsername().startsWith(searchUsername))
                {
                    makeSearchFriendsPaneResult(friend);
                    friendFound=true;
                }
            }
            if (!friendFound)
            {
                friendScrollPane.setContent(noFriendsPaneForSearch);
            }

        });
        resetSearchFriends.setOnAction(actionEvent ->
        {
            searchFriends.clear();
            if (friendsPaneTable.getChildren().size()==0)
            {
                tempFriendsPaneTable.getChildren().clear();
                friendScrollPane.setContent(noFriendsPane);
            }
            else
            {
                tempFriendsPaneTable.getChildren().clear();
                friendScrollPane.setContent(friendsPaneTable);
            }
        });
    }

    public void makeFriendsPane()
    {
        friendsPane=new AnchorPane();
        friendsPane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(friendsPane, 40.0);
        AnchorPane.setBottomAnchor(friendsPane, 60.0);
        AnchorPane.setLeftAnchor(friendsPane, 350.0);
        AnchorPane.setRightAnchor(friendsPane, 840.0);

        searchFriends=new TextField();
        searchFriends.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font searchUserFont = Font.font("Sage UI",18);
        searchFriends.setFont(searchUserFont);
        searchFriends.setPromptText("Search Friend");
        AnchorPane.setTopAnchor(searchFriends, 12.0);
        AnchorPane.setLeftAnchor(searchFriends, 99.0);
        AnchorPane.setRightAnchor(searchFriends, 100.0);

        Image resetSearchFriendsImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/SocialPage/Dark/dark reset search friends.png").toExternalForm());
        ImageView resetSearchFriendsImageView = new ImageView(resetSearchFriendsImage);

        resetSearchFriends=new Button();
        resetSearchFriends.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        resetSearchFriends.setGraphic(resetSearchFriendsImageView);
        AnchorPane.setTopAnchor(resetSearchFriends, 12.0);
        AnchorPane.setLeftAnchor(resetSearchFriends, 600.0);
        AnchorPane.setRightAnchor(resetSearchFriends, 10.0);

        friends=new Label("Friend");
        friends.setStyle("");
        Font FriendLabelFont = Font.font("Sage UI",20);
        friends.setFont(FriendLabelFont);
        friends.setOpacity(0.8);
        friends.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(friends, 50.0);
        AnchorPane.setLeftAnchor(friends, 15.0);

        friendsPaneTable=new VBox();
        friendsPaneTable.setSpacing(5);

        tempFriendsPaneTable=new VBox();
        tempFriendsPaneTable.setSpacing(5);

        friendScrollPane=new ScrollPane();
        friendScrollPane.setStyle("-fx-background-color: #262522;-fx-background: #262522;");
        friendScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        AnchorPane.setTopAnchor(friendScrollPane, 90.0);
        AnchorPane.setBottomAnchor(friendScrollPane, 20.0);
        AnchorPane.setLeftAnchor(friendScrollPane, 10.0);
        AnchorPane.setRightAnchor(friendScrollPane, 10.0);

        noFriendsPane=new AnchorPane();
        noFriendsPane.setStyle("-fx-background-color: #262522;");
        noFriendsPane.setPrefHeight(360);
        noFriendsPane.setPrefWidth(690);

        noFriendsPaneLabel=new Label("No Friend");
        Font noFriendLabelFont = Font.font("Sage UI",30);
        noFriendsPaneLabel.setFont(noFriendLabelFont);
        AnchorPane.setTopAnchor(noFriendsPaneLabel, 200.0);
        AnchorPane.setLeftAnchor(noFriendsPaneLabel, 290.0);

        noFriendsPane.getChildren().add(noFriendsPaneLabel);

        noFriendsPaneForSearch=new AnchorPane();
        noFriendsPaneForSearch.setStyle("-fx-background-color: #262522;");
        noFriendsPaneForSearch.setPrefHeight(360);
        noFriendsPaneForSearch.setPrefWidth(690);

        noFriendsPaneLabelForSearch=new Label("No Friend Founded");
        Font noFriendsPaneLabelForSearchFont = Font.font("Sage UI",30);
        noFriendsPaneLabelForSearch.setFont(noFriendsPaneLabelForSearchFont);
        AnchorPane.setTopAnchor(noFriendsPaneLabelForSearch, 200.0);
        AnchorPane.setLeftAnchor(noFriendsPaneLabelForSearch, 235.0);

        noFriendsPaneForSearch.getChildren().add(noFriendsPaneLabelForSearch);

        friendScrollPane.setContent(noFriendsPane);

        friendsPane.getChildren().add(searchFriends);
        friendsPane.getChildren().add(resetSearchFriends);
        friendsPane.getChildren().add(friendScrollPane);

        homeImage = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/HomePage/main page 1.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(750);
        homeImageView.setFitWidth(1470);
        homeImageView.setOpacity(0.6);
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5); // Adjust the blur width as needed
        blur.setHeight(5); // Adjust the blur height as needed
        homeImageView.setEffect(blur);
        getChildren().add(homeImageView);
        getChildren().add(friendsPane);
    }

    public void makeFriendsPaneResult(Friend friend)
    {
        Member member=getMemberDetailsUsingID(friend.getID());

        int friend_ID=friend.getID();

        AnchorPane friendsPanes=new AnchorPane();
        friendsPanes.setStyle("-fx-background-color: #3b3b3b; -fx-background-radius: 20");
        friendsPanes.setPrefSize(690,168);
        friendsPanes.getProperties().put("friend_ID",friend_ID);

        Image defaultProfile=null;
        if (member.getProfile()!=null)
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(member.getProfile());
            // Create an Image object from the InputStream
            defaultProfile = new Image(inputStream);
        }
        else
        {
            defaultProfile = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        }
        ImageView defaultProfileImageView = new ImageView(defaultProfile);
        defaultProfileImageView.setFitHeight(150);
        defaultProfileImageView.setFitWidth(150);
        defaultProfileImageView.setLayoutX(15);
        defaultProfileImageView.setLayoutY(9);
        defaultProfileImageView.setSmooth(true);

        int opponentUser_ID= member.getUserID();

        Label opponentUsername = new Label(member.getUsername());
        Font usernamefont = Font.font("Sage UI",28);
        opponentUsername.setFont(usernamefont);
        opponentUsername.setLayoutX(180);
        opponentUsername.setLayoutY(46);

        Label name = new Label(member.getName());
        Font namefont = Font.font("Sage UI",22);
        name.setFont(namefont);
        name.setLayoutX(180);
        name.setLayoutY(86);
        name.setOpacity(0.8);

        Image unfriendImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/SocialPage/Light/light unfriend.png").toExternalForm());
        ImageView unfriendImageView = new ImageView(unfriendImage);
        unfriendImageView.setFitHeight(40);
        unfriendImageView.setFitWidth(40);
        unfriendImageView.setSmooth(true);

        Button unfriend=new Button();
        unfriend.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        unfriend.setGraphic(unfriendImageView);
        unfriend.setLayoutX(420);
        unfriend.setLayoutY(15);
        unfriend.setPrefHeight(140);
        unfriend.setPrefWidth(75);
        unfriend.setFocusTraversable(false);
        unfriend.getProperties().put("opponentUser_ID",opponentUser_ID);
        unfriend.getProperties().put("paneForRemove",friendsPanes);
        unfriend.setOnAction(events ->
        {
            int requestOpponentUser_ID = (int)unfriend.getProperties().get("opponentUser_ID");

            Platform.runLater(()->
            {
                unfriend.setDisable(true);
                if (tempUserID.contains(requestOpponentUser_ID))
                {
                    Integer integer=requestOpponentUser_ID;
                    tempUserID.remove(integer);
                }
                friendsPaneTable.getChildren().remove(unfriend.getProperties().get("paneForRemove"));
                if (friendsPaneTable.getChildren().size()==0)
                {
                    friendScrollPane.setContent(noFriendsPane);
                }
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponentUser_ID;

                    chessUI.client.execute("/Chess_Server/Remove_Friend",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Time 5 min", "Time 10 min", "Time 15 min","Time 20 min");
        choiceBox.setValue("Time 5 min");
        choiceBox.setStyle("-fx-background-color: #9a9a9a;");
        choiceBox.setLayoutX(510);
        choiceBox.setPrefHeight(50);
        choiceBox.setPrefWidth(165);
        choiceBox.setLayoutY(15);

        Image whiteChessImage = new Image(SocialPage.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/White Pawn.png").toExternalForm());
        ImageView whiteChessImageView = new ImageView(whiteChessImage);
        whiteChessImageView.setFitHeight(65);
        whiteChessImageView.setFitWidth(60);
        whiteChessImageView.setSmooth(true);

        Button whiteChessButton=new Button();
        whiteChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        whiteChessButton.setGraphic(whiteChessImageView);
        whiteChessButton.setLayoutX(510);
        whiteChessButton.setLayoutY(80);
        whiteChessButton.setFocusTraversable(false);
        whiteChessButton.getProperties().put("user_id",friend_ID);
        whiteChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)whiteChessButton.getProperties().get("user_id");
            chessUI.chessTopBar.opponent_user_ID=opponent_ID;

            Platform.runLater(()->
            {
                chessUI.chessPage.chessPane.getChildren().clear();
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.blurWhiteChessBoardView);
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.cancelPane);
                chessUI.chessMenuBar.chess.fire();
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=opponent_ID;
                    transfer.gameType= GAME_TYPE.AS_BLACK;

                    if (choiceBox.getValue().contains("5"))
                    {
                        transfer.time=05;
                    }
                    else
                    {
                        if (choiceBox.getValue().contains("10"))
                        {
                            transfer.time=10;
                        }
                        else
                        {
                            if (choiceBox.getValue().contains("15"))
                            {
                                transfer.time=15;
                            }
                            else
                            {
                                if (choiceBox.getValue().contains("20"))
                                {
                                    transfer.time=20;
                                }
                            }
                        }
                    }
                    chessUI.client.execute("/Chess_Server/Send_Invitation",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        Image blackChessImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/Black Pawn.png").toExternalForm());
        ImageView blackChessImageView = new ImageView(blackChessImage);
        blackChessImageView.setFitHeight(65);
        blackChessImageView.setFitWidth(60);
        blackChessImageView.setSmooth(true);

        Button blackChessButton=new Button();
        blackChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        blackChessButton.setGraphic(blackChessImageView);
        blackChessButton.setLayoutX(600);
        blackChessButton.setLayoutY(80);
        blackChessButton.setFocusTraversable(false);
        blackChessButton.getProperties().put("user_id",friend_ID);
        blackChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)blackChessButton.getProperties().get("user_id");
            chessUI.chessTopBar.opponent_user_ID=opponent_ID;
            Platform.runLater(()->
            {
                chessUI.chessPage.chessPane.getChildren().clear();
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.blurBlackChessBoardView);
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.cancelPane);
                chessUI.chessMenuBar.chess.fire();
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=opponent_ID;
                    transfer.gameType= GAME_TYPE.AS_WHITE;

                    if (choiceBox.getValue().contains("5"))
                    {
                        transfer.time=05;
                    }
                    else
                    {
                        if (choiceBox.getValue().contains("10"))
                        {
                            transfer.time=10;
                        }
                        else
                        {
                            if (choiceBox.getValue().contains("15"))
                            {
                                transfer.time=15;
                            }
                            else
                            {
                                if (choiceBox.getValue().contains("20"))
                                {
                                    transfer.time=20;
                                }
                            }
                        }
                    }

                    chessUI.client.execute("/Chess_Server/Send_Invitation",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        friendsPanes.getChildren().addAll(defaultProfileImageView,opponentUsername,name,unfriend,choiceBox,whiteChessButton,blackChessButton);
        friendsPaneTable.getChildren().add(friendsPanes);
        friendScrollPane.setContent(friendsPaneTable);
    }

    private void makeSearchFriendsPaneResult(Friend friend)
    {
        Member member=getMemberDetailsUsingID(friend.getID());
        int friend_ID=friend.getID();

        AnchorPane friendsPanes=new AnchorPane();
        friendsPanes.setStyle("-fx-background-color: #3b3b3b; -fx-background-radius: 20");
        friendsPanes.setPrefSize(690,168);
        friendsPanes.getProperties().put("friend_ID",friend_ID);

        Image defaultProfile = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        ImageView defaultProfileImageView = new ImageView(defaultProfile);
        defaultProfileImageView.setFitHeight(150);
        defaultProfileImageView.setFitWidth(150);
        defaultProfileImageView.setLayoutX(15);
        defaultProfileImageView.setLayoutY(9);
        defaultProfileImageView.setSmooth(true);

        Label opponentUsername = new Label(member.getUsername());
        Font usernamefont = Font.font("Sage UI",28);
        opponentUsername.setFont(usernamefont);
        opponentUsername.setLayoutX(180);
        opponentUsername.setLayoutY(46);

        Label name = new Label(member.getName());
        Font namefont = Font.font("Sage UI",22);
        name.setFont(namefont);
        name.setLayoutX(180);
        name.setLayoutY(86);
        name.setOpacity(0.8);

        Image unfriendImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/SocialPage/Light/light unfriend.png").toExternalForm());
        ImageView unfriendImageView = new ImageView(unfriendImage);
        unfriendImageView.setFitHeight(35);
        unfriendImageView.setFitWidth(35);
        unfriendImageView.setSmooth(true);

        Button unfriend=new Button();
        unfriend.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        unfriend.setGraphic(unfriendImageView);
        unfriend.setLayoutX(630);
        unfriend.setLayoutY(65);
        unfriend.setFocusTraversable(false);
        unfriend.getProperties().put("friend_ID", friend_ID);
        unfriend.getProperties().put("paneForRemove",friendsPanes);
        unfriend.setOnAction(events ->
        {
            unfriend.setDisable(true);
            int requestOpponentUser_ID = (int)unfriend.getProperties().get("friend_ID");

            Platform.runLater(()->
            {
                if (tempUserID.contains(requestOpponentUser_ID))
                {
                    Integer integer=requestOpponentUser_ID;
                    tempUserID.remove(integer);
                }

                tempFriendsPaneTable.getChildren().remove(unfriend.getProperties().get("paneForRemove"));

                for (Node node : friendsPaneTable.getChildren())
                {
                    AnchorPane friendPane = (AnchorPane) node;
                    int storedUser_ID = (int) friendPane.getProperties().get("friend_ID");
                    if (storedUser_ID==requestOpponentUser_ID)
                    {
                        friendsPaneTable.getChildren().remove(friendPane);
                        if (friendsPaneTable.getChildren().size()==0)
                        {
                            friendScrollPane.setContent(noFriendsPane);
                        }
                        break;
                    }
                }
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponentUser_ID;

                    chessUI.client.execute("/Chess_Server/Remove_Friend",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        Image whiteChessImage = new Image(SocialPage.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/White Pawn.png").toExternalForm());
        ImageView whiteChessImageView = new ImageView(whiteChessImage);
        whiteChessImageView.setFitHeight(40);
        whiteChessImageView.setFitWidth(40);
        whiteChessImageView.setSmooth(true);

        Button whiteChessButton=new Button();
        whiteChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        whiteChessButton.setGraphic(whiteChessImageView);
        whiteChessButton.setLayoutX(500);
        whiteChessButton.setLayoutY(65);
        whiteChessButton.setFocusTraversable(false);
        whiteChessButton.getProperties().put("user_id",friend_ID);
        whiteChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)whiteChessButton.getProperties().get("user_id");
            chessUI.chessTopBar.opponent_user_ID=opponent_ID;

            Platform.runLater(()->
            {
                chessUI.chessPage.chessPane.getChildren().clear();
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.blurWhiteChessBoardView);
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.cancelPane);
                chessUI.chessMenuBar.chess.fire();
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=opponent_ID;
                    transfer.gameType= GAME_TYPE.AS_BLACK;
                    chessUI.client.execute("/Chess_Server/Send_Invitation",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        Image blackChessImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/Black Pawn.png").toExternalForm());
        ImageView blackChessImageView = new ImageView(blackChessImage);
        blackChessImageView.setFitHeight(40);
        blackChessImageView.setFitWidth(40);
        blackChessImageView.setSmooth(true);

        Button blackChessButton=new Button();
        blackChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        blackChessButton.setGraphic(blackChessImageView);
        blackChessButton.setLayoutX(560);
        blackChessButton.setLayoutY(65);
        blackChessButton.setFocusTraversable(false);
        blackChessButton.getProperties().put("user_id",friend_ID);
        blackChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)blackChessButton.getProperties().get("user_id");
            chessUI.chessTopBar.opponent_user_ID=opponent_ID;

            Platform.runLater(()->
            {
                chessUI.chessPage.chessPane.getChildren().clear();
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.blurBlackChessBoardView);
                chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.cancelPane);
                chessUI.chessMenuBar.chess.fire();
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=opponent_ID;
                    transfer.gameType= GAME_TYPE.AS_WHITE;
                    chessUI.client.execute("/Chess_Server/Send_Invitation",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        friendsPanes.getChildren().addAll(defaultProfileImageView,opponentUsername,name,unfriend,whiteChessButton,blackChessButton);
        tempFriendsPaneTable.getChildren().add(friendsPanes);
        friendScrollPane.setContent(tempFriendsPaneTable);
    }


    private void removeFriendsWhoRemovedMe(int user_IDToRemove)
    {
        for (Node node : friendsPaneTable.getChildren())
        {
            AnchorPane friendPane = (AnchorPane) node;
            int storedUser_ID = (int) friendPane.getProperties().get("friend_ID");
            if (storedUser_ID==user_IDToRemove)
            {
                friendsPaneTable.getChildren().remove(friendPane);
                if (friendsPaneTable.getChildren().size()==0)
                {
                    friendScrollPane.setContent(noFriendsPane);
                }
                break;
            }
        }
    }

    private void startTimerForGetFriends()
    {
        tempUserID=new LinkedList<>();
        this.getFriendsTimer=new Timer(2000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    friendsList=new LinkedList<>();
                    List<Object> objects = (List<Object>)chessUI.client.execute("/Chess_Server/Get_Friends",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Friend friend = gson.fromJson(json, Friend.class);
                            friendsList.add(friend);
                        }
                    }
                    if(friendsList.size()!=0)
                    {
                        for(Friend f:friendsList)
                        {
                            if (!tempUserID.contains(f.getID()))
                            {
                                    tempUserID.add(f.getID());
                                    Platform.runLater(() ->
                                    {
                                        makeFriendsPaneResult(f);
                                    });
                            }
                        }
                    }

                    Iterator<Integer> iterator = tempUserID.iterator();
                    while (iterator.hasNext())
                    {
                        Integer element = iterator.next();
                        boolean found = false;
                        for (Friend friend : friendsList)
                        {
                            if (friend.getID() == element)
                            {
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            Platform.runLater(() ->
                            {
                                removeFriendsWhoRemovedMe(element);
                            });
                            iterator.remove();
                        }
                    }

                }catch (Throwable t) // there are some error with updating list
                {
                    System.out.println("Member List c : "+t);
                }
            }
        });
        getFriendsTimer.start();
    }

    public Member getMemberDetailsUsingID(int userID)
    {
        Member member=null;
        try
        {
            Transfer transfer=new Transfer();
            transfer.user_ID=userID;
            Object object = (Object)chessUI.client.execute("/Chess_Server/Get_Member_Details_Using_ID",transfer);
            if (object != null)
            {
                Gson gson1 = new Gson();
                String json = gson1.toJson(object);
                member = gson1.fromJson(json, Member.class);
            }
        } catch (Throwable e)
        {
            System.out.println("Failed to load member details : "+e.getMessage());
        }
        return member;
    }
}