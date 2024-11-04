package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Chess extends Application
{

    public void start(Stage stage) throws Exception
    {
        AnchorPane anchorPane=new AnchorPane();
        ChessBoard chessBoard=new ChessBoard();
        anchorPane.getChildren().add(chessBoard);

        Scene scene=new Scene(anchorPane,600,600);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args)
    {
        launch();
    }
}
