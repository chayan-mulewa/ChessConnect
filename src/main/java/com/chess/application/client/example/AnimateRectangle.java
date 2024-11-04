package com.chess.application.client.example;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimateRectangle extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    private Rectangle line;

    public void start(Stage stage) throws Exception
    {
        Pane root=new Pane();
        root.setStyle("-fx-background-color: gray");

        Button one=new Button("one");
        one.setLayoutX(50);
        one.setLayoutY(50);
        one.setOnAction(event -> moveLine(one));

        line=new Rectangle();
        line.setX(one.getLayoutX()-10);
        line.setY(one.getLayoutY());

        line.setHeight(one.getLayoutY()/2);
        line.setWidth(6);

        line.setArcWidth(6);
        line.setArcHeight(6);

        line.setFill(Color.WHITE);

        Button two=new Button("two");
        two.setLayoutX(50);
        two.setLayoutY(100);
        two.setOnAction(event -> moveLine(two));

        Button three=new Button("three");
        three.setLayoutX(50);
        three.setLayoutY(150);
        three.setOnAction(event -> moveLine(three));


        root.getChildren().addAll(one,two,three,line);
        Scene scene=new Scene(root,500,500);
        stage.setScene(scene);
        stage.show();
    }
    private void moveLine(Button button)
    {
        double startX = line.getX();
        double startY = line.getY();
        double newStartX = button.getLayoutX() - 10;
        double newStartY = button.getLayoutY();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), line);
        transition.setFromX(startX - newStartX);
        transition.setFromY(startY - newStartY);
        transition.setToX(0);
        transition.setToY(0);
        transition.play();

        line.setX(newStartX);
        line.setY(newStartY);

    }

}