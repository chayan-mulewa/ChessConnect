package com.chess.application.client.example;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Hamburger extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1f22");
        root.setPadding(new javafx.geometry.Insets(0,0,0,0));

        VBox sidebar = new VBox(10); // Vertical box for sidebar items
        sidebar.setStyle("-fx-background-color: #2b2d30");
        sidebar.setPrefWidth(150);
        sidebar.setAlignment(Pos.TOP_CENTER);
        Button home=new Button("Home");
        home.setPrefWidth(1500);
        sidebar.getChildren().add(home);
        Button profile=new Button("Profile");
        profile.setPrefWidth(1500);
        sidebar.getChildren().add(profile);

        Button toggleButton = new Button("☰");
        toggleButton.setTextFill(Color.WHITE);
        toggleButton.setPrefHeight(15);
        Font font = new Font(17);
        //Button font's size should increase to 40
        toggleButton.setFont(font);
        toggleButton.setStyle("-fx-background-color: #2b2d30");
        // Create a TranslateTransition for animating the sidebar
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        sidebarTransition.setFromX(-155);
        sidebarTransition.setToX(0);
        sidebar.setTranslateX(-150);

        // Add items to the sidebar
        // Add your navigation items (buttons, labels, etc.) here

        toggleButton.setOnAction(event -> {
            if (sidebar.getTranslateX() == 0) {
                sidebarTransition.setRate(-1);
            } else {
                sidebarTransition.setRate(1);
            }
            sidebarTransition.play();
        });


        root.setLeft(sidebar);
        Pane pane=new Pane();
        pane.setPrefHeight(40);
        pane.setStyle("-fx-background-color: #2b2d30");
        pane.getChildren().add(toggleButton);
        root.setTop(pane);
        root.setCenter(new ChessBoard());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Navigation Sidebar Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
