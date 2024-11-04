package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

public class CustomWindowButtonsApp extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 300);

        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");

        Label titleLabel = new Label("Custom Window Title");
        titleBar.getChildren().add(titleLabel);

        titleBar.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        titleBar.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        root.setTop(titleBar);
        root.setCenter(new Label("Content"));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
