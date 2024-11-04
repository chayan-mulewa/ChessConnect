package com.chess.application.client.components;

import com.chess.application.client.example.LoadingAnimationApp;
import com.chess.application.client.ui.ChessUI;
import com.chess.common.Transfer;
import com.chess.framework.client.NFrameworkClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.prefs.Preferences;

public class SignInPage extends AnchorPane
{
    private AnchorPane loginPane,designer;
    private Label signInLabel,designerLabel,differentSignUp,dontHaveAnAccountLabel;
    private Image homeImage,showImage,hideImage;
    private ImageView homeImageView,showImageView,hideImageView;
    private TextField username,passwordText;
    private PasswordField password;
    private Button showPasswordButton;
    private CheckBox rememberMe;
    private Button signIn,forgetPassword,googleSignUp,facebookSignUp,twitterSignUp,signUpNow;
    private ChessUI chessUI;
    public Preferences preferences;

    public SignInPage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        preferences = Preferences.userNodeForPackage(SignInPage.class);
        makeLoginPane();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b;");
    }

    public void addListeners()
    {
        loginPane.setOnMouseClicked((MouseEvent event) -> {
            // Request focus on the dummyLabel to make the PasswordField lose focus
            homeImageView.requestFocus();
        });

        this.setOnMouseClicked((MouseEvent event) -> {
            // Request focus on the dummyLabel to make the PasswordField lose focus
            homeImageView.requestFocus();
        });

        signIn.setOnAction(actionEvent ->
        {
            Image gifImage = new Image(LoadingAnimationApp.class.getResource("/com/chess/application/client/components/Default/Loading/loading 3.gif").toExternalForm());
            ImageView imageView = new ImageView(gifImage);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            signIn.setDisable(true);
            signIn.setText(null);
            signIn.setGraphic(imageView);

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
                signIn.setDisable(false);
                signIn.setText("Sign In");
                signIn.setGraphic(null);
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
                signIn.setDisable(false);
                signIn.setText("Sign In");
                signIn.setGraphic(null);
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
                signIn.setDisable(false);
                signIn.setText("Sign In");
                signIn.setGraphic(null);
                return;
            }

            new Thread(() ->
            {

            Transfer transfer=new Transfer();
            transfer.username=username.getText();
            transfer.password=password.getText();
            try
            {
                boolean authentic = (boolean)chessUI.client.execute("/Chess_Server/Authenticate_Member",transfer);
                if(authentic)
                {
                    Platform.runLater(() ->
                    {
                        if (rememberMe.isSelected())
                        {
                            preferences.put("username", username.getText());
                            preferences.put("password", password.getText());
                        }
                        chessUI.username=username.getText();
                        chessUI.getProfileDetails();

                        chessUI.chessTopBar=new ChessTopBar(chessUI);
                        chessUI.chessMenuBar=new ChessMenuBar(chessUI);

                        chessUI.homePage=new HomePage(chessUI);
                        chessUI.chessPage=new ChessPage(chessUI);
                        chessUI.socialPage=new SocialPage(chessUI);
                        chessUI.profilePage=new ProfilePage(chessUI);

                        signIn.setDisable(false);
                        signIn.setText("Sign In");
                        signIn.setGraphic(null);

                        chessUI.root.setTop(chessUI.chessTopBar);
                        chessUI.root.setLeft(chessUI.chessMenuBar);
                        chessUI.root.setCenter(chessUI.homePage);
                    });
                }
                else
                {
                    Platform.runLater(() ->
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("");
                        alert.setContentText("Invalid Username and Password");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(550);
                        alertStage.setY(350);
                        signIn.setDisable(false);
                        signIn.setText("Sign In");
                        signIn.setGraphic(null);
                        alert.showAndWait();
                    });
                }
            } catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }).start();
            chessUI.signUpPage=new SignUpPage(chessUI);
            chessUI.signInPage=new SignInPage(chessUI);
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
                passwordText.setPromptText("password");
                passwordText.setText(password.getText());
                passwordText.setFocusTraversable(false);
                AnchorPane.setTopAnchor(passwordText, 160.0);
                AnchorPane.setLeftAnchor(passwordText, 100.0);
                AnchorPane.setRightAnchor(passwordText, 100.0);

                loginPane.getChildren().remove(password);
                loginPane.getChildren().add(passwordText);
                loginPane.getChildren().remove(showPasswordButton);
                loginPane.getChildren().add(showPasswordButton);
                showPasswordButton.setGraphic(showImageView);
            }
            else
            {
                if (imageView.getImage().getUrl().contains("show"))
                {
                    password=new PasswordField();
                    password.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
                    Font passwordFont = Font.font("Sage UI",18);
                    password.setFont(passwordFont);
                    password.setPromptText("password");
                    password.setText(passwordText.getText());
                    password.setFocusTraversable(false);
                    AnchorPane.setTopAnchor(password, 160.0);
                    AnchorPane.setLeftAnchor(password, 100.0);
                    AnchorPane.setRightAnchor(password, 100.0);

                    loginPane.getChildren().remove(passwordText);
                    loginPane.getChildren().add(password);
                    loginPane.getChildren().remove(showPasswordButton);
                    loginPane.getChildren().add(showPasswordButton);
                    showPasswordButton.setGraphic(hideImageView);
                }
            }
        });

        signUpNow.setOnAction( actionEvent ->
        {
            chessUI.signUpPage=new SignUpPage(chessUI);
            chessUI.signInPage=new SignInPage(chessUI);
            chessUI.root.getChildren().clear();
            chessUI.root.setCenter(chessUI.signUpPage);
        });

    }

    private void makeLoginPane()
    {
        homeImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/HomePage/main page 3.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(800);
        homeImageView.setFitWidth(1540);
        homeImageView.setOpacity(0.8);
        this.getChildren().add(homeImageView);

        loginPane=new AnchorPane();
        loginPane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(loginPane, 200.0);
        AnchorPane.setBottomAnchor(loginPane, 200.0);
        AnchorPane.setLeftAnchor(loginPane, 500.0);
        AnchorPane.setRightAnchor(loginPane, 500.0);

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

        signInLabel=new Label("Sign In");
        Font signInLabelFont = Font.font("Sage UI",30);
        signInLabel.setFont(signInLabelFont);
        signInLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(signInLabel, 20.0);
        AnchorPane.setLeftAnchor(signInLabel, 225.0);

        username=new TextField();
        username.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font usernameFont = Font.font("Sage UI",18);
        username.setFont(usernameFont);
        username.setPromptText("username");
        username.setFocusTraversable(false);
        AnchorPane.setTopAnchor(username, 100.0);
        AnchorPane.setLeftAnchor(username, 100.0);
        AnchorPane.setRightAnchor(username, 100.0);

        password=new PasswordField();
        password.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font passwordFont = Font.font("Sage UI",18);
        password.setFont(passwordFont);
        password.setPromptText("password");
        password.setFocusTraversable(false);
        AnchorPane.setTopAnchor(password, 160.0);
        AnchorPane.setLeftAnchor(password, 100.0);
        AnchorPane.setRightAnchor(password, 100.0);

        passwordText = new TextField(password.getText());
        passwordText.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 40px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        passwordText.setFont(password.getFont());
        AnchorPane.setTopAnchor(passwordText, 160.0);
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
        AnchorPane.setTopAnchor(showPasswordButton, 164.0);
        AnchorPane.setLeftAnchor(showPasswordButton, 400.0);

        rememberMe=new CheckBox("Remember Me");
        rememberMe.setStyle("-fx-cursor: hand;");
        rememberMe.setTextFill(Color.WHITE);
        rememberMe.setFocusTraversable(false);
        AnchorPane.setTopAnchor(rememberMe, 210.0);
        AnchorPane.setLeftAnchor(rememberMe, 101.0);
        AnchorPane.setRightAnchor(rememberMe, 100.0);

        forgetPassword=new Button("Forget Password");
        forgetPassword.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        forgetPassword.setTextFill(Color.WHITE);
        forgetPassword.setFocusTraversable(false);
        AnchorPane.setTopAnchor(forgetPassword, 208.0);
        AnchorPane.setLeftAnchor(forgetPassword, 330.0);
        AnchorPane.setRightAnchor(forgetPassword, 90.0);

        signIn=new Button("Sign In");
        signIn.setStyle("-fx-cursor: hand; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        Font loginFont = Font.font("Sage UI",18);
        signIn.setFont(loginFont);
        signIn.setFocusTraversable(false);
        AnchorPane.setTopAnchor(signIn, 240.0);
        AnchorPane.setLeftAnchor(signIn, 100.0);
        AnchorPane.setRightAnchor(signIn, 100.0);

        differentSignUp=new Label("Or Sign Up Using");
        differentSignUp.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(differentSignUp, 265.0);
        AnchorPane.setLeftAnchor(differentSignUp, 225.0);

        Image googleImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/SignInPage/SignUpWith/google.png").toExternalForm());
        ImageView googleImageView=new ImageView(googleImage);
        googleImageView.setFitHeight(30);
        googleImageView.setFitWidth(30);
        googleImageView.setSmooth(true);

        googleSignUp=new Button();
        googleSignUp.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        googleSignUp.setTextFill(Color.WHITE);
        googleSignUp.setFocusTraversable(false);
        googleSignUp.setGraphic(googleImageView);
        AnchorPane.setTopAnchor(googleSignUp, 285.0);
        AnchorPane.setLeftAnchor(googleSignUp, 200.0);//205.0

        Image facebookImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/SignInPage/SignUpWith/facebook.png").toExternalForm());
        ImageView facebookImageView=new ImageView(facebookImage);
        facebookImageView.setFitHeight(30);
        facebookImageView.setFitWidth(30);
        facebookImageView.setSmooth(true);

        facebookSignUp=new Button();
        facebookSignUp.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        facebookSignUp.setTextFill(Color.WHITE);
        facebookSignUp.setFocusTraversable(false);
        facebookSignUp.setGraphic(facebookImageView);
        AnchorPane.setTopAnchor(facebookSignUp, 285.0);
        AnchorPane.setLeftAnchor(facebookSignUp, 248.0);

        Image twitterImage = new javafx.scene.image.Image(SignInPage.class.getResource("/com/chess/application/client/components/SignInPage/SignUpWith/twitter.png").toExternalForm());
        ImageView twitterImageView=new ImageView(twitterImage);
        twitterImageView.setFitHeight(30);
        twitterImageView.setFitWidth(30);
        twitterImageView.setSmooth(true);

        twitterSignUp=new Button();
        twitterSignUp.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        twitterSignUp.setTextFill(Color.WHITE);
        twitterSignUp.setFocusTraversable(false);
        twitterSignUp.setGraphic(twitterImageView);
        AnchorPane.setTopAnchor(twitterSignUp, 285.0);
        AnchorPane.setLeftAnchor(twitterSignUp, 295.0);

        dontHaveAnAccountLabel=new Label("Don't Have An Account  : (");
        dontHaveAnAccountLabel.setTextFill(Color.WHITE);
        dontHaveAnAccountLabel.setFocusTraversable(false);
        AnchorPane.setTopAnchor(dontHaveAnAccountLabel, 300.0);
        AnchorPane.setLeftAnchor(dontHaveAnAccountLabel, 160.0);

        signUpNow=new Button("Sign Up Now!");
        signUpNow.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        signUpNow.setTextFill(Color.SKYBLUE);
        signUpNow.setFocusTraversable(false);
        AnchorPane.setTopAnchor(signUpNow, 296.0);
        AnchorPane.setLeftAnchor(signUpNow, 300.0);


        loginPane.getChildren().addAll(signInLabel,username,password,showPasswordButton,rememberMe,forgetPassword,signIn,dontHaveAnAccountLabel,signUpNow);

        this.getChildren().addAll(loginPane,designer);
    }
}
