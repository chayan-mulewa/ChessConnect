package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.InetAddress;

public class InternetError extends AnchorPane
{
    public AnchorPane internetErrorPane;
    public Label oops,internetErrorLabel;
    public Button retry;
    public ChessUI chessUI;
    public InternetError(ChessUI chessUI)
    {
        this.chessUI=chessUI;
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents()
    {
        makeInternetErrorPane();
    }

    private void setAppearance()
    {
        setStyle("-fx-background-color: #302e2b;");
    }

    public void addListeners()
    {
        retry.setOnAction( actionEvent -> {
            try
            {
                InetAddress address = InetAddress.getByName("www.google.com");
                 if (address.isReachable(5000))
                 {
                     if (!chessUI.signInPage.preferences.get("username","").isEmpty() && !chessUI.signInPage.preferences.get("password","").isEmpty())
                     {
                         chessUI.username=chessUI.signInPage.preferences.get("username","");

                         chessUI.getProfileDetails();

                         chessUI.chessTopBar=new ChessTopBar(chessUI);
                         chessUI.chessMenuBar=new ChessMenuBar(chessUI);

                         chessUI.homePage=new HomePage(chessUI);
                         chessUI.chessPage=new ChessPage(chessUI);
                         chessUI.socialPage=new SocialPage(chessUI);
                         chessUI.profilePage=new ProfilePage(chessUI);

                         chessUI.root.setTop(chessUI.chessTopBar);
                         chessUI.root.setLeft(chessUI.chessMenuBar);
                         chessUI.root.setCenter(chessUI.homePage);
                     }
                     else
                     {
                         chessUI.root.setCenter(chessUI.signInPage);
                     }
                 }
                 else
                 {
                     chessUI.root.setCenter(chessUI.internetError);
                 }
            }
            catch (Throwable throwable)
            {
                chessUI.root.setCenter(chessUI.internetError);
            }
        });
    }

    private void makeInternetErrorPane()
    {
        internetErrorPane=new AnchorPane();
//        internetErrorPane.setStyle("-fx-background-color: #262522; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
        AnchorPane.setTopAnchor(internetErrorPane, 200.0);
        AnchorPane.setBottomAnchor(internetErrorPane, 200.0);
        AnchorPane.setLeftAnchor(internetErrorPane, 500.0);
        AnchorPane.setRightAnchor(internetErrorPane, 500.0);

        oops=new Label("Oops!!!");
        Font oopsFont = Font.font("Sage UI",30);
        oops.setFont(oopsFont);
        oops.setTextFill(Color.WHITE);
        oops.setFocusTraversable(false);
        AnchorPane.setTopAnchor(oops, 110.0);
        AnchorPane.setLeftAnchor(oops, 220.0);

        internetErrorLabel=new Label("Please check your internet connectivity and try again");
        Font internetErrorLabelFont = Font.font("Sage UI",20);
        internetErrorLabel.setFont(internetErrorLabelFont);
        internetErrorLabel.setTextFill(Color.WHITE);
        internetErrorLabel.setFocusTraversable(false);
        AnchorPane.setTopAnchor(internetErrorLabel, 160.0);
        AnchorPane.setLeftAnchor(internetErrorLabel, 30.0);

        retry=new Button("Retry");
        Font tryAgainFont = Font.font("Sage UI",18);
        retry.setFont(tryAgainFont);
        retry.setStyle("-fx-cursor: hand;");
        retry.setFocusTraversable(false);
        AnchorPane.setTopAnchor(retry, 210.0);
        AnchorPane.setLeftAnchor(retry, 230.0);

        internetErrorPane.getChildren().addAll(oops,internetErrorLabel,retry);
        this.getChildren().add(internetErrorPane);
    }

}
