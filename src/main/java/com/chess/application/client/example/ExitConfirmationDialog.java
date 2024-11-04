package com.chess.application.client.example;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ExitConfirmationDialog extends Application {

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Exit Confirmation Dialog Example");

        VBox root = new VBox();

        Scene scene = new Scene(root, 300, 200);

        primaryStage.setScene(scene);

        // Create a button to trigger the exit confirmation dialog
        javafx.scene.control.Button exitButton = new javafx.scene.control.Button("Exit");
        exitButton.setOnAction(e -> {
            // Create the confirmation dialog
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Resign");
            alert.setContentText("Are You Sure?");
            alert.getDialogPane().setStyle("-fx-background-color: #262522; -fx-color: #262522; -fx-background:  #262522;");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.setOnCloseRequest(Event::consume);


            // Add buttons to the dialog
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            // Show the dialog and wait for the user's response
            java.util.Optional<ButtonType> result = alert.showAndWait();

            // If the user clicks "Yes," exit the application
            if (result.isPresent() && result.get() == yesButton) {
                primaryStage.close();
            }
        });

        root.getChildren().add(exitButton);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
