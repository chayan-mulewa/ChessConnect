package com.chess.application.client.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;

public class JpgFileChooserExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JPG File Chooser Example");

        Button openButton = new Button("Open JPG File");
        ImageView imageView = new ImageView();

        openButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            // Determine the download folder based on the operating system
            String userHome = System.getProperty("user.home");
            String osName = System.getProperty("os.name").toLowerCase();
            File initialDirectory;

            if (osName.contains("win")) { // Windows
                initialDirectory = new File(userHome + "\\Downloads");
            } else if (osName.contains("mac")) { // macOS
                initialDirectory = new File(userHome + "/Downloads");
            } else { // Assume a default location for other operating systems
                initialDirectory = new File(userHome + "/Downloads");
            }

            fileChooser.setInitialDirectory(initialDirectory);

            // Configure the FileChooser to filter only JPG files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show the file chooser dialog
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                try {
                    // Load and display the selected JPG image
                    Image image = new Image(selectedFile.toURI().toString());
                    imageView.setImage(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    imageView.setSmooth(true);

                    byte[] imageData = new byte[0];
                    System.out.println(imageData.length);

                    FileInputStream fis = new FileInputStream(selectedFile);
                    imageData = fis.readAllBytes();
                    fis.close();
                    
                    System.out.println(imageData.length);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(new javafx.scene.layout.VBox(openButton, imageView), 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
