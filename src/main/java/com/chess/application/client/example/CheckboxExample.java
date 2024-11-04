package com.chess.application.client.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckboxExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkbox Example");

        // Create two checkboxes
        CheckBox checkBox1 = new CheckBox("Checkbox 1");
        CheckBox checkBox2 = new CheckBox("Checkbox 2");

        checkBox1.setOnAction(actionEvent ->
        {
            checkBox2.setSelected(false);
            if(!checkBox1.isSelected())
            {
                checkBox1.setSelected(true);
            }
        });

        checkBox2.setOnAction(actionEvent ->
        {
            checkBox1.setSelected(false);
            if(!checkBox2.isSelected())
            {
                checkBox2.setSelected(true);
            }
        });

        // Initially, select checkbox1
        checkBox1.setSelected(true);

        // Create a layout for the checkboxes
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(checkBox1, checkBox2);

        // Create a scene and set it on the stage
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

