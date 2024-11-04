package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundTester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sound Player Example");

        // Create a button
        Button playButton = new Button("Play Sound");
        playButton.setOnAction(event -> playSound());

        // Create a layout
        VBox vbox = new VBox(playButton);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to play the sound
    private void playSound() {
        try {
            // Load the audio file from the resources
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/check.wav");

            if (soundUrl != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("Sound file not found.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
