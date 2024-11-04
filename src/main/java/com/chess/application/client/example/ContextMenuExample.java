package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ContextMenuExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Context Menu Example");

        // Create a root layout
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 250);

        // Create a button
        Button button = new Button("Right-click me");

        // Create a context menu
        ContextMenu contextMenu = new ContextMenu();

        // Create menu items
        MenuItem menuItem1 = new MenuItem("Option 1");
        MenuItem menuItem2 = new MenuItem("Option 2");

        // Add menu items to the context menu
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        // Attach the context menu to the button
        button.setContextMenu(contextMenu);

        root.getChildren().add(button);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
