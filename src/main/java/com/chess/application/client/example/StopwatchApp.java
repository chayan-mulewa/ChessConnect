package com.chess.application.client.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StopwatchApp extends Application {
    private int elapsedSeconds = 0;
    private Label timerLabel = new Label("00:00");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-font-size: 24px;");
        root.getChildren().add(timerLabel);

        Scene scene = new Scene(root, 200, 100);
        primaryStage.setTitle("Stopwatch");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create a timeline to update the timer every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        elapsedSeconds++;
                        updateTimerLabel();
                    }
                })
        );

        // Set the total duration to 10 minutes (600 seconds)
        timeline.setCycleCount(600);
        timeline.play();
    }

    private void updateTimerLabel() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(timeText);
    }
}
