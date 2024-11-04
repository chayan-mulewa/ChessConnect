package com.chess.application.client.example;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimateLine extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    private Line line;

    public void start(Stage stage) throws Exception
    {
        AnchorPane root=new AnchorPane();

        Button one=new Button("one");
        one.setLayoutX(50);
        one.setLayoutY(50);
        one.setOnAction(event -> moveLine(one));

        line=new Line();
        line.setStrokeWidth(3);
        line.setStyle("-fx-background-color: red");
        line.setStartX(one.getLayoutX()-10);
        line.setStartY(one.getLayoutY());
        line.setEndX(one.getLayoutX()-10);
        line.setEndY(one.getLayoutX()+one.getLayoutY()/2);

        Button two=new Button("two");
        two.setLayoutX(50);
        two.setLayoutY(100);
        two.setOnAction(event -> moveLine(two));

        root.getChildren().addAll(one,two,line);
        Scene scene=new Scene(root,500,500);
        stage.setScene(scene);
        stage.show();
    }
    private void moveLine(Button button) {

//        line.setStartX(button.getLayoutX() - 10);
//        line.setStartY(button.getLayoutY());
//        line.setEndX(button.getLayoutX() - 10);
//        line.setEndY(button.getLayoutY() + button.getHeight());

        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        double newStartX = button.getLayoutX() - 10;
        double newStartY = button.getLayoutY();
        double newEndX = button.getLayoutX() - 10;
        double newEndY = button.getLayoutY() + button.getHeight();

        // Create a TranslateTransition for animating the line
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), line);
        transition.setFromX(startX - newStartX);
        transition.setFromY(startY - newStartY);
        transition.setToX(0);
        transition.setToY(0);
        transition.play();

        line.setStartX(newStartX);
        line.setStartY(newStartY);
        line.setEndX(newEndX);
        line.setEndY(newEndY);
    }

}