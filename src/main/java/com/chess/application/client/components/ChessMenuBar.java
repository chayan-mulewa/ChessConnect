package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ChessMenuBar extends AnchorPane
{
    public Button home,social,chess,setting,profile;
    private Image lightHomeImage,lightSocialImage,lightChessImage,lightSettingImage,lightProfileImage;
    private ImageView lightHomeImageView,lightSocialImageView,lightChessImageView,lightSettingImageView,lightProfileImageView;
    private Image darkHomeImage,darkSocialImage,darkChessImage,darkSettingImage,darkProfileImage;
    private ImageView darkHomeImageView,darkSocialImageView,darkChessImageView,darkSettingImageView,darkProfileImageView;
    private FadeTransition fadeOut,padeFade;
    private Rectangle barLine;
    private ChessUI chessUI;

    public ChessMenuBar(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        this.padeFade=new FadeTransition();
        this.fadeOut = new FadeTransition();
        createLightImages();
        createDarkImages();
        makeAllMenuButton();
    }

    private void setAppearance()
    {
        this.setPrefSize(65,0);
        this.setStyle("-fx-background-color: #262522;");
    }

    private void addListeners()
    {
        home.setOnAction(event ->
        {
            chessUI.chessTopBar.makeTopPaneVisible();
            this.chessUI.root.setCenter(chessUI.homePage);
            fadeOut.setDuration(Duration.seconds(0.3));
            fadeOut.setNode(lightHomeImageView);
            fadeOut.setFromValue(0);
            fadeOut.setToValue(4);
            fadeOut.setOnFinished(fade -> {
                lightHomeImageView.setImage(lightHomeImage);
            });
            fadeOut.play();
            home.setGraphic(lightHomeImageView);
            social.setGraphic(darkSocialImageView);
            chess.setGraphic(darkChessImageView);
//            setting.setGraphic(darkSettingImageView);
            profile.setGraphic(darkProfileImageView);
            moveBarLine(home);
        });
        social.setOnAction(event ->
        {
            chessUI.chessTopBar.makeTopPaneVisible();
            this.chessUI.root.setCenter(chessUI.socialPage);
//               padeFade.setDuration(Duration.seconds(0.5));
//               padeFade.setNode(chessUI.socialPage);
//               padeFade.setFromValue(0);
//               padeFade.setToValue(4);
//               padeFade.play();
            fadeOut.setDuration(Duration.seconds(0.3));
            fadeOut.setNode(lightSocialImageView);
            fadeOut.setFromValue(0);
            fadeOut.setToValue(5);
            fadeOut.setOnFinished(fade -> {
                lightSocialImageView.setImage(lightSocialImage);
            });
            fadeOut.play();
            home.setGraphic(darkHomeImageView);
            social.setGraphic(lightSocialImageView);
            chess.setGraphic(darkChessImageView);
//            setting.setGraphic(darkSettingImageView);
            profile.setGraphic(darkProfileImageView);
            moveBarLine(social);
        });
        chess.setOnAction(event ->
        {
            if (chessUI.chessTopBar.opponent_user_ID==0)
            {
                chessUI.chessTopBar.makeTopPaneNull();
                this.chessUI.root.setCenter(chessUI.chessPage);
//               padeFade.setDuration(Duration.seconds(0.5));
//               padeFade.setNode(chessUI.chessPage);
//               padeFade.setFromValue(0);
//               padeFade.setToValue(4);
//               padeFade.play();
                fadeOut.setDuration(Duration.seconds(0.3));
                fadeOut.setNode(lightChessImageView);
                fadeOut.setFromValue(0);
                fadeOut.setToValue(4);
                fadeOut.setOnFinished(fade -> {
                    lightChessImageView.setImage(lightChessImage);
                });
                fadeOut.play();
                home.setGraphic(darkHomeImageView);
                social.setGraphic(darkSocialImageView);
                chess.setGraphic(lightChessImageView);
//            setting.setGraphic(darkSettingImageView);
                profile.setGraphic(darkProfileImageView);
                moveBarLine(chess);
            }
            else
            {
                home.setDisable(true);
                social.setDisable(true);
                profile.setDisable(true);
                chessUI.chessTopBar.makeTopPaneNull();
                this.chessUI.root.setCenter(chessUI.chessPage);
//               padeFade.setDuration(Duration.seconds(0.5));
//               padeFade.setNode(chessUI.chessPage);
//               padeFade.setFromValue(0);
//               padeFade.setToValue(4);
//               padeFade.play();
                fadeOut.setDuration(Duration.seconds(0.3));
                fadeOut.setNode(lightChessImageView);
                fadeOut.setFromValue(0);
                fadeOut.setToValue(4);
                fadeOut.setOnFinished(fade -> {
                    lightChessImageView.setImage(lightChessImage);
                });
                fadeOut.play();
                home.setGraphic(darkHomeImageView);
                social.setGraphic(darkSocialImageView);
                chess.setGraphic(lightChessImageView);
//            setting.setGraphic(darkSettingImageView);
                profile.setGraphic(darkProfileImageView);
                moveBarLine(chess);
            }
        });
//        setting.setOnAction(event ->
//        {
//            chessUI.chessTopBar.makeTopPaneVisible();
//            this.chessUI.root.setCenter(null);
//            fadeOut.setDuration(Duration.seconds(0.3));
//            fadeOut.setNode(lightSettingImageView);
//            fadeOut.setFromValue(0);
//            fadeOut.setToValue(4);
//            fadeOut.setOnFinished(fade -> {
//                lightSettingImageView.setImage(lightSettingImage);
//            });
//            fadeOut.play();
//            home.setGraphic(darkHomeImageView);
//            social.setGraphic(darkSocialImageView);
//            chess.setGraphic(darkChessImageView);
//            setting.setGraphic(lightSettingImageView);
//            profile.setGraphic(darkProfileImageView);
//            moveBarLine(setting);
//        });
        profile.setOnAction(event ->
        {
            chessUI.chessTopBar.makeTopPaneVisible();
            this.chessUI.root.setCenter(chessUI.profilePage);
            fadeOut.setDuration(Duration.seconds(0.3));
            fadeOut.setNode(lightProfileImageView);
            fadeOut.setFromValue(0);
            fadeOut.setToValue(4);
            fadeOut.setOnFinished(fade -> {
                lightProfileImageView.setImage(lightProfileImage);
            });
            fadeOut.play();
            home.setGraphic(darkHomeImageView);
            social.setGraphic(darkSocialImageView);
            chess.setGraphic(darkChessImageView);
//            setting.setGraphic(darkSettingImageView);
            profile.setGraphic(lightProfileImageView);
            moveBarLine(profile);
        });
    }
    public void makeAllMenuButton()
    {
        home=new Button();
        home.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        home.setLayoutX(11);
        home.setLayoutY(240);
        home.setGraphic(lightHomeImageView);
        social=new Button();
        social.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        social.setLayoutX(11);
        social.setLayoutY(290);
        social.setGraphic(darkSocialImageView);
        chess=new Button();
        chess.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        chess.setLayoutX(11);
        chess.setLayoutY(340);
        chess.setGraphic(darkChessImageView);
//        setting=new Button();
//        setting.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
//        setting.setLayoutX(11);
//        setting.setLayoutY(390);
//        setting.setGraphic(darkSettingImageView);
        profile=new Button();
        profile.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");
        profile.setLayoutX(13);
        profile.setLayoutY(390);
        profile.setGraphic(darkProfileImageView);
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.web("#68a741"));
        glowEffect.setRadius(6);
        glowEffect.setSpread(0.5);
        barLine=new Rectangle();
        barLine.setX(home.getLayoutX()-11.5);
        barLine.setY(home.getLayoutY());
        barLine.setHeight(31);
        barLine.setWidth(6);
        barLine.setArcWidth(6);
        barLine.setArcHeight(6);
        barLine.setEffect(glowEffect);
        barLine.setFill(Color.web("#68a741"));
        this.getChildren().add(home);
        this.getChildren().add(social);
        this.getChildren().add(chess);
//        this.getChildren().add(setting);
        this.getChildren().add(profile);
        this.getChildren().add(barLine);
    }
    private void moveBarLine(Button button)
    {
        double startX = barLine.getX();
        double startY = barLine.getY();
        double newStartX = button.getLayoutX() - 11.5;
        double newStartY = button.getLayoutY();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), barLine);
        transition.setFromX(startX - newStartX);
        transition.setFromY(startY - newStartY);
        transition.setToX(0);
        transition.setToY(0);
        transition.play();
        barLine.setX(newStartX);
        barLine.setY(newStartY);
    }
    public void createLightImages()
    {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.WHITE);
        glowEffect.setRadius(5);
        glowEffect.setSpread(0.05);
        lightHomeImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Light/light home.png").toExternalForm());
        lightHomeImageView = new ImageView(lightHomeImage);
        lightHomeImageView.setFitHeight(24);
        lightHomeImageView.setFitWidth(24);
        lightHomeImageView.setEffect(glowEffect);
        lightHomeImageView.setSmooth(true);
        lightSocialImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Light/light social.png").toExternalForm());
        lightSocialImageView = new ImageView(lightSocialImage);
        lightSocialImageView.setFitHeight(24);
        lightSocialImageView.setFitWidth(24);
        lightSocialImageView.setEffect(glowEffect);
        lightSocialImageView.setSmooth(true);
        lightChessImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Light/light chess.png").toExternalForm());
        lightChessImageView = new ImageView(lightChessImage);
        lightChessImageView.setFitHeight(24);
        lightChessImageView.setFitWidth(24);
        lightChessImageView.setEffect(glowEffect);
        lightChessImageView.setSmooth(true);
//        lightSettingImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Light/light setting.png").toExternalForm());
//        lightSettingImageView = new ImageView(lightSettingImage);
//        lightSettingImageView.setFitHeight(24);
//        lightSettingImageView.setFitWidth(24);
//        lightSettingImageView.setEffect(glowEffect);
//        lightSettingImageView.setSmooth(true);
        lightProfileImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Light/light profile.png").toExternalForm());
        lightProfileImageView = new ImageView(lightProfileImage);
        lightProfileImageView.setFitHeight(22);
        lightProfileImageView.setFitWidth(22);
        lightProfileImageView.setEffect(glowEffect);
        lightProfileImageView.setSmooth(true);
    }
    public void createDarkImages()
    {
        darkHomeImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Dark/dark home.png").toExternalForm());
        darkHomeImageView = new ImageView(darkHomeImage);
        darkHomeImageView.setFitHeight(24);
        darkHomeImageView.setFitWidth(24);
        darkHomeImageView.setOpacity(1);
        darkHomeImageView.setSmooth(true);
        darkSocialImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Dark/dark social.png").toExternalForm());
        darkSocialImageView = new ImageView(darkSocialImage);
        darkSocialImageView.setFitHeight(24);
        darkSocialImageView.setFitWidth(24);
        darkSocialImageView.setOpacity(1);
        darkSocialImageView.setSmooth(true);
        darkChessImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Dark/dark chess.png").toExternalForm());
        darkChessImageView = new ImageView(darkChessImage);
        darkChessImageView.setFitHeight(24);
        darkChessImageView.setFitWidth(24);
        darkChessImageView.setOpacity(1);
        darkChessImageView.setSmooth(true);
//        darkSettingImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Dark/dark setting.png").toExternalForm());
//        darkSettingImageView = new ImageView(darkSettingImage);
//        darkSettingImageView.setFitHeight(24);
//        darkSettingImageView.setFitWidth(24);
//        darkSettingImageView.setOpacity(1);
//        darkSettingImageView.setSmooth(true);
        darkProfileImage = new Image(ChessMenuBar.class.getResource("/com/chess/application/client/components/ChessMenuBar/Dark/dark profile.png").toExternalForm());
        darkProfileImageView = new ImageView(darkProfileImage);
        darkProfileImageView.setFitHeight(22);
        darkProfileImageView.setFitWidth(22);
        darkProfileImageView.setOpacity(1);
        darkProfileImageView.setSmooth(true);
    }
}