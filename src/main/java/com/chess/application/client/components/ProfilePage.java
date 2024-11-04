package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import com.chess.common.Friend;
import com.chess.common.Member;
import com.chess.common.Transfer;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ProfilePage extends AnchorPane
{
    private AnchorPane profilePane,usernamePane,namePane,emailPane,passwordPane;
    private Image homeImage,defaultProfile;
    private ImageView homeImageView,defaultProfileImageView;
    private Label elo,gamePlayed,usernameLabel,username,nameLabel,name,emailLabel,email,passwordLabel,password;
    private Button editUsername,editName,editEmail,editPassword,logout;
    private TextField updateUsername,updateName,updateEmail,updatePassword;
    private VBox usernameBox,nameBox;
    private ChessUI chessUI;
    private Member member;
    public Timer timerForProfile;
    public ProfilePage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }
    
    private void initComponents()
    {
        getProfileDetails();
        makeProfilePane();
        startTimerForProfile();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b;");
    }

    public void addListeners()
    {
        editUsername.setOnAction( actionEvent ->
        {
            if (editUsername.getText().contains("Edit"))
            {

                updateUsername=new TextField();
                Font updateUsernameFont = Font.font("Sage UI",20);
                updateUsername.setFont(updateUsernameFont);
                updateUsername.setText(username.getText().trim());
                updateUsername.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
                AnchorPane.setTopAnchor(updateUsername,5.0);
                AnchorPane.setBottomAnchor(updateUsername,5.0);
                AnchorPane.setLeftAnchor(updateUsername,170.0);
                AnchorPane.setRightAnchor(updateUsername,170.0);

                usernamePane.getChildren().clear();
                usernamePane.getChildren().addAll(usernameLabel,updateUsername,editUsername);
                editUsername.setText("Save");
            }
            else
            {
                if (editUsername.getText().contains("Save"))
                {
                    if (updateUsername.getText().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Please Enter Username");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(565);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }
                    if (updateUsername.getText().contains(" "))
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Username can not be contain Space");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(550);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }
                    if (!updateUsername.getText().equals(username.getText()))
                    {
                        Transfer transfer=new Transfer();
                        transfer.username=updateUsername.getText();
                        try
                        {
                            boolean isMemberExixts = (boolean)chessUI.client.execute("/Chess_Server/Is_Member_Exixts",transfer);
                            if(isMemberExixts)
                            {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("");
                                alert.setContentText("Username not Available");
                                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                                alertStage.setX(550);
                                alertStage.setY(350);
                                alert.showAndWait();
                                return;
                            }
                        }
                        catch (Throwable e)
                        {
                            System.out.println("Profile Pane : "+e.getMessage());
                        }
                    }

                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.username=updateUsername.getText();

                    try
                    {
                        chessUI.client.execute("/Chess_Server/Update_Username",transfer);
                    }catch (Throwable e)
                    {
                        System.out.println("Profile Pane : "+e.getMessage());
                    }

                    chessUI.username=updateUsername.getText();
                    try
                    {
                        chessUI.signInPage.preferences.put("username", updateUsername.getText());
                    } catch (Throwable e)
                    {

                    }

                    username=new Label(chessUI.username);
                    Font usernameFont = Font.font("Sage UI",25);
                    username.setFont(usernameFont);
                    username.setTextFill(Color.WHITE);
                    AnchorPane.setTopAnchor(username,0.0);
                    AnchorPane.setBottomAnchor(username,0.0);
                    AnchorPane.setLeftAnchor(username,170.0);

                    usernamePane.getChildren().clear();
                    usernamePane.getChildren().addAll(usernameLabel,username,editUsername);

                    editUsername.setText("Edit");
                }
            }
            
        });

        editName.setOnAction( actionEvent ->
        {
            if (editName.getText().contains("Edit"))
            {
                updateName=new TextField();
                Font updateNameFont = Font.font("Sage UI",20);
                updateName.setFont(updateNameFont);
                updateName.setText(name.getText().trim());
                updateName.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
                AnchorPane.setTopAnchor(updateName,5.0);
                AnchorPane.setBottomAnchor(updateName,5.0);
                AnchorPane.setLeftAnchor(updateName,170.0);
                AnchorPane.setRightAnchor(updateName,170.0);

                namePane.getChildren().clear();
                namePane.getChildren().addAll(nameLabel,updateName,editName);
                editName.setText("Save");
            }
            else
            {
                if (editName.getText().contains("Save"))
                {
                    if (updateName.getText().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Please Enter Name");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(565);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }

                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.name=updateName.getText();

                    try
                    {
                        chessUI.client.execute("/Chess_Server/Update_Name",transfer);
                    }catch (Throwable e)
                    {
                        System.out.println("Profile Pane : "+e.getMessage());
                    }

                    name=new Label(updateName.getText());
                    Font NameFont = Font.font("Sage UI",25);
                    name.setFont(NameFont);
                    name.setTextFill(Color.WHITE);
                    AnchorPane.setTopAnchor(name,0.0);
                    AnchorPane.setBottomAnchor(name,0.0);
                    AnchorPane.setLeftAnchor(name,170.0);

                    namePane.getChildren().clear();
                    namePane.getChildren().addAll(nameLabel,name,editName);

                    editName.setText("Edit");
                }
            }

        });

        editEmail.setOnAction( actionEvent ->
        {
            if (editEmail.getText().contains("Edit"))
            {
                updateEmail=new TextField();
                Font updateEmailFont = Font.font("Sage UI",20);
                updateEmail.setFont(updateEmailFont);
                updateEmail.setText(email.getText().trim());
                updateEmail.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
                AnchorPane.setTopAnchor(updateEmail,5.0);
                AnchorPane.setBottomAnchor(updateEmail,5.0);
                AnchorPane.setLeftAnchor(updateEmail,170.0);
                AnchorPane.setRightAnchor(updateEmail,170.0);

                emailPane.getChildren().clear();
                emailPane.getChildren().addAll(emailLabel,updateEmail,editEmail);
                editEmail.setText("Save");
            }
            else
            {
                if (editEmail.getText().contains("Save"))
                {
                    if (updateEmail.getText().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Please Enter Email");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(565);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }
                    if (updateEmail.getText().contains(" "))
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Email can not be contain Space");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(550);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }
                    if (!updateEmail.getText().contains("@"))
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Invalid Email");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(550);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }

                    Transfer transfer=new Transfer();
                    transfer.email=updateEmail.getText();
                    try
                    {
                        boolean isEmailExixts = (boolean)chessUI.client.execute("/Chess_Server/Is_Email_Exixts",transfer);
                        if (isEmailExixts)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("");
                            alert.setContentText("Email is already used !!");
                            alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                            alertStage.setX(550);
                            alertStage.setY(350);
                            alert.showAndWait();
                            return;
                        }
                    }
                    catch (Throwable e)
                    {
                            System.out.println("Profile Pane : "+e.getMessage());
                    }

                    transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.email=updateEmail.getText();

                    try
                    {
                        chessUI.client.execute("/Chess_Server/Update_Email",transfer);
                    }catch (Throwable e)
                    {
                        System.out.println("Profile Pane : "+e.getMessage());
                    }

                    email=new Label(updateEmail.getText());
                    Font EmailFont = Font.font("Sage UI",25);
                    email.setFont(EmailFont);
                    email.setTextFill(Color.WHITE);
                    AnchorPane.setTopAnchor(email,0.0);
                    AnchorPane.setBottomAnchor(email,0.0);
                    AnchorPane.setLeftAnchor(email,170.0);

                    emailPane.getChildren().clear();
                    emailPane.getChildren().addAll(emailLabel,email,editEmail);

                    editEmail.setText("Edit");
                }
            }

        });

        editPassword.setOnAction( actionEvent ->
        {
            if (editPassword.getText().contains("Edit"))
            {
                updatePassword=new TextField();
                Font updatePasswordFont = Font.font("Sage UI",20);
                updatePassword.setFont(updatePasswordFont);
                updatePassword.setText("");
                updatePassword.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
                AnchorPane.setTopAnchor(updatePassword,5.0);
                AnchorPane.setBottomAnchor(updatePassword,5.0);
                AnchorPane.setLeftAnchor(updatePassword,170.0);
                AnchorPane.setRightAnchor(updatePassword,170.0);

                passwordPane.getChildren().clear();
                passwordPane.getChildren().addAll(passwordLabel,updatePassword,editPassword);
                editPassword.setText("Save");
            }
            else
            {
                if (editPassword.getText().contains("Save"))
                {
                    if (updatePassword.getText().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Please Enter Password");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(565);
                        alertStage.setY(350);
                        alert.showAndWait();
                        return;
                    }

                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    transfer.password=updatePassword.getText();

                    try
                    {
                        chessUI.client.execute("/Chess_Server/Update_Password",transfer);
                    }catch (Throwable e)
                    {
                        System.out.println("Profile Pane : "+e.getMessage());
                    }

                    try
                    {
                        chessUI.signInPage.preferences.put("password", updatePassword.getText());
                    } catch (Throwable e)
                    {

                    }

                    password=new Label("**********");
                    Font PasswordFont = Font.font("Sage UI",25);
                    password.setFont(PasswordFont);
                    password.setTextFill(Color.WHITE);
                    AnchorPane.setTopAnchor(password,0.0);
                    AnchorPane.setBottomAnchor(password,0.0);
                    AnchorPane.setLeftAnchor(password,170.0);

                    passwordPane.getChildren().clear();
                    passwordPane.getChildren().addAll(passwordLabel,password,editPassword);

                    editPassword.setText("Edit");
                }
            }

        });

        logout.setOnAction( actionEvent ->
        {
            chessUI.signInPage=new SignInPage(chessUI);
            chessUI.root.setCenter(chessUI.signInPage);
            chessUI.root.setTop(null);
            chessUI.root.setLeft(null);
            chessUI.root.setBottom(null);

            chessUI.chessTopBar=null;
            chessUI.chessMenuBar=null;

            chessUI.homePage=null;
            chessUI.chessPage=null;
            chessUI.socialPage=null;
            chessUI.profilePage=null;

            try
            {
                Preferences preferences = Preferences.userNodeForPackage(SignInPage.class);
                preferences.clear();
            } catch (Exception e)
            {

            }
        });
    }

    private void makeProfilePane()
    {
        profilePane=new AnchorPane();
        profilePane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(profilePane, 40.0);
        AnchorPane.setBottomAnchor(profilePane, 60.0);
        AnchorPane.setLeftAnchor(profilePane, 350.0);
        AnchorPane.setRightAnchor(profilePane, 840.0);

        homeImage = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/HomePage/main page 1.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(750);
        homeImageView.setFitWidth(1470);
        homeImageView.setOpacity(0.6);
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5); // Adjust the blur width as needed
        blur.setHeight(5); // Adjust the blur height as needed
        homeImageView.setEffect(blur);
        this.getChildren().add(homeImageView);

        if (member.getProfile()!=null)
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(member.getProfile());
            // Create an Image object from the InputStream
            defaultProfile = new Image(inputStream);
        }
        else
        {
            defaultProfile = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
        }

        defaultProfileImageView = new ImageView(defaultProfile);
        defaultProfileImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
        defaultProfileImageView.setFitHeight(170);
        defaultProfileImageView.setFitWidth(170);
        defaultProfileImageView.setLayoutX(280.0);
        defaultProfileImageView.setLayoutY(20);
        defaultProfileImageView.setSmooth(true);

        elo=new Label("Elo : "+member.getElo());
        Font eloFont = Font.font("Sage UI",25);
        elo.setFont(eloFont);
        elo.setTextFill(Color.WHITE);
        elo.setLayoutX(315);
        elo.setLayoutY(200);

        gamePlayed=new Label("Game Played : "+member.getGamePlayed());
        Font gamePlayedFont = Font.font("Sage UI",25);
        gamePlayed.setFont(gamePlayedFont);
        gamePlayed.setTextFill(Color.WHITE);
        gamePlayed.setLayoutX(275);
        gamePlayed.setLayoutY(250);

        usernameLabel=new Label("Username");
        Font usernameLabelFont = Font.font("Sage UI",25);
        usernameLabel.setFont(usernameLabelFont);
        usernameLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(usernameLabel,0.0);
        AnchorPane.setBottomAnchor(usernameLabel,0.0);
        AnchorPane.setLeftAnchor(usernameLabel,5.0);

        username=new Label(member.getUsername());
        Font usernameFont = Font.font("Sage UI",25);
        username.setFont(usernameFont);
        username.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(username,0.0);
        AnchorPane.setBottomAnchor(username,0.0);
        AnchorPane.setLeftAnchor(username,170.0);

        editUsername=new Button("Edit");
        Font editUsernameFont = Font.font("Sage UI",20);
        editUsername.setFont(editUsernameFont);
        editUsername.setStyle("-fx-background-color: lightblue; -fx-image-rendering: optimizeQuality; fx-border-radius: 40px; -fx-background-radius: 15px;");
        editUsername.setTextFill(Color.WHITE);
        editUsername.setPrefWidth(110);
        AnchorPane.setTopAnchor(editUsername,5.0);
        AnchorPane.setBottomAnchor(editUsername,6.0);
        AnchorPane.setLeftAnchor(editUsername,530.0);

        usernamePane=new AnchorPane();
        usernamePane.setStyle("-fx-background-color: #322e2b; fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
        usernamePane.setPrefWidth(644);
        usernamePane.setPrefHeight(50);
        usernamePane.setLayoutX(43);
        usernamePane.setLayoutY(310);
        usernamePane.getChildren().addAll(usernameLabel,username,editUsername);

        nameLabel=new Label("Name");
        Font nameLabelFont = Font.font("Sage UI",25);
        nameLabel.setFont(nameLabelFont);
        nameLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(nameLabel,0.0);
        AnchorPane.setBottomAnchor(nameLabel,0.0);
        AnchorPane.setLeftAnchor(nameLabel,5.0);

        name=new Label(member.getName());
        Font nameFont = Font.font("Sage UI",25);
        name.setFont(nameFont);
        name.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(name,0.0);
        AnchorPane.setBottomAnchor(name,0.0);
        AnchorPane.setLeftAnchor(name,170.0);

        editName=new Button("Edit");
        Font editNameFont = Font.font("Sage UI",20);
        editName.setFont(editNameFont);
        editName.setStyle("-fx-background-color: lightblue; -fx-image-rendering: optimizeQuality; fx-border-radius: 40px; -fx-background-radius: 15px;");
        editName.setTextFill(Color.WHITE);
        editName.setPrefWidth(110);
        AnchorPane.setTopAnchor(editName,5.0);
        AnchorPane.setBottomAnchor(editName,6.0);
        AnchorPane.setLeftAnchor(editName,530.0);

        namePane=new AnchorPane();
        namePane.setStyle("-fx-background-color: #322e2b; fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
        namePane.setPrefWidth(644);
        namePane.setPrefHeight(50);
        namePane.setLayoutX(43);
        namePane.setLayoutY(380);
        namePane.getChildren().addAll(nameLabel,name,editName);

        emailLabel=new Label("Email");
        Font emailLabelFont = Font.font("Sage UI",25);
        emailLabel.setFont(emailLabelFont);
        emailLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(emailLabel,0.0);
        AnchorPane.setBottomAnchor(emailLabel,0.0);
        AnchorPane.setLeftAnchor(emailLabel,5.0);

        email=new Label(member.getEmail());
        Font emailFont = Font.font("Sage UI",25);
        email.setFont(emailFont);
        email.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(email,0.0);
        AnchorPane.setBottomAnchor(email,0.0);
        AnchorPane.setLeftAnchor(email,170.0);

        editEmail=new Button("Edit");
        Font editEmailFont = Font.font("Sage UI",20);
        editEmail.setFont(editEmailFont);
        editEmail.setStyle("-fx-background-color: lightblue; -fx-image-rendering: optimizeQuality; fx-border-radius: 40px; -fx-background-radius: 15px;");
        editEmail.setTextFill(Color.WHITE);
        editEmail.setPrefWidth(110);
        AnchorPane.setTopAnchor(editEmail,5.0);
        AnchorPane.setBottomAnchor(editEmail,6.0);
        AnchorPane.setLeftAnchor(editEmail,530.0);

        emailPane=new AnchorPane();
        emailPane.setStyle("-fx-background-color: #322e2b; fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
        emailPane.setPrefWidth(644);
        emailPane.setPrefHeight(50);
        emailPane.setLayoutX(43);
        emailPane.setLayoutY(450);
        emailPane.getChildren().addAll(emailLabel,email,editEmail);

        passwordLabel=new Label("Password");
        Font passwordLabelFont = Font.font("Sage UI",25);
        passwordLabel.setFont(passwordLabelFont);
        passwordLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(passwordLabel,0.0);
        AnchorPane.setBottomAnchor(passwordLabel,0.0);
        AnchorPane.setLeftAnchor(passwordLabel,5.0);

        password=new Label("**********");
        Font passwordFont = Font.font("Sage UI",25);
        password.setFont(passwordFont);
        password.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(password,0.0);
        AnchorPane.setBottomAnchor(password,0.0);
        AnchorPane.setLeftAnchor(password,170.0);

        editPassword=new Button("Edit");
        Font editPasswordFont = Font.font("Sage UI",20);
        editPassword.setFont(editPasswordFont);
        editPassword.setStyle("-fx-background-color: lightblue; -fx-image-rendering: optimizeQuality; fx-border-radius: 40px; -fx-background-radius: 15px;");
        editPassword.setTextFill(Color.WHITE);
        editPassword.setPrefWidth(110);
        AnchorPane.setTopAnchor(editPassword,5.0);
        AnchorPane.setBottomAnchor(editPassword,6.0);
        AnchorPane.setLeftAnchor(editPassword,530.0);

        passwordPane=new AnchorPane();
        passwordPane.setStyle("-fx-background-color: #322e2b; fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 10px, 0.5, 0, 0);");
        passwordPane.setPrefWidth(644);
        passwordPane.setPrefHeight(50);
        passwordPane.setLayoutX(43);
        passwordPane.setLayoutY(520);
        passwordPane.getChildren().addAll(passwordLabel,password,editPassword);

        logout=new Button("Logout");
        Font logoutFont = Font.font("Sage UI",20);
        logout.setFont(logoutFont);
        logout.setStyle("-fx-background-color: lightblue; -fx-image-rendering: optimizeQuality; fx-border-radius: 40px; -fx-background-radius: 15px;");
        logout.setTextFill(Color.WHITE);
        logout.setLayoutY(591);
        logout.setLayoutX(320);

        profilePane.getChildren().addAll(elo,gamePlayed,defaultProfileImageView,usernamePane,namePane,emailPane,passwordPane,logout);
        this.getChildren().add(profilePane);
    }

    private void startTimerForProfile()
    {
        this.timerForProfile=new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;
                    Object object = (Object)chessUI.client.execute("/Chess_Server/Get_Member_Details_Using_ID",transfer);
                    if (object != null)
                    {
                        Gson gson = new Gson();
                        String json = gson.toJson(object);
                        member = gson.fromJson(json, Member.class);
                    }
                } catch (Throwable e)
                {
                    System.out.println("Failed to load member details : "+e.getMessage());
                }
                Platform.runLater(() ->
                {
                    elo.setText("Elo : "+member.getElo());
                    gamePlayed.setText("Game Played : "+member.getGamePlayed());
                    username.setText(member.getUsername());
                    name.setText(member.getName());
                    email.setText(member.getEmail());
                    password.setText("**********");
                    if (member.getProfile()!=null)
                    {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(member.getProfile());
                        // Create an Image object from the InputStream
                        defaultProfile = new Image(inputStream);
                    }
                    else
                    {
                        defaultProfile = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/Default/Profile Photo/default profile.png").toExternalForm());
                    }

                });
            }
        });
        timerForProfile.start();
    }

    private void getProfileDetails()
    {
        try
        {
            Transfer transfer=new Transfer();
            transfer.user_ID=chessUI.user_ID;
            Object object = (Object)chessUI.client.execute("/Chess_Server/Get_Member_Details_Using_ID",transfer);
            if (object != null)
            {
                Gson gson = new Gson();
                String json = gson.toJson(object);
                member = gson.fromJson(json, Member.class);
            }
        } catch (Throwable e)
        {
            System.out.println("Failed to load member details : "+e.getMessage());
        }
    }


}
