package com.chess.application.client.example;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ItemChooserApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Item Chooser Example");

        // Create a ChoiceBox with the options
        ChoiceBox<String> itemChooser = new ChoiceBox<>();
        itemChooser.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        // Set an initial value (optional)
        itemChooser.setValue("Male");

        // Create a VBox to hold the ChoiceBox
        VBox vbox = new VBox(itemChooser);
        vbox.setSpacing(20);
        vbox.setStyle("-fx-padding: 20px");

        // Create a Scene
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
