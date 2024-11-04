package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Table extends Application
{
    public void start(Stage stage) throws Exception
    {
        AnchorPane root=new AnchorPane();
        root.setStyle("-fx-background-color: darkgray;");
        root.setPrefSize(600, 500);

        AnchorPane one=new AnchorPane();
        one.setStyle("-fx-background-color: white;");
        one.setPrefSize(498, 120);


        VBox table = new VBox();
        table.setSpacing(10);
        table.getChildren().addAll(one);

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setStyle("-fx-background-color: gray; -fx-background: gray");
        scrollPane.setPrefSize(520,400);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AnchorPane.setTopAnchor(scrollPane,50.0);
        AnchorPane.setLeftAnchor(scrollPane,50.0);

        scrollPane.setContent(table);

        root.getChildren().add(scrollPane);

        Scene scene=new Scene(root,600,500);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args)
    {
        launch();
    }
}
