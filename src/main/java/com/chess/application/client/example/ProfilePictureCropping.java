package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfilePictureCropping extends Application {
    private ImageView imageView;
    private Rectangle cropRectangle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Profile Picture Cropping");

        // Create a VBox to hold the UI elements
        StackPane stackPane = new StackPane();

        // Create an ImageView to display the selected image
        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);

        // Create a crop rectangle for user-defined cropping
        cropRectangle = new Rectangle(100, 100);
        cropRectangle.setStyle("-fx-fill: null; -fx-stroke: red; -fx-stroke-dash-array: 5 5;");

        // Create a button to open the file chooser
        Button openButton = new Button("Open Image");
        openButton.setOnAction(e -> openFile());

        // Create a button to crop the image
        Button cropButton = new Button("Crop Image");
        cropButton.setOnAction(e -> cropImage());

        // Add elements to the StackPane
        stackPane.getChildren().addAll(imageView, cropRectangle, openButton, cropButton);

        // Create the scene and set it in the stage
        Scene scene = new Scene(stackPane, 420, 450);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Load and display the selected image
            imageView.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
        }
    }

    private void cropImage() {
        // Create a snapshot of the ImageView with the cropRectangle
        SnapshotParameters params = new SnapshotParameters();
        params.setViewport(new javafx.geometry.Rectangle2D(
                cropRectangle.getX(), cropRectangle.getY(),
                cropRectangle.getWidth(), cropRectangle.getHeight()
        ));

        WritableImage croppedImage = imageView.snapshot(params, null);

        // Replace the original image with the cropped image
        imageView.setImage(croppedImage);
    }
}

