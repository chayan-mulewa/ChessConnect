package com.chess.application.client.components;

import com.chess.application.client.example.LoadingAnimationApp;
import com.chess.application.client.ui.ChessUI;
import com.chess.common.Transfer;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.prefs.Preferences;

public class SignUpPage extends AnchorPane
{
    private AnchorPane signUpPage,designer;
    private Label signUpLabel,designerLabel,alreadyHaveAnAccountLabel;
    private Image homeImage,showImage,hideImage;
    public byte[] profilePhotoData;
    private ImageView homeImageView,showImageView,hideImageView;
    private TextField name,username,email,passwordText;
    private PasswordField password,reEnterPassword;
    private Button showPasswordButton;
    private Button signUp,signInNow,uploadProfile;
    private ChessUI chessUI;
    public Preferences preferences;

    public SignUpPage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        preferences = Preferences.userNodeForPackage(SignInPage.class);
        makesignUpPage();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b;");
    }

    public void addListeners()
    {
        signUpPage.setOnMouseClicked((MouseEvent event) -> {
            // Request focus on the dummyLabel to make the PasswordField lose focus
            homeImageView.requestFocus();
        });

        this.setOnMouseClicked((MouseEvent event) -> {
            // Request focus on the dummyLabel to make the PasswordField lose focus
            homeImageView.requestFocus();
        });

        signUp.setOnAction(actionEvent ->
        {
            Image gifImage = new Image(LoadingAnimationApp.class.getResource("/com/chess/application/client/components/Default/Loading/loading 3.gif").toExternalForm());
            ImageView imageView = new ImageView(gifImage);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            signUp.setDisable(true);
            signUp.setText(null);
            signUp.setGraphic(imageView);

            if (name.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Enter Full Name");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (email.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Enter Email");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (!email.getText().contains("@"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Invalid Email");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (username.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Enter Username");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (username.getText().contains(" "))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Username can not be contain Space");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (password.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Enter Password");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (reEnterPassword.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Renter Enter Password");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }
            if (!password.getText().equals(reEnterPassword.getText()))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setContentText("Please Check Both Password");
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.setX(550);
                alertStage.setY(350);
                alert.showAndWait();
                signUp.setDisable(false);
                signUp.setText("Sign Up");
                signUp.setGraphic(null);
                return;
            }

            new Thread(() ->
            {
                Platform.runLater(()->
                {
                    Transfer transfer=new Transfer();
                    transfer.username=username.getText();
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
                            signUp.setDisable(false);
                            signUp.setText("Sign Up");
                            signUp.setGraphic(null);
                            return;
                        }
                        transfer=new Transfer();
                        transfer.email=email.getText();
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
                            signUp.setDisable(false);
                            signUp.setText("Sign Up");
                            signUp.setGraphic(null);
                            return;
                        }
                        transfer=new Transfer();
                        transfer.name=name.getText();
                        transfer.email=email.getText();
                        transfer.user=username.getText();
                        transfer.pass=password.getText();
                        transfer.profilePhotoData=profilePhotoData;

                        chessUI.client.execute("/Chess_Server/Create_Member",transfer);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("");
                        alert.setContentText("Congratulations Account Successfully Created  : )");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(550);
                        alertStage.setY(350);
                        ButtonType okButton;
                        alert.getButtonTypes().setAll(okButton=new ButtonType("Ok"));
                        java.util.Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == okButton)
                        {
                            chessUI.root.setCenter(chessUI.signInPage);
                        }
                        else
                        {
                            chessUI.root.setCenter(chessUI.signInPage);
                        }
                        signUp.setDisable(false);
                        signUp.setText("Sign Up");
                        signUp.setGraphic(null);

                    } catch (Throwable e)
                    {
                        throw new RuntimeException(e);
                    }
                    chessUI.signUpPage=new SignUpPage(chessUI);
                });
            }).start();
        });

        showPasswordButton.setOnAction(event ->
        {
            ImageView imageView=(ImageView)showPasswordButton.getGraphic();
            if (imageView.getImage().getUrl().contains("hide"))
            {
                passwordText=new TextField();
                passwordText.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
                Font passwordFont = Font.font("Sage UI",18);
                passwordText.setFont(passwordFont);
                passwordText.setPromptText("Repeat Password");
                passwordText.setText(reEnterPassword.getText());
                passwordText.setFocusTraversable(false);
                AnchorPane.setTopAnchor(passwordText, 310.0);
                AnchorPane.setLeftAnchor(passwordText, 100.0);
                AnchorPane.setRightAnchor(passwordText, 100.0);

                signUpPage.getChildren().remove(reEnterPassword);
                signUpPage.getChildren().add(passwordText);
                signUpPage.getChildren().remove(showPasswordButton);
                signUpPage.getChildren().add(showPasswordButton);
                showPasswordButton.setGraphic(showImageView);
            }
            else
            {
                if (imageView.getImage().getUrl().contains("show"))
                {
                    reEnterPassword=new PasswordField();
                    reEnterPassword.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
                    Font passwordFont = Font.font("Sage UI",18);
                    reEnterPassword.setFont(passwordFont);
                    reEnterPassword.setPromptText("Repeat Password");
                    reEnterPassword.setText(passwordText.getText());
                    reEnterPassword.setFocusTraversable(false);
                    AnchorPane.setTopAnchor(reEnterPassword, 310.0);
                    AnchorPane.setLeftAnchor(reEnterPassword, 100.0);
                    AnchorPane.setRightAnchor(reEnterPassword, 100.0);

                    signUpPage.getChildren().remove(passwordText);
                    signUpPage.getChildren().add(reEnterPassword);
                    signUpPage.getChildren().remove(showPasswordButton);
                    signUpPage.getChildren().add(showPasswordButton);
                    showPasswordButton.setGraphic(hideImageView);
                }
            }
        });
        signInNow.setOnAction( actionEvent ->
        {
            chessUI.signUpPage=new SignUpPage(chessUI);
            chessUI.signInPage=new SignInPage(chessUI);
            chessUI.root.getChildren().clear();
            chessUI.root.setCenter(chessUI.signInPage);
        });

        uploadProfile.setOnAction(e ->
        {
            profilePhotoData=new byte[0];

            FileChooser fileChooser = new FileChooser();

            // Determine the download folder based on the operating system
            String userHome = System.getProperty("user.home");
            String osName = System.getProperty("os.name").toLowerCase();

            File initialDirectory;

            if (osName.contains("win"))
            { // Windows
                initialDirectory = new File(userHome + "\\Downloads");
            }
            else if (osName.contains("mac"))
            { // macOS
                initialDirectory = new File(userHome + "/Downloads");
            }
            else
            { // Assume a default location for other operating systems
                initialDirectory = new File(userHome + "/Downloads");
            }

            fileChooser.setInitialDirectory(initialDirectory);

            // Configure the FileChooser to filter only JPG files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show the file chooser dialog
            File selectedFile = fileChooser.showOpenDialog(chessUI.primaryStage);

            if (selectedFile != null)
            {
                try
                {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    profilePhotoData = fis.readAllBytes();
                    fis.close();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void makesignUpPage()
    {
        homeImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/HomePage/main page 3.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(800);
        homeImageView.setFitWidth(1540);
        homeImageView.setOpacity(0.8);
        this.getChildren().add(homeImageView);

        signUpPage=new AnchorPane();
        signUpPage.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(signUpPage, 125.0);
        AnchorPane.setBottomAnchor(signUpPage, 125.0);
        AnchorPane.setLeftAnchor(signUpPage, 500.0);
        AnchorPane.setRightAnchor(signUpPage, 500.0);

        designer=new AnchorPane();
        designer.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(designer, 702.0);
        AnchorPane.setBottomAnchor(designer, 15.0);
        AnchorPane.setLeftAnchor(designer, 10.0);
        AnchorPane.setRightAnchor(designer, 1185.0);

        designerLabel=new Label("Develop By Chayan Mulewa");
        Font designerLabelFont = Font.font("Sage UI",25);
        designerLabel.setFont(designerLabelFont);
        designerLabel.setTextFill(Color.WHITE);
        designerLabel.setLayoutX(18);
        designerLabel.setLayoutY(22);

        designer.getChildren().add(designerLabel);

        signUpLabel=new Label("Sign Up");
        Font signUpLabelFont = Font.font("Sage UI",30);
        signUpLabel.setFont(signUpLabelFont);
        signUpLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(signUpLabel, 10.0);
        AnchorPane.setLeftAnchor(signUpLabel, 225.0);

        name=new TextField();
        name.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font nameFont = Font.font("Sage UI",18);
        name.setFont(nameFont);
        name.setPromptText("Full Name");
        name.setFocusTraversable(false);
        AnchorPane.setTopAnchor(name, 70.0);
        AnchorPane.setLeftAnchor(name, 100.0);
        AnchorPane.setRightAnchor(name, 100.0);

        email=new TextField();
        email.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font emailFont = Font.font("Sage UI",18);
        email.setFont(emailFont);
        email.setPromptText("Email");
        email.setFocusTraversable(false);
        AnchorPane.setTopAnchor(email, 130.0);
        AnchorPane.setLeftAnchor(email, 100.0);
        AnchorPane.setRightAnchor(email, 100.0);

        username=new TextField();
        username.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font usernameFont = Font.font("Sage UI",18);
        username.setFont(usernameFont);
        username.setPromptText("Username");
        username.setFocusTraversable(false);
        AnchorPane.setTopAnchor(username, 190.0);
        AnchorPane.setLeftAnchor(username, 100.0);
        AnchorPane.setRightAnchor(username, 100.0);

        password=new PasswordField();
        password.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font passwordFont = Font.font("Sage UI",18);
        password.setFont(passwordFont);
        password.setPromptText("Password");
        password.setFocusTraversable(false);
        AnchorPane.setTopAnchor(password, 250.0);
        AnchorPane.setLeftAnchor(password, 100.0);
        AnchorPane.setRightAnchor(password, 100.0);

        reEnterPassword=new PasswordField();
        reEnterPassword.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font reEnterPasswordFont = Font.font("Sage UI",18);
        reEnterPassword.setFont(reEnterPasswordFont);
        reEnterPassword.setPromptText("Repeat Password");
        reEnterPassword.setFocusTraversable(false);
        AnchorPane.setTopAnchor(reEnterPassword, 310.0);
        AnchorPane.setLeftAnchor(reEnterPassword, 100.0);
        AnchorPane.setRightAnchor(reEnterPassword, 100.0);

        passwordText = new TextField(password.getText());
        passwordText.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        passwordText.setFont(password.getFont());
        AnchorPane.setTopAnchor(passwordText, 310.0);
        AnchorPane.setLeftAnchor(passwordText, 100.0);
        AnchorPane.setRightAnchor(passwordText, 100.0);

        showImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/SignInPage/show.png").toExternalForm());
        showImageView=new ImageView(showImage);
        showImageView.setSmooth(true);
        showImageView.setFitHeight(25);
        showImageView.setFitWidth(25);

        hideImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/SignInPage/hide.png").toExternalForm());
        hideImageView=new ImageView(hideImage);
        hideImageView.setSmooth(true);
        hideImageView.setFitHeight(25);
        hideImageView.setFitWidth(25);

        showPasswordButton = new Button();
        showPasswordButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        showPasswordButton.setGraphic(hideImageView);
        showPasswordButton.setFocusTraversable(false);
        AnchorPane.setTopAnchor(showPasswordButton, 314.0);
        AnchorPane.setLeftAnchor(showPasswordButton, 400.0);

        signUp=new Button("Sign Up");
        signUp.setStyle("-fx-cursor: hand; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font loginFont = Font.font("Sage UI",18);
        signUp.setFont(loginFont);
        signUp.setFocusTraversable(false);
        AnchorPane.setTopAnchor(signUp, 430.0);
        AnchorPane.setLeftAnchor(signUp, 100.0);
        AnchorPane.setRightAnchor(signUp, 100.0);

        profilePhotoData=new byte[0];
        uploadProfile=new Button("Upload Porfile (Optional)");
        uploadProfile.setStyle("-fx-cursor: hand; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font uploadProfileFont = Font.font("Sage UI",18);
        uploadProfile.setFont(uploadProfileFont);
        uploadProfile.setFocusTraversable(false);
        AnchorPane.setTopAnchor(uploadProfile, 370.0);
        AnchorPane.setLeftAnchor(uploadProfile, 100.0);
        AnchorPane.setRightAnchor(uploadProfile, 100.0);

        
        alreadyHaveAnAccountLabel=new Label("Already Have An Account  : )");
        alreadyHaveAnAccountLabel.setTextFill(Color.WHITE);
        alreadyHaveAnAccountLabel.setFocusTraversable(false);
        AnchorPane.setTopAnchor(alreadyHaveAnAccountLabel, 490.0);
        AnchorPane.setLeftAnchor(alreadyHaveAnAccountLabel, 160.0);

        signInNow=new Button("Sign In Now!");
        signInNow.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        signInNow.setTextFill(Color.SKYBLUE);
        signInNow.setFocusTraversable(false);
        AnchorPane.setTopAnchor(signInNow, 486.0);
        AnchorPane.setLeftAnchor(signInNow, 310.0);


        signUpPage.getChildren().addAll(signUpLabel,name,email,username,password,reEnterPassword,showPasswordButton,signUp,uploadProfile,alreadyHaveAnAccountLabel,signInNow);

        this.getChildren().addAll(signUpPage,designer);
    }
}
