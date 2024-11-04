package com.chess.application.client.example;

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//
//public class AnimateMenuApp extends Application {
//
//    private Line indicator;
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox();
//        HBox tabContainer = new HBox();
//        Pane contentContainer = new Pane();
//
//        // Create tabs
//        Label homeTab = createTab("Home");
//        Label profileTab = createTab("Profile");
//        Label settingsTab = createTab("Settings");
//
//        // Create an indicator line
//        indicator = new Line(0, 0,0 , 20);
//        indicator.setStyle("-fx-stroke-width: 2; -fx-stroke: black;");
//        indicator.setFill(Color.BLUE);
//
//        // Set the initial position of the indicator
//        indicator.setTranslateX(homeTab.getLayoutY());
//
//        // Add tabs to tab container
//        tabContainer.getChildren().addAll(homeTab, profileTab, settingsTab);
//
//        // Add indicator and content to the root container
//        root.getChildren().addAll(indicator,tabContainer, contentContainer);
//
//        // Set the content for each tab
//        homeTab.setOnMouseClicked(event -> {
//            updateIndicatorPosition(homeTab);
//            // Show corresponding content in contentContainer
//        });
//
//        profileTab.setOnMouseClicked(event -> {
//            updateIndicatorPosition(profileTab);
//            // Show corresponding content in contentContainer
//        });
//
//        settingsTab.setOnMouseClicked(event -> {
//            updateIndicatorPosition(settingsTab);
//            // Show corresponding content in contentContainer
//        });
//
//        Scene scene = new Scene(root, 400, 300);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Tabbed Interface Example");
//        primaryStage.show();
//    }
//
//    private Label createTab(String label) {
//        Label tab = new Label(label);
//        tab.setStyle("-fx-padding: 10;");
//        return tab;
//    }
//
//    private void updateIndicatorPosition(Label selectedTab) {
//        indicator.setTranslateX(selectedTab.getLayoutX());
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.VBox;
//import javafx.scene.shape.Line;
//import javafx.stage.Stage;
//
//public class AnimateMenuApp extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox vbox = new VBox(10); // Spacing between buttons
//
//        Button homeButton = new Button("Home");
//        Button profileButton = new Button("Profile");
//        Button settingButton = new Button("Setting");
//
//        Line highlightLine = new Line(0,0,0,22);
//        highlightLine.setStyle("-fx-stroke-width: 5; -fx-stroke: red;");
//        highlightLine.setManaged(false); // We'll manage its visibility manually
//        highlightLine.setVisible(false);
//        homeButton.setOnMouseClicked(event -> {
//            highlightLine.setVisible(true);
//        });
//
//        // Reset highlight when other buttons are clicked
//        profileButton.setOnMouseClicked(event -> highlightLine.setVisible(false));
//        settingButton.setOnMouseClicked(event -> highlightLine.setVisible(false));
//
//        vbox.getChildren().addAll(homeButton, profileButton, settingButton, highlightLine);
//
//        Scene scene = new Scene(vbox, 300, 200);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Button Highlight Example");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AnimateMenuApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button homeButton = createButtonWithIndicator("Home");
        Button profileButton = createButtonWithIndicator("Profile");
        Button settingButton = createButtonWithIndicator("Setting");

        StackPane root = new StackPane(homeButton, profileButton, settingButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Button Indicator Example");
        primaryStage.show();
    }

    private Button createButtonWithIndicator(String buttonText) {
        Button button = new Button(buttonText);
        Rectangle indicator = new Rectangle(10, 0); // Width of the indicator, initial height is 0
        indicator.setFill(Color.BLUE);

        StackPane stackPane = new StackPane(button, indicator);
        stackPane.setAlignment(indicator, javafx.geometry.Pos.CENTER_LEFT); // Position the indicator on the left

        button.setOnAction(event -> {
            double buttonHeight = button.getHeight();
            indicator.setHeight(buttonHeight);
        });

        return button;
    }
}
