package com.chess.application.client.example;

import com.chess.application.client.components.ProfilePage;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

public class MultipleChoiceAlertExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Multiple Choice Alert Example");

        BorderPane borderPane=new BorderPane();

        Button button=new Button("Press");
        button.setOnAction( actionEvent ->
        {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("");
            alert.setHeaderText("");

            Image queenImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
            ImageView queenImageView=new ImageView(queenImage);
            queenImageView.setFitHeight(40);
            queenImageView.setFitWidth(40);

            Image knightImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
            ImageView knightImageView=new ImageView(knightImage);
            knightImageView.setFitHeight(40);
            knightImageView.setFitWidth(40);

            Image bishopImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
            ImageView bishopImageView=new ImageView(bishopImage);
            bishopImageView.setFitHeight(40);
            bishopImageView.setFitWidth(40);

            Image rookImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
            ImageView rookImageView=new ImageView(rookImage);
            rookImageView.setFitHeight(40);
            rookImageView.setFitWidth(40);

            HBox hBox=new HBox(46);
            hBox.getChildren().addAll(queenImageView,knightImageView,bishopImageView,rookImageView);
            hBox.setLayoutX(47);

            alert.getDialogPane().getChildren().addAll(hBox);

            // Create custom button types
            ButtonType option1 = new ButtonType("Option 1");
            ButtonType option2 = new ButtonType("Option 2");
            ButtonType option3 = new ButtonType("Option 3");
            ButtonType option4 = new ButtonType("Option 4");

            alert.getButtonTypes().setAll(option1, option2, option3, option4);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setOnCloseRequest(e -> e.consume());

            alert.showAndWait().ifPresent(response ->
            {
                if (response == option1) {
                    System.out.println("Option 1 selected");
                } else if (response == option2) {
                    System.out.println("Option 2 selected");
                } else if (response == option3) {
                    System.out.println("Option 3 selected");
                }
            });
        });
        borderPane.setCenter(button);

        Scene scene=new Scene(borderPane,200,200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
