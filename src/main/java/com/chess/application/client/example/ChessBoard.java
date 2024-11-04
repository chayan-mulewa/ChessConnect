package com.chess.application.client.example;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
public class ChessBoard extends GridPane
{
    Button[][] squares;
    public ChessBoard()
    {
        this.setStyle("-fx-background-color: black");
        this.setPrefSize(600,600);
        this.squares = new Button[8][8];
        initializeBoard();
    }
    private void initializeBoard() {
        for (int row=0;row<8;row++)
        {
            for (int col=0;col<8; col++)
            {
                Button button = new Button();
                squares[row][col] = button;
                // Set the button's color based on the chess board pattern
                if((row+col)%2==0)
                {
                    button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                else
                {
                    button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                button.setPrefWidth(getPrefWidth());
                button.setPrefHeight(getPrefHeight());
                this.add(button,row,col);
            }
        }
    }
}