package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ResponsiveAnchorPaneExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create an AnchorPane for the top section
        AnchorPane topAnchorPane = new AnchorPane();
        topAnchorPane.setStyle("-fx-background-color: black;");
        topAnchorPane.setPrefHeight(20);
        topAnchorPane.setPrefWidth(200);

        // Add child nodes to the top AnchorPane
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");

        AnchorPane temp = new AnchorPane();
        temp.setStyle("-fx-background-color: red;");
        temp.setPrefHeight(20);
        temp.setPrefWidth(200);
        AnchorPane.setTopAnchor(temp, 0.0);
        AnchorPane.setRightAnchor(temp, 0.0);
        AnchorPane.setBottomAnchor(temp, 0.0);
        AnchorPane.setLeftAnchor(temp, 0.0);



        Font font = Font.font("Sage UI",18);
        TextField searchUser=new TextField();
        searchUser.setStyle("-fx-background-color: #3b3b3b; -fx-text-inner-color: white; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        searchUser.setFont(font);
        searchUser.setPromptText("Search User");
//        searchUser.setPrefHeight(30);
//        searchUser.setPrefWidth(530);
        AnchorPane.setTopAnchor(searchUser, 0.0);
        AnchorPane.setRightAnchor(searchUser, 200.0);
        AnchorPane.setBottomAnchor(searchUser, 0.0);
        AnchorPane.setLeftAnchor(searchUser, 200.0);

        temp.getChildren().addAll(searchUser ,button2);

        topAnchorPane.getChildren().addAll(temp);

        // Set layout constraints for child nodes
        AnchorPane.setTopAnchor(button1, 10.0);
        AnchorPane.setLeftAnchor(button1, 10.0);
        AnchorPane.setTopAnchor(button2, 10.0);
        AnchorPane.setRightAnchor(button2, 50.0);


        // Add the top AnchorPane to the BorderPane
        root.setTop(topAnchorPane);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Responsive AnchorPane Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
