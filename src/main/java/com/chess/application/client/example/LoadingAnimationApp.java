package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoadingAnimationApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GIF Button Example");

        // Load the GIF image from the resources

        Image gifImage = new Image(LoadingAnimationApp.class.getResource("/com/chess/application/client/components/Default/Loading/loading 3.gif").toExternalForm());

        // Create an ImageView to display the GIF animation
        ImageView imageView = new ImageView(gifImage);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Create a Button to hold the GIF animation
        Button gifButton = new Button();
        gifButton.setGraphic(imageView);
        gifButton.setStyle("-fx-background-color: transparent; -fx-image-rendering: optimizeQuality;");


        // Create a StackPane to center the button
        StackPane root = new StackPane();
        root.getChildren().add(gifButton);

        // Set the scene
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }
}

