package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HomePage extends AnchorPane
{
    private AnchorPane homePane;
    private Image homeImage;
    private ImageView homeImageView;
    private ChessUI chessUI;
    public HomePage(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        makeHomePane();
    }

    private void setAppearance()
    {
        this.setStyle("-fx-background-color: #302e2b;");
        this.setPrefHeight(200);
    }

    private void addListeners()
    {

    }

    public void makeHomePane()
    {
        homePane=new AnchorPane();
        homePane.setStyle("-fx-background-color: #322e2b;");
        homePane.setPrefSize(1470,720);

        homeImage = new javafx.scene.image.Image(HomePage.class.getResource("/com/chess/application/client/components/HomePage/main page 2.png").toExternalForm());
        homeImageView=new ImageView(homeImage);
        homeImageView.setFitHeight(750);
//        homeImageView.setFitWidth(1470);
        homeImageView.setOpacity(0.8);
        homeImageView.setFitWidth(1420);

        homePane.getChildren().add(homeImageView);
        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #322e2b;");
        anchorPane.setPrefHeight(100);
        anchorPane.setPrefWidth(300);
        anchorPane.setLayoutX(570);
        anchorPane.setLayoutY(40);


        getChildren().addAll(homePane,anchorPane);

    }
}
