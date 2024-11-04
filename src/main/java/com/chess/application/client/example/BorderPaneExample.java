package com.chess.application.client.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BorderPaneExample extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    public void start(Stage primaryStage) {
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: #111310");

//        Pane topPane = new Pane();
//        topPane.setPrefSize(600, 50);
//        topPane.setStyle("-fx-background-color: #222425;");
//        BorderPane.setAlignment(topPane, javafx.geometry.Pos.CENTER);
//        root.setTop(topPane);

//        ChessMenuBar leftPane = new ChessMenuBar();
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setColor(Color.WHITE);
//        dropShadow.setRadius(4);
//
//        Image image = new Image(BorderPaneExample.class.getResource("/com/chess/application/main/ChessMenuBar/Light/light home.png").toExternalForm());
//        ImageView imageView = new ImageView(image);
//        imageView.setEffect(dropShadow);
//        imageView.setSmooth(true);
//
//        Button home=new Button();
//        home.setStyle("-fx-background-color: transparent; -fx-background-image: url('" + image.getUrl() + "'); -fx-image-rendering: optimizeQuality;");
//        home.setLayoutX(12);
//        home.setLayoutY(200);
//        home.setGraphic(imageView);
//
//        imageView.setFitHeight(23);
//        imageView.setFitWidth(23.2);
//        imageView.setOpacity(0.7);
//        leftPane.getChildren().add(home);

//        BorderPane.setAlignment(leftPane, javafx.geometry.Pos.CENTER);
//        root.setLeft(leftPane);

//        Pane bottomPane = new Pane();
//        bottomPane.setPrefSize(600, 60);
//        bottomPane.setStyle("-fx-background-color: #222425;");
//        BorderPane.setAlignment(bottomPane, javafx.geometry.Pos.CENTER);
//        root.setBottom(bottomPane);

//        Pane rightPane = new Pane();
//        rightPane.setPrefSize(0, 352);
//        rightPane.setStyle("-fx-background-color: #222425;");
//        BorderPane.setAlignment(rightPane, javafx.geometry.Pos.CENTER);
//        root.setRight(rightPane);

        Pane centerPane = new Pane();
//        Image image = new Image(BorderPaneExample.class.getResource("/com/chess/application/main/main.png").toExternalForm());
//        ImageView imageView = new ImageView(image);
//        imageView.setX(250);
//        imageView.preserveRatioProperty().set(true);
//        imageView.fitWidthProperty().bind(centerPane.widthProperty());
//        imageView.fitHeightProperty().bind(centerPane.heightProperty());
//        centerPane.getChildren().add(imageView);
        centerPane.setStyle("-fx-background-color: #111310;");//-fx-background-radius: 20 0 0 20;");
        BorderPane.setAlignment(centerPane, Pos.CENTER);
        root.setCenter(centerPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX BorderPane Example");
//        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
