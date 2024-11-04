package com.chess.application.client.components;

import com.chess.application.client.example.LoadingAnimationApp;
import com.chess.application.client.ui.ChessUI;
import com.chess.common.ENUM.GAME_TYPE;
import com.chess.common.ENUM.INVITATION_TYPE;
import com.chess.common.ENUM.REQUEST_TYPE;
import com.chess.common.Member;
import com.chess.common.Request;
import com.chess.common.Invitation;
import com.chess.common.Friend;
import com.chess.common.Transfer;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChessTopBar extends AnchorPane
{
    private Image lightRequestImage,lightInvitationImage,darkRequestImage,darkInvitationImage,incomingRequestImage,incomingInvitationImage,loadingImage;
    private ImageView lightRequestImageView,lightInvitationImageView,darkRequestImageView,darkInvitationImageView,incomingRequestImageView,incomingInvitationImageView,loadingImageView;
    private AnchorPane side,main;
    private TextField searchUser;
    private Button request,invitation;
    private Popup requestPopUp,invitationPopUp,searchPopup;
    private AnchorPane requestPane,noRequestPane,invitationPane,noInvitationPane,searchPane,noSearchPane;
    private ScrollPane requestScrollPane,invitationScrollPane,searchScrollPane;
    private Label noRequestPaneLabel,noInvitationPaneLabel,noSearchPaneLabel,loading;
    private VBox table;
    private VBox requestTable,invitationTable;
    private javax.swing.Timer timerForAcceptedInvitation;
    private javax.swing.Timer timerForGetInvitations;
    private javax.swing.Timer timerForGetRequests;
    private javax.swing.Timer timerForRejectedInvitation;
    private List<Integer> tempOpponentList;
    private List<Integer> tempRequestList;
    private ChessUI chessUI;
    public int opponent_user_ID=0;
    public boolean isPlaying=false;
    public int opponent_elo=0;
    public int player_elo=0;

    public ChessTopBar(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
        startTimerForGetRequests();
        startTimerForGetInvitations();
        startTimerForAcceptedInvitation();
        startTimerForRejectedInvitation();
    }

    private void initComponents()
    {
        createLightImages();
        createDarkImages();
        createIncomingImages();

        makeTopMenuPane();
        makeTopMenuPaneComponents();
        makeTopMenuPaneComponentsFunctionality();

    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #262522;");
        setPrefHeight(65);
        setPrefWidth(1920);
    }

    private void addListeners()
    {
        request.setOnAction(event ->
        {
            request.setGraphic(darkRequestImageView);
            Stage mainStage = (Stage) getScene().getWindow();
            setPopupPosition(mainStage);
            requestPopUp.show(mainStage);
        });
        invitation.setOnAction(event ->
        {
            invitation.setGraphic(darkInvitationImageView);
            Stage mainStage = (Stage) getScene().getWindow();
            setPopupPosition(mainStage);
            invitationPopUp.show(mainStage);
        });

        searchUser.setOnAction(event ->
        {
            loading.setVisible(true);

            String search=searchUser.getText();
            table.getChildren().clear();

            Stage mainStage = (Stage) getScene().getWindow();
            setPopupPosition(mainStage);

            new Thread(() ->
            {
                try
                {
                    List<Member> members=new LinkedList<>();
                    Transfer transfer=new Transfer();
                    transfer.search=search;
                    List<Object> objects = (List<Object>)chessUI.client.execute("/Chess_Server/Chess_Member",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Member user = gson.fromJson(json, Member.class);
                            members.add(user);
                        }
                    }
                    Platform.runLater(() ->
                    {
                        loading.setVisible(false);
                        if (searchUser.getText().isEmpty())
                        {
                            searchPane.setPrefHeight(150);
                            noSearchPaneLabel.setText("Enter Name !!");
                            searchScrollPane.setContent(noSearchPane);
                            searchPopup.show(mainStage);
                        }
                        else
                        {
                            if (!members.isEmpty())
                            {
                                for (Member member : members)
                                {
                                    makeSearchResult(member);
                                }
                                searchPane.setPrefHeight(250);
                                searchScrollPane.setContent(table);
                                searchPopup.show(mainStage);
                            }
                            else
                            {
                                searchPane.setPrefHeight(150);
                                searchScrollPane.setContent(noSearchPane);
                                noSearchPaneLabel.setText("No User Found");
                                searchPopup.show(mainStage);
                            }
                        }
                    });
                }catch (Throwable e)
                {
                    System.out.println("Chess Top Bar Search : "+e.getMessage());
                }
            }).start();
        });
    }
    public void makeTopMenuPane()
    {
        side=new AnchorPane();
        side.setStyle("-fx-background-color: #262522;");
        side.setPrefSize(65,65);

        main=new AnchorPane();
        main.setStyle("-fx-background-color: #262522;"); //#302e2b;
        main.setLayoutX(65);
        main.setPrefSize(1920,65);

        getChildren().add(main);
        getChildren().add(side);

    }

    private void createLightImages()
    {
        lightRequestImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/light request.png").toExternalForm());
        lightRequestImageView = new ImageView(lightRequestImage);
        lightRequestImageView.setFitHeight(24);
        lightRequestImageView.setFitWidth(24);
        lightRequestImageView.setSmooth(true);

        lightInvitationImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/light invitation.png").toExternalForm());
        lightInvitationImageView = new ImageView(lightInvitationImage);
        lightInvitationImageView.setFitHeight(24);
        lightInvitationImageView.setFitWidth(24);
        lightInvitationImageView.setSmooth(true);
    }

    private void createDarkImages()
    {
        darkRequestImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/dark request.png").toExternalForm());
        darkRequestImageView = new ImageView(darkRequestImage);
        darkRequestImageView.setFitHeight(24);
        darkRequestImageView.setFitWidth(24);
        darkRequestImageView.setSmooth(true);

        darkInvitationImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/dark invitation.png").toExternalForm());
        darkInvitationImageView = new ImageView(darkInvitationImage);
        darkInvitationImageView.setFitHeight(24);
        darkInvitationImageView.setFitWidth(24);
        darkInvitationImageView.setSmooth(true);
    }

    private void createIncomingImages()
    {
        loadingImage = new Image(LoadingAnimationApp.class.getResource("/com/chess/application/client/components/Default/Loading/loading 4.gif").toExternalForm());
        loadingImageView = new ImageView(loadingImage);
        loadingImageView.setFitWidth(30);
        loadingImageView.setFitHeight(30);

        incomingRequestImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Incoming/incoming request.png").toExternalForm());
        incomingRequestImageView = new ImageView(incomingRequestImage);
        incomingRequestImageView.setFitHeight(24);
        incomingRequestImageView.setFitWidth(24);
        incomingRequestImageView.setSmooth(true);

        incomingInvitationImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Incoming/incoming invitation.png").toExternalForm());
        incomingInvitationImageView = new ImageView(incomingInvitationImage);
        incomingInvitationImageView.setFitHeight(24);
        incomingInvitationImageView.setFitWidth(24);
        incomingInvitationImageView.setSmooth(true);
    }

    public void makeTopMenuPaneComponents()
    {
        request=new Button();
        request.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        request.setLayoutX(1350); //1350
        request.setLayoutY(18);
        request.setGraphic(darkRequestImageView);

        invitation=new Button();
        invitation.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        invitation.setLayoutX(1400);
        invitation.setLayoutY(18);
        invitation.setGraphic(darkInvitationImageView);

        Font font = Font.font("Sage UI",18);
        searchUser=new TextField();
        searchUser.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        searchUser.setFont(font);
        searchUser.setPromptText("Search User");
        searchUser.setPrefHeight(30);
        searchUser.setPrefWidth(530);
        searchUser.setLayoutX(450);
        searchUser.setLayoutY(13.5);

        loading=new Label();
        loading.setVisible(false);
        loading.setLayoutX(940);
        loading.setLayoutY(17);
        loading.setGraphic(loadingImageView);

        main.getChildren().add(request);
        main.getChildren().add(invitation);
        main.getChildren().add(searchUser);
        main.getChildren().add(loading);

    }

    private void makeTopMenuPaneComponentsFunctionality()
    {
        searchPopup=new Popup();
        searchPopup.setAutoHide(true);
        searchPopup.setX(510);
        searchPopup.setY(90);

        searchPane=new AnchorPane();
        searchPane.setStyle("-fx-background-color: #3b3b3b; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        searchPane.setPrefHeight(250);
        searchPane.setPrefWidth(520);

        table = new VBox();
        table.setSpacing(4);
        table.setLayoutY(2);

        searchScrollPane=new ScrollPane();
        searchScrollPane.setStyle("-fx-background-color: #3b3b3b;-fx-background: #3b3b3b;");
        searchScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        AnchorPane.setTopAnchor(searchScrollPane, 2.0);
        AnchorPane.setBottomAnchor(searchScrollPane, 2.0);
        AnchorPane.setLeftAnchor(searchScrollPane, 2.0);
        AnchorPane.setRightAnchor(searchScrollPane, 2.0);

        searchPane.getChildren().add(searchScrollPane);

        noSearchPane=new AnchorPane();
        noSearchPane.setStyle("-fx-background-color: #3b3b3b;");
        noSearchPane.setPrefWidth(514);
        noSearchPane.setPrefHeight(144);

        noSearchPaneLabel=new Label("No User Found");
        Font noSearchPaneFont = Font.font("Sage UI",18);
        noSearchPaneLabel.setFont(noSearchPaneFont);
        AnchorPane.setTopAnchor(noSearchPaneLabel, 50.0);
        AnchorPane.setLeftAnchor(noSearchPaneLabel, 200.0);


        noSearchPane.getChildren().add(noSearchPaneLabel);

        // Request Components Functionality

        Polygon customShapeForRequest = new Polygon();
        customShapeForRequest.setFill(Color.web("#3b3b3b"));
        customShapeForRequest.getPoints().addAll(
                250.0, 0.0,           // Top-right corner
                265.0, -20.0,         // Slightly to the right and up for the tip
                280.0, 0.0,           // Slightly to the right of the top-right corner
                300.0, 0.0,           // Top-right corner
                300.0, 2.0,         // Bottom-right corner
                0.0, 2.0,           // Bottom-left corner
                0.0, 0.0
        );

        requestPopUp=new Popup();
        requestPopUp.setAutoHide(true);
        requestPopUp.setX(1157);
        requestPopUp.setY(79);

        requestPane=new AnchorPane();
        requestPane.setStyle("-fx-background-color: #3b3b3b; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        requestPane.setPrefHeight(150);
        requestPane.setPrefWidth(300);
        requestPane.getChildren().add(customShapeForRequest);

        requestTable=new VBox();
        requestTable.setSpacing(3);

        requestScrollPane=new ScrollPane();
        requestScrollPane.setPrefHeight(146);
        requestScrollPane.setPrefWidth(296);
        requestScrollPane.setLayoutX(2);
        requestScrollPane.setLayoutY(1.5);
        requestScrollPane.setStyle("-fx-background-color: #3b3b3b;-fx-background: #3b3b3b;");
        requestScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        requestPane.getChildren().add(requestScrollPane);

        noRequestPane=new AnchorPane();
        noRequestPaneLabel=new Label("No Request");
        Font noRequestPaneFont = Font.font("Sage UI",18);
        noRequestPaneLabel.setFont(noRequestPaneFont);
        AnchorPane.setTopAnchor(noRequestPaneLabel, 0.0);
        AnchorPane.setBottomAnchor(noRequestPaneLabel, 0.0);
        AnchorPane.setLeftAnchor(noRequestPaneLabel, 95.0);
        AnchorPane.setRightAnchor(noRequestPaneLabel, 0.0);
        noRequestPane.setStyle("-fx-background-color: #3b3b3b;");
        noRequestPane.setPrefHeight(144);
        noRequestPane.setPrefWidth(294);

        noRequestPane.getChildren().add(noRequestPaneLabel);

        requestScrollPane.setContent(noRequestPane);

        // Invitation Components Functionality

        Polygon customShapeForInvitation = new Polygon();
        customShapeForInvitation.setFill(Color.web("#3b3b3b"));
        customShapeForInvitation.getPoints().addAll(
                250.0, 0.0,           // Top-right corner
                265.0, -20.0,         // Slightly to the right and up for the tip
                280.0, 0.0,           // Slightly to the right of the top-right corner
                300.0, 0.0,           // Top-right corner
                300.0, 2.0,         // Bottom-right corner
                0.0, 2.0,           // Bottom-left corner
                0.0, 0.0
        );

        invitationPopUp=new Popup();
        invitationPopUp.setAutoHide(true);
        invitationPopUp.setX(1209);
        invitationPopUp.setY(79);

        invitationPane=new AnchorPane();
        invitationPane.setStyle("-fx-background-color: #3b3b3b; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        invitationPane.setPrefHeight(150);
        invitationPane.setPrefWidth(300);
        invitationPane.getChildren().add(customShapeForInvitation);

        invitationTable=new VBox();
        invitationTable.setSpacing(3);

        invitationScrollPane=new ScrollPane();
        invitationScrollPane.setPrefHeight(146);
        invitationScrollPane.setPrefWidth(296);
        invitationScrollPane.setLayoutX(2);
        invitationScrollPane.setLayoutY(1.5);
        invitationScrollPane.setStyle("-fx-background-color: #3b3b3b;-fx-background: #3b3b3b;");
        invitationScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        invitationPane.getChildren().add(invitationScrollPane);

        noInvitationPane=new AnchorPane();
        noInvitationPaneLabel=new Label("No Invitation");
        Font noInvitationPaneLabelFont = Font.font("Sage UI",18);
        noInvitationPaneLabel.setFont(noInvitationPaneLabelFont);
        AnchorPane.setTopAnchor(noInvitationPaneLabel, 0.0);
        AnchorPane.setBottomAnchor(noInvitationPaneLabel, 0.0);
        AnchorPane.setLeftAnchor(noInvitationPaneLabel, 95.0);
        AnchorPane.setRightAnchor(noInvitationPaneLabel, 0.0);
        noInvitationPane.setStyle("-fx-background-color: #3b3b3b;");
        noInvitationPane.setPrefHeight(144);
        noInvitationPane.setPrefWidth(294);

        noInvitationPane.getChildren().add(noInvitationPaneLabel);

        invitationScrollPane.setContent(noInvitationPane);

        searchPopup.getContent().add(searchPane);
        requestPopUp.getContent().add(requestPane);
        invitationPopUp.getContent().add(invitationPane);

    }

    private void makeSearchResult(Member member)
    {
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
        defaultProfileImageView.setFitHeight(80);
        defaultProfileImageView.setFitWidth(80);
        defaultProfileImageView.setLayoutX(20);
        defaultProfileImageView.setLayoutY(20);
        defaultProfileImageView.setSmooth(true);

        int user_id= member.getUserID();

        Label username = new Label(member.getUsername());
        Font usernamefont = Font.font("Sage UI",20);
        username.setFont(usernamefont);
        username.setLayoutX(120);
        username.setLayoutY(30);

        Font userfont = Font.font("Sage UI",16);
        Label name = new Label(member.getName());
        name.setFont(userfont);
        name.setLayoutX(120);
        name.setLayoutY(60);
        name.setOpacity(0.8);

        Image requestImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/light request.png").toExternalForm());
        ImageView requestImageView = new ImageView(requestImage);
        requestImageView.setFitHeight(24);
        requestImageView.setFitWidth(24);
        requestImageView.setSmooth(true);

        Button sendRequest=new Button();
        sendRequest.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        sendRequest.setGraphic(requestImageView);
        sendRequest.setLayoutX(318);
        sendRequest.setPrefHeight(98);
        sendRequest.setPrefWidth(55);
        sendRequest.setLayoutY(11);
        sendRequest.setFocusTraversable(false);
        sendRequest.getProperties().put("user_id",user_id);
        sendRequest.setOnAction(events ->
        {
            sendRequest.setDisable(true);
            int opponent_ID = (int)sendRequest.getProperties().get("user_id");

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=opponent_ID;

                    chessUI.client.execute("/Chess_Server/Send_Request",transfer);
                    chessUI.client.execute("/Chess_Server/Set_Pending_Request",transfer);
                }
                catch (Throwable t)
                {

                }
            }).start();
        });

        Member tempMember=getMemberDetailsUsingID(chessUI.user_ID);
        List<Integer> pendingRequests=getPendingRequestsUsingID(chessUI.user_ID);

        if (pendingRequests!=null)
        {
            for (Integer pendindRequest:pendingRequests)
            {
                if (member.getUserID()==pendindRequest)
                {
                    sendRequest.setDisable(true);
                }
            }
        }

        for (Friend friend :tempMember.getFriends())
        {
            if (friend.getID()==member.getUserID())
            {
                sendRequest.setVisible(false);
            }
        }

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Time 5 min", "Time 10 min", "Time 15 min","Time 20 min");
        choiceBox.setValue("Time 5 min");
        choiceBox.setStyle("-fx-background-color: #9a9a9a;");
        choiceBox.setLayoutX(378);
        choiceBox.setPrefHeight(40);
        choiceBox.setPrefWidth(115.5);
        choiceBox.setLayoutY(11);

        Image whiteChessImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/White Pawn.png").toExternalForm());
        ImageView whiteChessImageView = new ImageView(whiteChessImage);
        whiteChessImageView.setFitHeight(43);
        whiteChessImageView.setFitWidth(39);
        whiteChessImageView.setSmooth(true);

        Button whiteChessButton=new Button();
        whiteChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        whiteChessButton.setGraphic(whiteChessImageView);
        whiteChessButton.setLayoutX(378);
        whiteChessButton.setLayoutY(58);
        whiteChessButton.setFocusTraversable(false);
        whiteChessButton.getProperties().put("user_id",user_id);
        whiteChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)whiteChessButton.getProperties().get("user_id");
            opponent_user_ID=opponent_ID;

            Platform.runLater(()->
            {
                searchUser.clear();
                whiteChessButton.setDisable(true);
                searchPopup.hide();
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
        blackChessImageView.setFitHeight(43);
        blackChessImageView.setFitWidth(39);
        blackChessImageView.setSmooth(true);

        Button blackChessButton=new Button();
        blackChessButton.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        blackChessButton.setGraphic(blackChessImageView);
        blackChessButton.setLayoutX(438);
        blackChessButton.setLayoutY(58);
        blackChessButton.setFocusTraversable(false);
        blackChessButton.getProperties().put("user_id",user_id);
        blackChessButton.setOnAction(events ->
        {
            int opponent_ID = (int)blackChessButton.getProperties().get("user_id");
            opponent_user_ID=opponent_ID;

            Platform.runLater(()->
            {
                searchUser.clear();
                blackChessButton.setDisable(true);
                searchPopup.hide();
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

        if (tempMember.getUsername().equalsIgnoreCase(member.getUsername()))
        {
            sendRequest.setVisible(false);
            whiteChessButton.setVisible(false);
            blackChessButton.setVisible(false);
            choiceBox.setVisible(false);
        }

        AnchorPane searchedUser = new AnchorPane();
        searchedUser.getChildren().addAll(defaultProfileImageView,username,name,sendRequest,choiceBox,whiteChessButton,blackChessButton);
        searchedUser.setStyle("-fx-background-color: #262522;");
        searchedUser.setPrefSize(498, 120);
        table.getChildren().add(searchedUser);
    }

    public void makeRequestResult(Request request)
    {
        Member tempMember=getMemberDetailsUsingID(request.getFrom());
        int opponent_ID=request.getFrom();

        AnchorPane requestReceive=new AnchorPane();
        requestReceive.setStyle("-fx-background-color: #262522;");
        requestReceive.setPrefSize(280,70.4);

        Image defaultProfile=null;
        if (tempMember.getProfile()!=null)
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(tempMember.getProfile());
            // Create an Image object from the InputStream
            defaultProfile = new Image(inputStream);
        }
        else
        {
            defaultProfile = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        }

        ImageView defaultProfileImageView = new ImageView(defaultProfile);
        defaultProfileImageView.setFitHeight(50);
        defaultProfileImageView.setFitWidth(50);
        defaultProfileImageView.setLayoutX(5);
        defaultProfileImageView.setLayoutY(10);
        defaultProfileImageView.setSmooth(true);

        Label opponentUsername=new Label(tempMember.getUsername());
        Font opponentUsernameFont = Font.font("Sage UI",16);
        opponentUsername.setFont(opponentUsernameFont);
        AnchorPane.setTopAnchor(opponentUsername,10.0);
        AnchorPane.setLeftAnchor(opponentUsername,60.0);

        Label opponentName = new Label(tempMember.getName());
        Font userfont = Font.font("Sage UI",16);
        opponentName.setFont(userfont);
        AnchorPane.setTopAnchor(opponentName,35.0);
        AnchorPane.setLeftAnchor(opponentName,60.0);
        opponentName.setOpacity(0.8);

        Image requestAcceptImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/invitation accept.png").toExternalForm());
        ImageView requestAcceptImageView = new ImageView(requestAcceptImage);
        requestAcceptImageView.setFitHeight(40);
        requestAcceptImageView.setFitWidth(40);
        requestAcceptImageView.setSmooth(true);

        Button requestAccept=new Button();
        requestAccept.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        requestAccept.setPrefSize(30,30);
        requestAccept.setGraphic(requestAcceptImageView);
        requestAccept.setFocusTraversable(false);
        AnchorPane.setTopAnchor(requestAccept,10.0);
        AnchorPane.setLeftAnchor(requestAccept,164.0);
        requestAccept.getProperties().put("opponent_ID",opponent_ID);
        requestAccept.getProperties().put("opponentUsername",opponentUsername);
        requestAccept.getProperties().put("opponentName",opponentName);
        requestAccept.getProperties().put("paneForRemove",requestReceive);
        requestAccept.setOnAction(events ->
        {
            int requestOpponent_ID = (int)requestAccept.getProperties().get("opponent_ID");

            Platform.runLater(()->
            {
                if(tempRequestList.contains(requestOpponent_ID))
                {
                    Integer integer=requestOpponent_ID;
                    tempRequestList.remove(integer);
                }
                requestTable.getChildren().remove(requestAccept.getProperties().get("paneForRemove"));
                if (requestTable.getChildren().size()==0)
                {
                    requestScrollPane.setContent(noRequestPane);
                }
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponent_ID;

                    chessUI.client.execute("/Chess_Server/Request_Accepted",transfer);
                    chessUI.client.execute("/Chess_Server/Remove_Pending_Request",transfer);

                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }).start();

        });

        Image requestDeclineImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/invitation decline.png").toExternalForm());
        ImageView requestDeclineImageView = new ImageView(requestDeclineImage);
        requestDeclineImageView.setFitHeight(40);
        requestDeclineImageView.setFitWidth(40);
        requestDeclineImageView.setSmooth(true);

        Button requestDecline=new Button();
        requestDecline.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        requestDecline.setPrefSize(30,30);
        requestDecline.setGraphic(requestDeclineImageView);
        requestDecline.setFocusTraversable(false);
        AnchorPane.setTopAnchor(requestDecline,10.0);
        AnchorPane.setLeftAnchor(requestDecline,222.0);
        requestDecline.getProperties().put("opponent_ID",opponent_ID);
        requestDecline.getProperties().put("opponentUsername",opponentUsername);
        requestDecline.getProperties().put("paneForRemove",requestReceive);
        requestDecline.setOnAction(events ->
        {
            int requestOpponent_ID = (int)requestDecline.getProperties().get("opponent_ID");

            Platform.runLater(()->
            {
                if(tempRequestList.contains(requestOpponent_ID))
                {
                    Integer integer=requestOpponent_ID;
                    tempRequestList.remove(integer);
                }
                requestTable.getChildren().remove(requestDecline.getProperties().get("paneForRemove"));
                if (requestTable.getChildren().size()==0)
                {
                    requestScrollPane.setContent(noRequestPane);
                }
                requestReceive.getChildren().addAll(defaultProfileImageView,opponentUsername,opponentName,requestAccept,requestDecline);

                requestTable.getChildren().addAll(requestReceive);
                requestScrollPane.setContent(requestTable);
            });
            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponent_ID;

                    chessUI.client.execute("/Chess_Server/Request_Reject",transfer);
                    chessUI.client.execute("/Chess_Server/Remove_Pending_Request",transfer);

                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }).start();
        });

        requestReceive.getChildren().addAll(defaultProfileImageView,opponentUsername,opponentName,requestAccept,requestDecline);

        requestTable.getChildren().addAll(requestReceive);
        requestScrollPane.setContent(requestTable);
    }


    public void makeInvitationsResult(Invitation invitation)
    {
        Member tempMember=getMemberDetailsUsingID(invitation.getFrom());
        int opponent_ID=invitation.getFrom();
        GAME_TYPE gameType=invitation.getGameType();

        AnchorPane invitationReceive=new AnchorPane();
        invitationReceive.getProperties().put("opponent_ID",opponent_ID);

        invitationReceive.setStyle("-fx-background-color: #262522;");
        invitationReceive.setPrefSize(280,70.4);

        if (invitation.getGameType()==GAME_TYPE.AS_WHITE)
        {
            Image gameTypeImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/White Pawn.png").toExternalForm());
            ImageView gameTypeImageView = new ImageView(gameTypeImage);
            gameTypeImageView.setFitHeight(40);
            gameTypeImageView.setFitWidth(40);
            gameTypeImageView.setSmooth(true);
            AnchorPane.setTopAnchor(gameTypeImageView,30.0);
            AnchorPane.setLeftAnchor(gameTypeImageView,190.0);
            invitationReceive.getChildren().add(gameTypeImageView);
        }
        else
        {
            Image gameTypeImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/Black Pawn.png").toExternalForm());
            ImageView gameTypeImageView = new ImageView(gameTypeImage);
            gameTypeImageView.setFitHeight(40);
            gameTypeImageView.setFitWidth(40);
            gameTypeImageView.setSmooth(true);
            AnchorPane.setTopAnchor(gameTypeImageView,30.0);
            AnchorPane.setLeftAnchor(gameTypeImageView,190.0);
            invitationReceive.getChildren().add(gameTypeImageView);
        }

        Image defaultProfile=null;
        if (tempMember.getProfile()!=null)
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(tempMember.getProfile());
            // Create an Image object from the InputStream
            defaultProfile = new Image(inputStream);
        }
        else
        {
            defaultProfile = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        }

        ImageView defaultProfileImageView = new ImageView(defaultProfile);
        defaultProfileImageView.setFitHeight(50);
        defaultProfileImageView.setFitWidth(50);
        defaultProfileImageView.setLayoutX(5);
        defaultProfileImageView.setLayoutY(10);
        defaultProfileImageView.setSmooth(true);

        Label opponentUsername=new Label(tempMember.getUsername());
        Font opponentUsernameFont = Font.font("Sage UI",16);
        opponentUsername.setFont(opponentUsernameFont);
        AnchorPane.setTopAnchor(opponentUsername,10.0);
        AnchorPane.setLeftAnchor(opponentUsername,60.0);

        Label opponentName=new Label(tempMember.getName());
        Font opponentNameFont = Font.font("Sage UI",16);
        opponentName.setFont(opponentNameFont);
        AnchorPane.setTopAnchor(opponentName,35.0);
        AnchorPane.setLeftAnchor(opponentName,60.0);

        Label time=new Label(invitation.getTime()+" Min");
        Font timeFont = Font.font("Sage UI",16);
        time.setFont(timeFont);
        AnchorPane.setTopAnchor(time,8.0);
        AnchorPane.setLeftAnchor(time,183.0);

        Image invitationAcceptImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Light/invitation accept.png").toExternalForm());
        ImageView invitationAcceptImageView = new ImageView(invitationAcceptImage);
        invitationAcceptImageView.setFitHeight(24);
        invitationAcceptImageView.setFitWidth(25);
        invitationAcceptImageView.setSmooth(true);

        Button invitationAccept=new Button();
        invitationAccept.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        invitationAccept.setFocusTraversable(false);
        invitationAccept.setGraphic(invitationAcceptImageView);
        AnchorPane.setTopAnchor(invitationAccept,2.0);
        AnchorPane.setLeftAnchor(invitationAccept,236.0);
        invitationAccept.getProperties().put("opponent_ID",opponent_ID);
        invitationAccept.getProperties().put("gameType",gameType);
        invitationAccept.getProperties().put("opponentUsername",opponentUsername);
        invitationAccept.getProperties().put("paneForRemove",invitationReceive);
        invitationAccept.setOnAction(events ->
        {
            GAME_TYPE gameTypeForOpponent = (GAME_TYPE) invitationAccept.getProperties().get("gameType");
            int requestOpponent_ID = (int)invitationAccept.getProperties().get("opponent_ID");
            opponent_user_ID=requestOpponent_ID;
            isPlaying=true;
            tempOpponentList.clear();

            Platform.runLater(()->
            {
                invitationTable.getChildren().clear();
                if (invitationTable.getChildren().size()==0)
                {
                    invitationScrollPane.setContent(noInvitationPane);
                }

                if(gameTypeForOpponent==GAME_TYPE.AS_WHITE)
                {
                    if (invitation.getTime()==5)
                    {
                        chessUI.chessPage.timerForPlayer.setText("05:00");
                        chessUI.chessPage.timerForOppnent.setText("05:00");
                        chessUI.chessPage.elapsedSecondsForPlayer=300;
                        chessUI.chessPage.elapsedSecondsForOpponent=300;
                        chessUI.chessPage.timelineForPlayer.setCycleCount(300);
                        chessUI.chessPage.timelineForOpponent.setCycleCount(300);
                    }
                    else
                    {
                        if (invitation.getTime()==10)
                        {
                            chessUI.chessPage.timerForPlayer.setText("10:00");
                            chessUI.chessPage.timerForOppnent.setText("10:00");
                            chessUI.chessPage.elapsedSecondsForPlayer=600;
                            chessUI.chessPage.elapsedSecondsForOpponent=600;
                            chessUI.chessPage.timelineForPlayer.setCycleCount(600);
                            chessUI.chessPage.timelineForOpponent.setCycleCount(600);
                        }
                        else
                        {
                            if (invitation.getTime()==15)
                            {
                                chessUI.chessPage.timerForPlayer.setText("15:00");
                                chessUI.chessPage.timerForOppnent.setText("15:00");
                                chessUI.chessPage.elapsedSecondsForPlayer=900;
                                chessUI.chessPage.elapsedSecondsForOpponent=900;
                                chessUI.chessPage.timelineForPlayer.setCycleCount(900);
                                chessUI.chessPage.timelineForOpponent.setCycleCount(900);
                            }
                            else
                            {
                                if (invitation.getTime()==20)
                                {
                                    chessUI.chessPage.timerForPlayer.setText("20:00");
                                    chessUI.chessPage.timerForOppnent.setText("20:00");
                                    chessUI.chessPage.elapsedSecondsForPlayer=1200;
                                    chessUI.chessPage.elapsedSecondsForOpponent=1200;
                                    chessUI.chessPage.timelineForPlayer.setCycleCount(1200);
                                    chessUI.chessPage.timelineForOpponent.setCycleCount(1200);
                                }
                            }
                        }
                    }

                    Member player=getMemberDetailsUsingID(chessUI.user_ID);
                    Member opponent=getMemberDetailsUsingID(requestOpponent_ID);

                    chessUI.chessPage.defaultProfileForPlayer=null;
                    if (player.getProfile()!=null)
                    {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(player.getProfile());
                        // Create an Image object from the InputStream
                        chessUI.chessPage.defaultProfileForPlayer = new Image(inputStream);
                    }
                    else
                    {
                        chessUI.chessPage.defaultProfileForPlayer = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                    }
                    chessUI.chessPage.defaultProfileForPlayerImageView = new ImageView(chessUI.chessPage.defaultProfileForPlayer);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitHeight(45);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitWidth(45);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutX(60);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutY(602);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setSmooth(true);

                    chessUI.chessPage.defaultProfileForOpponent=null;
                    if (opponent.getProfile()!=null)
                    {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(opponent.getProfile());
                        // Create an Image object from the InputStream
                        chessUI.chessPage.defaultProfileForOpponent = new Image(inputStream);
                    }
                    else
                    {
                        chessUI.chessPage.defaultProfileForOpponent = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                    }
                    chessUI.chessPage.defaultProfileForOpponentImageView = new ImageView(chessUI.chessPage.defaultProfileForOpponent);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitHeight(45);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitWidth(45);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutX(60);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutY(2);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setSmooth(true);

                    chessUI.chessPage.playerName.setText(player.getUsername());
                    chessUI.chessPage.playerElo.setText("("+player.getElo()+")");
                    chessUI.chessPage.opponentName.setText(opponent.getUsername());
                    chessUI.chessPage.opponentElo.setText("("+opponent.getElo()+")");
                    chessUI.chessPage.chessPane.getChildren().clear();
                    chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.whiteChessBoard,chessUI.chessPage.playerBox,chessUI.chessPage.defaultProfileForPlayerImageView,chessUI.chessPage.opponentBox,chessUI.chessPage.defaultProfileForOpponentImageView,chessUI.chessPage.resign);
                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForPlayer);
                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForOppnent);
                    chessUI.chessPage.timelineForPlayer.play();
                    chessUI.chessPage.tempTimer.start();
                }
                else
                {
                    if (invitation.getTime()==5)
                    {
                        chessUI.chessPage.timerForPlayer.setText("05:00");
                        chessUI.chessPage.timerForOppnent.setText("05:00");
                        chessUI.chessPage.elapsedSecondsForPlayer=300;
                        chessUI.chessPage.elapsedSecondsForOpponent=300;
                        chessUI.chessPage.timelineForPlayer.setCycleCount(300);
                        chessUI.chessPage.timelineForOpponent.setCycleCount(300);
                    }
                    else
                    {
                        if (invitation.getTime()==10)
                        {
                            chessUI.chessPage.timerForPlayer.setText("10:00");
                            chessUI.chessPage.timerForOppnent.setText("10:00");
                            chessUI.chessPage.elapsedSecondsForPlayer=600;
                            chessUI.chessPage.elapsedSecondsForOpponent=600;
                            chessUI.chessPage.timelineForPlayer.setCycleCount(600);
                            chessUI.chessPage.timelineForOpponent.setCycleCount(600);
                        }
                        else
                        {
                            if (invitation.getTime()==15)
                            {
                                chessUI.chessPage.timerForPlayer.setText("15:00");
                                chessUI.chessPage.timerForOppnent.setText("15:00");
                                chessUI.chessPage.elapsedSecondsForPlayer=900;
                                chessUI.chessPage.elapsedSecondsForOpponent=900;
                                chessUI.chessPage.timelineForPlayer.setCycleCount(900);
                                chessUI.chessPage.timelineForOpponent.setCycleCount(900);
                            }
                            else
                            {
                                if (invitation.getTime()==20)
                                {
                                    chessUI.chessPage.timerForPlayer.setText("20:00");
                                    chessUI.chessPage.timerForOppnent.setText("20:00");
                                    chessUI.chessPage.elapsedSecondsForPlayer=1200;
                                    chessUI.chessPage.elapsedSecondsForOpponent=1200;
                                    chessUI.chessPage.timelineForPlayer.setCycleCount(1200);
                                    chessUI.chessPage.timelineForOpponent.setCycleCount(1200);
                                }
                            }
                        }
                    }

                    Member player=getMemberDetailsUsingID(chessUI.user_ID);
                    Member opponent=getMemberDetailsUsingID(requestOpponent_ID);

                    chessUI.chessPage.defaultProfileForPlayer=null;
                    if (player.getProfile()!=null)
                    {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(player.getProfile());
                        // Create an Image object from the InputStream
                        chessUI.chessPage.defaultProfileForPlayer = new Image(inputStream);
                    }
                    else
                    {
                        chessUI.chessPage.defaultProfileForPlayer = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                    }
                    chessUI.chessPage.defaultProfileForPlayerImageView = new ImageView(chessUI.chessPage.defaultProfileForPlayer);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitHeight(45);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitWidth(45);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutX(60);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutY(602);
                    chessUI.chessPage.defaultProfileForPlayerImageView.setSmooth(true);

                    chessUI.chessPage.defaultProfileForOpponent=null;
                    if (opponent.getProfile()!=null)
                    {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(opponent.getProfile());
                        // Create an Image object from the InputStream
                        chessUI.chessPage.defaultProfileForOpponent = new Image(inputStream);
                    }
                    else
                    {
                        chessUI.chessPage.defaultProfileForOpponent = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                    }
                    chessUI.chessPage.defaultProfileForOpponentImageView = new ImageView(chessUI.chessPage.defaultProfileForOpponent);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitHeight(45);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitWidth(45);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutX(60);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutY(2);
                    chessUI.chessPage.defaultProfileForOpponentImageView.setSmooth(true);

                    chessUI.chessPage.playerName.setText(player.getUsername());
                    chessUI.chessPage.playerElo.setText("("+player.getElo()+")");
                    chessUI.chessPage.opponentName.setText(opponent.getUsername());
                    chessUI.chessPage.opponentElo.setText("("+opponent.getElo()+")");
                    chessUI.chessPage.chessPane.getChildren().clear();
                    chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.blackChessBoard,chessUI.chessPage.playerBox,chessUI.chessPage.defaultProfileForPlayerImageView,chessUI.chessPage.opponentBox,chessUI.chessPage.defaultProfileForOpponentImageView,chessUI.chessPage.resign);
                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForPlayer);
                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForOppnent);
                    chessUI.chessPage.timelineForOpponent.play();
                    chessUI.chessPage.tempTimer.start();
                }

                invitationPopUp.hide();
                chessUI.chessMenuBar.chess.fire();

                if (gameTypeForOpponent==GAME_TYPE.AS_WHITE)
                {
                    chessUI.chessPage.whiteChessBoard.timerForMove.start();
                    chessUI.chessPage.whiteChessBoard.timerForResign.start();
                }
                else
                {
                    if (gameTypeForOpponent==GAME_TYPE.AS_BLACK)
                    {
                        chessUI.chessPage.blackChessBoard.timerForMove.start();
                        chessUI.chessPage.blackChessBoard.timerForResign.start();

                    }
                }
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponent_ID;
                    transfer.time=invitation.getTime();
                    if (gameTypeForOpponent==GAME_TYPE.AS_WHITE)
                    {
                        transfer.gameType=GAME_TYPE.AS_BLACK;
                    }
                    else
                    {
                        if (gameTypeForOpponent==GAME_TYPE.AS_BLACK)
                        {
                            transfer.gameType=GAME_TYPE.AS_WHITE;
                        }
                    }

                    chessUI.client.execute("/Chess_Server/Reject_All_Invitations_Because_One_Invitation_Is_Accepted",transfer);
                    chessUI.client.execute("/Chess_Server/Send_Accepted_Invitation",transfer);

                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }).start();

        });

        Image invitationDeclineImage = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/ChessTopBar/Dark/invitation decline.png").toExternalForm());
        ImageView invitationDeclineImageView = new ImageView(invitationDeclineImage);
        invitationDeclineImageView.setFitHeight(24);
        invitationDeclineImageView.setFitWidth(25);
        invitationDeclineImageView.setSmooth(true);

        Button invitationDecline=new Button();
        invitationDecline.setStyle("-fx-background-color: #9a9a9a; -fx-image-rendering: optimizeQuality;");
        invitationDecline.setGraphic(invitationDeclineImageView);
        invitationDecline.setFocusTraversable(false);
        AnchorPane.setTopAnchor(invitationDecline,36.0);
        AnchorPane.setLeftAnchor(invitationDecline,236.0);
        invitationDecline.getProperties().put("opponent_ID",opponent_ID);
        invitationDecline.getProperties().put("opponentUsername",opponentUsername);
        invitationDecline.getProperties().put("paneForRemove",invitationReceive);
        invitationDecline.setOnAction(events ->
        {
            int requestOpponent_ID = (int)invitationDecline.getProperties().get("opponent_ID");

            Platform.runLater(()->
            {
                if(tempOpponentList.contains(requestOpponent_ID))
                {
                    Integer integer=requestOpponent_ID;
                    tempOpponentList.remove(integer);
                }
                invitationTable.getChildren().remove(invitationDecline.getProperties().get("paneForRemove"));
                if (invitationTable.getChildren().size()==0)
                {
                    invitationScrollPane.setContent(noInvitationPane);
                }
            });

            new Thread(()->
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.opponent_ID=requestOpponent_ID;

                    chessUI.client.execute("/Chess_Server/Reject_Invitation",transfer);
                    chessUI.client.execute("/Chess_Server/Send_Rejected_Invitation",transfer);

                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }).start();
        });

        invitationReceive.getChildren().addAll(defaultProfileImageView,opponentUsername,opponentName,time,invitationAccept,invitationDecline);

        invitationTable.getChildren().addAll(invitationReceive);
        invitationScrollPane.setContent(invitationTable);
    }

    private void startTimerForGetRequests()
    {
        tempRequestList=new LinkedList<>();
        this.timerForGetRequests=new javax.swing.Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    List<Request> requestList=new LinkedList<>();
                    List<Object> objects = (List<Object>) chessUI.client.execute("/Chess_Server/Get_Requests",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Request request = gson.fromJson(json, Request.class);
                            requestList.add(request);
                        }
                    }
                    if(requestList.size()!=0)
                    {
                        for(Request tempRequest:requestList)
                        {
                            if(tempRequestList.contains(tempRequest.getFrom())==false)
                            {
                                tempRequestList.add(tempRequest.getFrom());
                                if(tempRequest.getTo()==chessUI.user_ID && tempRequest.getRequestType()== REQUEST_TYPE.REQUEST)
                                {
                                    Platform.runLater(() ->
                                    {
                                        request.setGraphic(incomingRequestImageView);
                                        makeRequestResult(tempRequest);
                                    });
                                }
                            }
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
        timerForGetRequests.start();
    }

    public void startTimerForGetInvitations()
    {
        tempOpponentList=new LinkedList<>();
        this.timerForGetInvitations=new javax.swing.Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    List<Invitation> opponentList=new LinkedList<>();
                    List<Object> objects = (List<Object>) chessUI.client.execute("/Chess_Server/Get_Invitations",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Invitation invitation = gson.fromJson(json, Invitation.class);
                            opponentList.add(invitation);
                        }
                    }
                    if(opponentList.size()!=0)
                    {
                        for(Invitation tempInvitation:opponentList)
                        {
                            if(tempOpponentList.contains(tempInvitation.getFrom())==false)
                            {
                                tempOpponentList.add(tempInvitation.getFrom());
                                if(tempInvitation.getTo()==chessUI.user_ID && tempInvitation.getType()== INVITATION_TYPE.INVITATION)
                                {
                                    Platform.runLater(() ->
                                    {
                                        invitation.setGraphic(incomingInvitationImageView);
                                        makeInvitationsResult(tempInvitation);
                                    });
                                }
                            }
                        }
                    }
                    Iterator<Integer> iterator = tempOpponentList.iterator();
                    while (iterator.hasNext())
                    {
                        Integer element = iterator.next();
                        boolean found = false;
                        for (Invitation invitation : opponentList)
                        {
                            if (invitation.getFrom() == element)
                            {
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            Platform.runLater(() ->
                            {
                                removeInvitations(element);
                            });
                            iterator.remove();
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
        timerForGetInvitations.start();
    }

    private void startTimerForAcceptedInvitation()
    {
        this.timerForAcceptedInvitation=new javax.swing.Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    List<Invitation> acceptedInvitation=new LinkedList<>();
                    List<Object> objects = (List<Object>) chessUI.client.execute("/Chess_Server/Get_Accepted_Invitations",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Invitation invitation = gson.fromJson(json, Invitation.class);
                            acceptedInvitation.add(invitation);
                        }
                    }
                    if(acceptedInvitation.size()!=0)
                    {
                        // Shut down the timerForAcceptedChallenge when acceptedChallenges are available
                        for(Invitation invitation:acceptedInvitation)
                        {
                            if(invitation.getTo()==chessUI.user_ID && invitation.getType()== INVITATION_TYPE.INVITATION_ACCEPTED && invitation.getGameType()==GAME_TYPE.AS_WHITE)
                            {
                                isPlaying=true;
                                chessUI.chessPage.whiteChessBoard.timerForMove.start();
                                chessUI.chessPage.whiteChessBoard.timerForResign.start();
                                Platform.runLater(() ->
                                {
                                    if (invitation.getTime()==5)
                                    {
                                        chessUI.chessPage.timerForPlayer.setText("05:00");
                                        chessUI.chessPage.timerForOppnent.setText("05:00");
                                        chessUI.chessPage.elapsedSecondsForPlayer=300;
                                        chessUI.chessPage.elapsedSecondsForOpponent=300;
                                        chessUI.chessPage.timelineForPlayer.setCycleCount(300);
                                        chessUI.chessPage.timelineForOpponent.setCycleCount(300);
                                    }
                                    else
                                    {
                                        if (invitation.getTime()==10)
                                        {
                                            chessUI.chessPage.timerForPlayer.setText("10:00");
                                            chessUI.chessPage.timerForOppnent.setText("10:00");
                                            chessUI.chessPage.elapsedSecondsForPlayer=600;
                                            chessUI.chessPage.elapsedSecondsForOpponent=600;
                                            chessUI.chessPage.timelineForPlayer.setCycleCount(600);
                                            chessUI.chessPage.timelineForOpponent.setCycleCount(600);
                                        }
                                        else
                                        {
                                            if (invitation.getTime()==15)
                                            {
                                                chessUI.chessPage.timerForPlayer.setText("15:00");
                                                chessUI.chessPage.timerForOppnent.setText("15:00");
                                                chessUI.chessPage.elapsedSecondsForPlayer=900;
                                                chessUI.chessPage.elapsedSecondsForOpponent=900;
                                                chessUI.chessPage.timelineForPlayer.setCycleCount(900);
                                                chessUI.chessPage.timelineForOpponent.setCycleCount(900);
                                            }
                                            else
                                            {
                                                if (invitation.getTime()==20)
                                                {
                                                    chessUI.chessPage.timerForPlayer.setText("20:00");
                                                    chessUI.chessPage.timerForOppnent.setText("20:00");
                                                    chessUI.chessPage.elapsedSecondsForPlayer=1200;
                                                    chessUI.chessPage.elapsedSecondsForOpponent=1200;
                                                    chessUI.chessPage.timelineForPlayer.setCycleCount(1200);
                                                    chessUI.chessPage.timelineForOpponent.setCycleCount(1200);
                                                }
                                            }
                                        }
                                    }

                                    Member player=getMemberDetailsUsingID(chessUI.user_ID);
                                    Member opponent=getMemberDetailsUsingID(invitation.getFrom());

                                    chessUI.chessPage.defaultProfileForPlayer=null;
                                    if (player.getProfile()!=null)
                                    {
                                        ByteArrayInputStream inputStream = new ByteArrayInputStream(player.getProfile());
                                        // Create an Image object from the InputStream
                                        chessUI.chessPage.defaultProfileForPlayer = new Image(inputStream);
                                    }
                                    else
                                    {
                                        chessUI.chessPage.defaultProfileForPlayer = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                                    }
                                    chessUI.chessPage.defaultProfileForPlayerImageView = new ImageView(chessUI.chessPage.defaultProfileForPlayer);
                                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitHeight(45);
                                    chessUI.chessPage.defaultProfileForPlayerImageView.setFitWidth(45);
                                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutX(60);
                                    chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutY(602);
                                    chessUI.chessPage.defaultProfileForPlayerImageView.setSmooth(true);

                                    chessUI.chessPage.defaultProfileForOpponent=null;
                                    if (opponent.getProfile()!=null)
                                    {
                                        ByteArrayInputStream inputStream = new ByteArrayInputStream(opponent.getProfile());
                                        // Create an Image object from the InputStream
                                        chessUI.chessPage.defaultProfileForOpponent = new Image(inputStream);
                                    }
                                    else
                                    {
                                        chessUI.chessPage.defaultProfileForOpponent = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                                    }
                                    chessUI.chessPage.defaultProfileForOpponentImageView = new ImageView(chessUI.chessPage.defaultProfileForOpponent);
                                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitHeight(45);
                                    chessUI.chessPage.defaultProfileForOpponentImageView.setFitWidth(45);
                                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutX(60);
                                    chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutY(2);
                                    chessUI.chessPage.defaultProfileForOpponentImageView.setSmooth(true);

                                    chessUI.chessPage.playerName.setText(player.getUsername());
                                    chessUI.chessPage.playerElo.setText("("+player.getElo()+")");
                                    chessUI.chessPage.opponentName.setText(opponent.getUsername());
                                    chessUI.chessPage.opponentElo.setText("("+opponent.getElo()+")");
                                    chessUI.chessPage.chessPane.getChildren().clear();
                                    chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.whiteChessBoard,chessUI.chessPage.playerBox,chessUI.chessPage.defaultProfileForPlayerImageView,chessUI.chessPage.opponentBox,chessUI.chessPage.defaultProfileForOpponentImageView,chessUI.chessPage.resign);
                                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForPlayer);
                                    chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForOppnent);
                                    chessUI.chessPage.timelineForPlayer.play();
                                    chessUI.chessPage.tempTimer.start();
                                    chessUI.chessMenuBar.chess.fire();
                                });
                                try
                                {
                                    Transfer tempTransfer=new Transfer();
                                    tempTransfer.user_ID=chessUI.user_ID;
                                    tempTransfer.opponent_ID=invitation.getFrom();
                                    chessUI.client.execute("/Chess_Server/Remove_Accepted_Invitation",tempTransfer);
                                }catch (Throwable t)
                                {
                                    System.out.println("Member List : "+t);
                                }
                            }
                            else
                            {
                                if(invitation.getTo()==chessUI.user_ID && invitation.getType()== INVITATION_TYPE.INVITATION_ACCEPTED && invitation.getGameType()==GAME_TYPE.AS_BLACK)
                                {
                                    isPlaying=true;
                                    chessUI.chessPage.blackChessBoard.timerForMove.start();
                                    chessUI.chessPage.blackChessBoard.timerForResign.start();

                                    Platform.runLater(() ->
                                    {
                                        if (invitation.getTime()==5)
                                        {
                                            chessUI.chessPage.timerForPlayer.setText("05:00");
                                            chessUI.chessPage.timerForOppnent.setText("05:00");
                                            chessUI.chessPage.elapsedSecondsForPlayer=300;
                                            chessUI.chessPage.elapsedSecondsForOpponent=300;
                                            chessUI.chessPage.timelineForPlayer.setCycleCount(300);
                                            chessUI.chessPage.timelineForOpponent.setCycleCount(300);
                                        }
                                        else
                                        {
                                            if (invitation.getTime()==10)
                                            {
                                                chessUI.chessPage.timerForPlayer.setText("10:00");
                                                chessUI.chessPage.timerForOppnent.setText("10:00");
                                                chessUI.chessPage.elapsedSecondsForPlayer=600;
                                                chessUI.chessPage.elapsedSecondsForOpponent=600;
                                                chessUI.chessPage.timelineForPlayer.setCycleCount(600);
                                                chessUI.chessPage.timelineForOpponent.setCycleCount(600);
                                            }
                                            else
                                            {
                                                if (invitation.getTime()==15)
                                                {
                                                    chessUI.chessPage.timerForPlayer.setText("15:00");
                                                    chessUI.chessPage.timerForOppnent.setText("15:00");
                                                    chessUI.chessPage.elapsedSecondsForPlayer=900;
                                                    chessUI.chessPage.elapsedSecondsForOpponent=900;
                                                    chessUI.chessPage.timelineForPlayer.setCycleCount(900);
                                                    chessUI.chessPage.timelineForOpponent.setCycleCount(900);
                                                }
                                                else
                                                {
                                                    if (invitation.getTime()==20)
                                                    {
                                                        chessUI.chessPage.timerForPlayer.setText("20:00");
                                                        chessUI.chessPage.timerForOppnent.setText("20:00");
                                                        chessUI.chessPage.elapsedSecondsForPlayer=1200;
                                                        chessUI.chessPage.elapsedSecondsForOpponent=1200;
                                                        chessUI.chessPage.timelineForPlayer.setCycleCount(1200);
                                                        chessUI.chessPage.timelineForOpponent.setCycleCount(1200);
                                                    }
                                                }
                                            }
                                        }

                                        Member player=getMemberDetailsUsingID(chessUI.user_ID);
                                        Member opponent=getMemberDetailsUsingID(invitation.getFrom());

                                        chessUI.chessPage.defaultProfileForPlayer=null;
                                        if (player.getProfile()!=null)
                                        {
                                            ByteArrayInputStream inputStream = new ByteArrayInputStream(player.getProfile());
                                            // Create an Image object from the InputStream
                                            chessUI.chessPage.defaultProfileForPlayer = new Image(inputStream);
                                        }
                                        else
                                        {
                                            chessUI.chessPage.defaultProfileForPlayer = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                                        }
                                        chessUI.chessPage.defaultProfileForPlayerImageView = new ImageView(chessUI.chessPage.defaultProfileForPlayer);
                                        chessUI.chessPage.defaultProfileForPlayerImageView.setFitHeight(45);
                                        chessUI.chessPage.defaultProfileForPlayerImageView.setFitWidth(45);
                                        chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutX(60);
                                        chessUI.chessPage.defaultProfileForPlayerImageView.setLayoutY(602);
                                        chessUI.chessPage.defaultProfileForPlayerImageView.setSmooth(true);

                                        chessUI.chessPage.defaultProfileForOpponent=null;
                                        if (opponent.getProfile()!=null)
                                        {
                                            ByteArrayInputStream inputStream = new ByteArrayInputStream(opponent.getProfile());
                                            // Create an Image object from the InputStream
                                            chessUI.chessPage.defaultProfileForOpponent = new Image(inputStream);
                                        }
                                        else
                                        {
                                            chessUI.chessPage.defaultProfileForOpponent = new Image(ChessTopBar.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                                        }
                                        chessUI.chessPage.defaultProfileForOpponentImageView = new ImageView(chessUI.chessPage.defaultProfileForOpponent);
                                        chessUI.chessPage.defaultProfileForOpponentImageView.setFitHeight(45);
                                        chessUI.chessPage.defaultProfileForOpponentImageView.setFitWidth(45);
                                        chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutX(60);
                                        chessUI.chessPage.defaultProfileForOpponentImageView.setLayoutY(2);
                                        chessUI.chessPage.defaultProfileForOpponentImageView.setSmooth(true);

                                        chessUI.chessPage.playerName.setText(player.getUsername());
                                        chessUI.chessPage.playerElo.setText("("+player.getElo()+")");
                                        chessUI.chessPage.opponentName.setText(opponent.getUsername());
                                        chessUI.chessPage.opponentElo.setText("("+opponent.getElo()+")");
                                        chessUI.chessPage.chessPane.getChildren().clear();
                                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.blackChessBoard,chessUI.chessPage.playerBox,chessUI.chessPage.defaultProfileForPlayerImageView,chessUI.chessPage.opponentBox,chessUI.chessPage.defaultProfileForOpponentImageView,chessUI.chessPage.resign);
                                        chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForPlayer);
                                        chessUI.chessPage.chessPane.getChildren().add(chessUI.chessPage.timerForOppnent);
                                        chessUI.chessPage.timelineForOpponent.play();
                                        chessUI.chessPage.tempTimer.start();
                                        chessUI.chessMenuBar.chess.fire();
                                    });
                                    try
                                    {
                                        Transfer tempTransfer=new Transfer();
                                        tempTransfer.user_ID=chessUI.user_ID;
                                        tempTransfer.opponent_ID=invitation.getFrom();
                                        chessUI.client.execute("/Chess_Server/Remove_Accepted_Invitation",tempTransfer);
                                    }catch (Throwable t)
                                    {
                                        System.out.println("Member List : "+t);
                                    }
                                }
                            }
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
        timerForAcceptedInvitation.start();
    }

    private void startTimerForRejectedInvitation()
    {
        this.timerForRejectedInvitation=new javax.swing.Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    List<Invitation> rejectedInvitation=new LinkedList<>();
                    List<Object> objects = (List<Object>) chessUI.client.execute("/Chess_Server/Get_Rejected_Invitations",transfer);

                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Invitation invitation = gson.fromJson(json, Invitation.class);
                            rejectedInvitation.add(invitation);
                        }
                    }
                    if(rejectedInvitation.size()!=0)
                    {
                        // Shut down the timerForAcceptedChallenge when acceptedChallenges are available
                        for(Invitation invitation:rejectedInvitation)
                        {
                            if(invitation.getTo()==chessUI.user_ID && invitation.getType()== INVITATION_TYPE.INVITATION_REJECTED)
                            {
                                opponent_user_ID=0;
                                Platform.runLater(() ->
                                {
                                    chessUI.chessPage.chessPane.getChildren().clear();
                                    chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                                    chessUI.chessMenuBar.home.setDisable(false);
                                    chessUI.chessMenuBar.social.setDisable(false);
                                    chessUI.chessMenuBar.profile.setDisable(false);
                                    chessUI.chessMenuBar.home.fire();
                                });
                                try
                                {
                                    Transfer tempTransfer=new Transfer();
                                    tempTransfer.user_ID=chessUI.user_ID;
                                    tempTransfer.opponent_ID=invitation.getFrom();
                                    chessUI.client.execute("/Chess_Server/Remove_Rejected_Invitation",tempTransfer);
                                }catch (Throwable t)
                                {
                                    System.out.println("Member List : "+t);
                                }
                            }
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
        timerForRejectedInvitation.start();
    }

    public void makeTopPaneNull()
    {
        main.setVisible(false);
    }
    public void makeTopPaneVisible()
    {
        main.setVisible(true);
    }
    private void setPopupPosition(Stage mainStage)
    {
//        Scene mainScene = mainStage.getScene();
//        double mainStageX = mainStage.getX();
//        double mainStageY = mainStage.getY();
//        double mainSceneX = mainScene.getX();
//        double mainSceneY = mainScene.getY();
//
//        double popupX = mainStageX + mainSceneX + request.getLayoutX() + request.getScene().getX();
//        double popupY = mainStageY + mainSceneY + request.getLayoutY() + request.getScene().getY() + request.getHeight();
//
//        requestPopUp.setX(popupX);
//        requestPopUp.setY(popupY);

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

    public List<Integer> getPendingRequestsUsingID(int userID)
    {
        List<Integer> pendingRequests=new LinkedList<>();
        try
        {
            Transfer transfer=new Transfer();
            transfer.user_ID=userID;
            List<Object> objects=(List<Object>)chessUI.client.execute("/Chess_Server/Get_Pending_Requests",transfer);
            if (objects != null)
            {
                for (Object object : objects)
                {
                    Gson gson = new Gson();
                    String json = gson.toJson(object);
                    Integer pendingRequest = gson.fromJson(json, Integer.class);
                    pendingRequests.add(pendingRequest);
                }
            }
        } catch (Throwable e)
        {
            System.out.println("Failed to load member details : "+e.getMessage());
        }
        return pendingRequests;
    }

    private void removeInvitations(int opponent_ID)
    {
        for (Node node : invitationTable.getChildren())
        {
            AnchorPane invitationPane = (AnchorPane) node;
            int storedUser_ID = (int) invitationPane.getProperties().get("opponent_ID");
            if (storedUser_ID==opponent_ID)
            {
                invitationTable.getChildren().remove(invitationPane);
                if (invitationTable.getChildren().size()==0)
                {
                    invitation.setGraphic(darkInvitationImageView);
                    invitationScrollPane.setContent(noInvitationPane);
                }
                break;
            }
        }
    }
}
