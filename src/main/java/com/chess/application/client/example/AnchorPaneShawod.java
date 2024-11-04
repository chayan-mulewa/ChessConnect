package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AnchorPaneShawod extends Application
{

    public void start(Stage stage) throws Exception
    {
        AnchorPane root=new AnchorPane();
        root.setStyle("-fx-background-color: gray");

        AnchorPane main=new AnchorPane();
        main.setStyle("-fx-background-color: red;");
        main.setLayoutX(100);
        main.setLayoutY(100);
        main.setPrefSize(200,200);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(5); // Adjust this value to control the shadow's vertical offset
        dropShadow.setColor(javafx.scene.paint.Color.GRAY);

        AnchorPane border=new AnchorPane();
        border.setStyle("-fx-background-color: red;");
        border.setPrefSize(50,50);
        border.setLayoutY(100);
        border.setLayoutX(100);
        border.setEffect(dropShadow);
        border.setStyle("-fx-background-color: red; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 0, 0, 0, 2.5);");

        main.getChildren().add(border);
        root.getChildren().add(main);
        Scene scene=new Scene(root,400,400);
        stage.setScene(scene);
        stage.show();
    }
}
