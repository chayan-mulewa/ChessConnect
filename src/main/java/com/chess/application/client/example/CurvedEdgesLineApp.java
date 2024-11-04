package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CurvedEdgesLineApp extends Application {

    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Rectangle rectangle=new Rectangle(150,150,6,40);
        rectangle.setStyle("-fx-background-color: red");
        rectangle.setArcWidth(10);  // Adjust this value to control the roundness
        rectangle.setArcHeight(10);
        root.getChildren().add(rectangle);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
