package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import com.chess.common.ENUM.PROMOTION;
import com.chess.common.ENUM.RESIGN_TYPE;
import com.chess.common.Member;
import com.chess.common.Move;
import com.chess.common.Resign;
import com.chess.common.Transfer;
import com.google.gson.Gson;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlackChessBoardStockFish extends AnchorPane
{
    private Process stockfishProcess; // Add this field
    private PrintWriter stockfishWriter; // Add this field
    private List<String> moves;
    public Timer timer;
    public Button[][] squares;
    public GridPane chessBoard;
    public int selectedRow = -1;
    public int selectedCol = -1;
    public List<Boolean> checkmateList;
    public boolean isMyTurn=true;
    public int level;
    public ChessUI chessUI;
    public boolean kingSideCastle=true;
    public boolean queenSideCastle=true;

    public BlackChessBoardStockFish(ChessUI chessUI,int level)
    {
        this.chessUI=chessUI;
        this.level=level;
        this.checkmateList=new LinkedList<>();
        this.setPrefHeight(550);
        this.setPrefWidth(550);
        initComponents();
        setAppearance();
        addListeners();
        startStockfish();
        makeTimer();
    }

    private void initComponents()
    {
        setUpChessBoard();
    }

    private void setAppearance()
    {
        this.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10px, 0.5, 0, 0);");
    }

    private void addListeners()
    {

    }

    public void makeMoveErrorSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/move error.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip moveError= AudioSystem.getClip();
                moveError.open(audioInputStream);
                moveError.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }
    public void makeCheckSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/check.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip check= AudioSystem.getClip();
                check.open(audioInputStream);
                check.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }

    public void makeCaptureSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/capture.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip check= AudioSystem.getClip();
                check.open(audioInputStream);
                check.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }
    public void makeCheckMateSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/checkmate.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip check= AudioSystem.getClip();
                check.open(audioInputStream);
                check.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }
    public void makeMoveSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/move.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip check= AudioSystem.getClip();
                check.open(audioInputStream);
                check.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }

    public void makeCastleSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/castle.wav");
            if (soundUrl != null)
            {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
                Clip check= AudioSystem.getClip();
                check.open(audioInputStream);
                check.start();
            }
        } catch (Throwable throwable)
        {
            System.err.println("Sound file not found : "+throwable.getMessage());
        }
    }

    private void setUpChessBoard()
    {
        squares=new Button[8][8];
        chessBoard=new GridPane();
        chessBoard.setPrefSize(550,550);

        for (int row=0;row<8;row++)
        {
            for (int col=0;col<8; col++)
            {
                Button button = new Button();
                squares[row][col] = button;
                // Set the button's color based on the chess board pattern
                if((row+col)%2==0)
                {
                    button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                else
                {
                    button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                button.setPrefWidth(getPrefWidth());
                button.setPrefHeight(getPrefHeight());
                button.setOnAction(new ChessButtonActionListenerForBlackStockFish(this,row,col));
                this.chessBoard.add(button,col,row);

                if(row==0 || row==7)
                {
                    addPiece(button, getInitialPiece(row,col));
                }
                if(row==1)
                {
                    addPiece(button,"White Pawn");
                }
                else
                {
                    if (row==6)
                    {
                        addPiece(button,"Black Pawn");
                    }
                }
            }
        }
        this.getChildren().add(chessBoard);
    }

    private String getInitialPiece(int row,int col)
    {
        String[] whitePieces={"White Rook", "White Knight", "White Bishop", "White King", "White Queen", "White Bishop", "White Knight", "White Rook"};
        String[] blackPieces={"Black Rook", "Black Knight", "Black Bishop", "Black King", "Black Queen", "Black Bishop", "Black Knight", "Black Rook"};
        if(row==0)
        {
            return whitePieces[col];
        }
        else
        {
            return blackPieces[col];
        }
    }

    private void addPiece(Button button, String piece)
    {
        Image image=new Image(BlackChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/"+piece+".png").toExternalForm());
        ImageView imageView=new ImageView(image);
        button.setGraphic(imageView);
    }

    public void movePiece(int targetRow, int targetCol)
    {
        if (isMyTurn)
        {
            String notation = convertToChessNotation(selectedRow, selectedCol, targetRow, targetCol);
            String white=convertMoveFromBlackToWhitePerspective(notation);
            moves.add(white);

            for (int row=0;row<8;row++)
            {
                for (int col=0;col<8; col++)
                {
                    Button button = squares[row][col];
                    // Set the button's color based on the chess board pattern
                    if((row+col)%2==0)
                    {
                        button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                    }
                    else
                    {
                        button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                    }
                }
            }

            Button sourceButton=squares[selectedRow][selectedCol];

            if (sourceButton.getStyle().contains("#eeeed2"))
            {
                sourceButton.setStyle("-fx-background-color: #f4f680; -fx-cursor: hand; -fx-background-radius: 0;");
            }
            else
            {
                sourceButton.setStyle("-fx-background-color: #bbcc44; -fx-cursor: hand; -fx-background-radius: 0;");
            }

            Button targetButton=squares[targetRow][targetCol];
            if (targetButton.getStyle().contains("#eeeed2"))
            {
                targetButton.setStyle("-fx-background-color: #f4f680; -fx-cursor: hand; -fx-background-radius: 0;");
            }
            else
            {
                targetButton.setStyle("-fx-background-color: #bbcc44; -fx-cursor: hand; -fx-background-radius: 0;");
            }

            ImageView imageView=(ImageView) targetButton.getGraphic();
            ImageView sourceImageView=(ImageView) sourceButton.getGraphic();

            targetButton.setGraphic(sourceButton.getGraphic());
            sourceButton.setGraphic(null);

            PROMOTION promotion;

            if (targetRow==0 && sourceImageView.getImage().getUrl().contains("Pawn"))
            {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("");
                alert.setHeaderText("");

                Image queenImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Queen.png").toExternalForm());
                ImageView queenImageView=new ImageView(queenImage);
                queenImageView.setFitHeight(40);
                queenImageView.setFitWidth(40);

                Image knightImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Knight.png").toExternalForm());
                ImageView knightImageView=new ImageView(knightImage);
                knightImageView.setFitHeight(40);
                knightImageView.setFitWidth(40);

                Image bishopImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Bishop.png").toExternalForm());
                ImageView bishopImageView=new ImageView(bishopImage);
                bishopImageView.setFitHeight(40);
                bishopImageView.setFitWidth(40);

                Image rookImage = new Image(ProfilePage.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Rook.png").toExternalForm());
                ImageView rookImageView=new ImageView(rookImage);
                rookImageView.setFitHeight(40);
                rookImageView.setFitWidth(40);

                ButtonType queen = new ButtonType("Queen");
                ButtonType knight = new ButtonType("Knight");
                ButtonType bishop = new ButtonType("Bishop");
                ButtonType rook = new ButtonType("RooK");

                HBox hBox=new HBox(46);
                hBox.getChildren().addAll(queenImageView,knightImageView,bishopImageView,rookImageView);
                hBox.setLayoutX(47);
                alert.getDialogPane().getChildren().addAll(hBox);
                alert.getButtonTypes().setAll(queen, knight, bishop, rook);
                alert.getDialogPane().setStyle("-fx-background-color: lightgray;");

                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setX(592);
                stage.setY(412);
                stage.setResizable(false);
                stage.setOnCloseRequest(e -> e.consume());

                java.util.Optional<ButtonType> response = alert.showAndWait();

                if (response.get() == queen)
                {
                    Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Queen.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_QUEEN;
                }else if (response.get() == knight)
                {
                    Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Knight.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_KNIGHT;
                }else if (response.get() == bishop)
                {
                    Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Bishop.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_BISHOP;
                } else if (response.get() == rook)
                {
                    Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Rook.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_ROOK;
                }
                else
                {
                    promotion = null;
                }
            }
            else
            {
                promotion = null;
            }

            CheckValidatorForPlayerMoveForBlackStockFish checkValidatorForPlayerMove=new CheckValidatorForPlayerMoveForBlackStockFish(this);

            if (checkValidatorForPlayerMove.isKingHasCheck())
            {
                if (checkValidatorForPlayerMove.isKingHasCheckMate())
                {
                    if (checkmateList.contains(false))
                    {
                        makeCheckSoundEffect();
                    }
                    else
                    {
                        makeCheckMateSoundEffect();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("");
                        alert.setTitle("Win");
                        alert.setContentText("Checkmate You Win :)");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        ButtonType okWin=new ButtonType("Ok");
                        alert.getButtonTypes().setAll(okWin);
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(564);
                        alertStage.setY(384);
                        java.util.Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == okWin)
                        {
                            chessUI.chessPage.blackChessBoardStockFish=null;
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                        else
                        {
                            chessUI.chessPage.blackChessBoardStockFish=null;
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                    }
                    checkmateList.clear();
                }
                else
                {
                    if (checkValidatorForPlayerMove.isKingAsDraw())
                    {
                        makeCheckMateSoundEffect();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("");
                        alert.setTitle("Draw");
                        alert.setContentText("Game Stalemate");
                        alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                        ButtonType okWin=new ButtonType("Ok");
                        alert.getButtonTypes().setAll(okWin);
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(564);
                        alertStage.setY(384);
                        java.util.Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == okWin)
                        {
                            chessUI.chessPage.blackChessBoardStockFish=null;
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                        else
                        {
                            chessUI.chessPage.blackChessBoardStockFish=null;
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                            chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                    }
                    else
                    {
                        makeCheckSoundEffect();
                    }
                }
            }
            else
            {
                if (imageView!=null)
                {
                    makeCaptureSoundEffect();
                }
                else
                {
                    makeMoveSoundEffect();
                }
            }
            new Thread(()->
            {
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                sendMoveToStockfish(moves);
            }).start();
        }
        else
        {
            makeMoveErrorSoundEffect();
        }

        isMyTurn=true;

    }

    void movePieceForOpponent(int blackSelectedRow, int blackSelectedCol, int blackTargetRow, int blackTargetCol, PROMOTION promotion)
    {
        isMyTurn=true;

        for (int row=0;row<8;row++)
        {
            for (int col=0;col<8; col++)
            {
                Button button = squares[row][col];
                // Set the button's color based on the chess board pattern
                if((row+col)%2==0)
                {
                    button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                }
                else
                {
                    button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                }
            }
        }

        Button sourceButton=squares[blackSelectedRow][blackSelectedCol];
        if (sourceButton.getStyle().contains("#eeeed2"))
        {
            sourceButton.setStyle("-fx-background-color: #f4f680; -fx-cursor: hand; -fx-background-radius: 0;");
        }
        else
        {
            sourceButton.setStyle("-fx-background-color: #bbcc44; -fx-cursor: hand; -fx-background-radius: 0;");
        }
        Button targetButton=squares[blackTargetRow][blackTargetCol];
        if (targetButton.getStyle().contains("#eeeed2"))
        {
            targetButton.setStyle("-fx-background-color: #f4f680; -fx-cursor: hand; -fx-background-radius: 0;");
        }
        else
        {
            targetButton.setStyle("-fx-background-color: #bbcc44; -fx-cursor: hand; -fx-background-radius: 0;");
        }

        ImageView imageView=(ImageView) targetButton.getGraphic();
        targetButton.setGraphic(sourceButton.getGraphic());
        sourceButton.setGraphic(null);

        if (promotion!=null)
        {
            if (promotion==PROMOTION.PROMOTED_AS_QUEEN)
            {
                Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_KNIGHT)
            {
                Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Knight.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_BISHOP)
            {
                Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Bishop.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_ROOK)
            {
                Image image=new Image(BlackChessBoardStockFish.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Rook.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            }
        }

        CheckValidatorForOpponentMoveForBlackStockFish checkValidatorForOpponentMove=new CheckValidatorForOpponentMoveForBlackStockFish(this);

        if (checkValidatorForOpponentMove.isKingHasCheck())
        {
            if (checkValidatorForOpponentMove.isKingHasCheckMate())
            {
                if (checkmateList.contains(false))
                {
                    makeCheckSoundEffect();
                }
                else
                {
                    makeCheckMateSoundEffect();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Lose");
                    alert.setContentText("Checkmate You Loos :(");
                    alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                    ButtonType okWin=new ButtonType("Ok");
                    alert.getButtonTypes().setAll(okWin);
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.setX(564);
                    alertStage.setY(384);
                    java.util.Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okWin)
                    {
                        chessUI.chessPage.blackChessBoardStockFish=null;
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                    else
                    {
                        chessUI.chessPage.blackChessBoardStockFish=null;
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                }
                checkmateList.clear();
            }
            else
            {
                if (checkValidatorForOpponentMove.isKingAsDraw())
                {
                    makeCheckMateSoundEffect();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Draw");
                    alert.setContentText("Game Stalemate");
                    alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                    ButtonType okWin=new ButtonType("Ok");
                    alert.getButtonTypes().setAll(okWin);
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.setX(564);
                    alertStage.setY(384);
                    java.util.Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okWin)
                    {
                        chessUI.chessPage.blackChessBoardStockFish=null;
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                    else
                    {
                        chessUI.chessPage.blackChessBoardStockFish=null;
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);
                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                }
                else
                {
                    makeCheckSoundEffect();
                }
            }
        }
        else
        {
            if (imageView!=null)
            {
                makeCaptureSoundEffect();
            }
            else
            {
                makeMoveSoundEffect();
            }
        }
    }

    boolean isPathObstructed(int targetRow, int targetCol)
    {
        int rowDirection = targetRow - selectedRow;
        int colDirection = targetCol - selectedCol;

        int rowIncrement = (rowDirection == 0) ? 0 : rowDirection / Math.abs(rowDirection);
        int colIncrement = (colDirection == 0) ? 0 : colDirection / Math.abs(colDirection);

        int currentRow = selectedRow + rowIncrement;
        int currentCol = selectedCol + colIncrement;

        while (currentRow != targetRow || currentCol != targetCol)
        {
            if (squares[currentRow][currentCol].getGraphic() != null)
            {
                return true; // Path is obstructed by another piece.
            }
            currentRow += rowIncrement;
            currentCol += colIncrement;
        }
        return false; // Path is clear.
    }

    public String convertToChessNotation(int sourceRow, int sourceCol, int targetRow, int targetCol)
    {
        char sourceFile = (char) ('h' - sourceCol); // Adjust here
        char sourceRank = (char) ('8' - sourceRow);
        char targetFile = (char) ('h' - targetCol); // Adjust here
        char targetRank = (char) ('8' - targetRow);

        return "" + sourceFile + sourceRank + targetFile + targetRank;
    }

    private void startStockfish()
    {
        moves = new ArrayList<>();
        try
        {
            InputStream inputStream = WhiteChessBoardStockFish.class.getResourceAsStream("/com/chess/stockfish/stockfish.exe");
            File tempFile = File.createTempFile("stockfish", ".exe");

            try (FileOutputStream outputStream = new FileOutputStream(tempFile))
            {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            ProcessBuilder processBuilder = new ProcessBuilder(tempFile.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            stockfishProcess = processBuilder.start();
            OutputStream outputStream = stockfishProcess.getOutputStream();
            stockfishWriter = new PrintWriter(outputStream, true);

            new Thread(()->
            {
                try
                {
                    Thread.sleep(500);
                }catch (InterruptedException e)
                {
                }
                makeFirstMoveAsWhite();
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveToStockfish(List<String> moves)
    {
        try
        {
            // Send the UCI (Universal Chess Interface) commands to Stockfish FEN

            // position fen rnbkqbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBKQBNR b moves best without castle
            StringBuilder fullMoves = new StringBuilder();
            fullMoves.append("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq moves");

            for (String move : moves)
            {
                fullMoves.append(" ");
                fullMoves.append(move);
            }

            stockfishWriter.println(fullMoves.toString());

            // Request Stockfish to calculate the best move
            stockfishWriter.println("go depth "+level); // Adjust the depth as needed

            // Read Stockfish's response
            InputStream inputStream = stockfishProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean bestMoveFound = false;

            while ((line = reader.readLine()) != null) {
                // Process Stockfish's response, which may include the best move
                if (line.startsWith("bestmove")) {
                    // Extract the best move from Stockfish's response
                    String bestMove = line.split(" ")[1];

                    moves.add(bestMove);

                    String tempBestMove=convertMoveFromBlackToWhitePerspective(bestMove);

                    String sourceSquare = tempBestMove.substring(0, 2);
                    String targetSquare = tempBestMove.substring(2, 4);

                    // Convert source and target squares into rows and columns

                    int sourceRow = 7 - (sourceSquare.charAt(1) - '1');
                    int sourceCol = 'h' - sourceSquare.charAt(0); // Adjust here
                    int targetRow = 7 - (targetSquare.charAt(1) - '1');
                    int targetCol = 'h' - targetSquare.charAt(0); // Adjust here

                    // Update your chessboard with the move

                    Platform.runLater(()->
                    {
                        if (isEnPassantCapture(sourceCol, sourceRow, targetCol, targetRow))
                        {
                            handleEnPassantCapture(sourceCol, sourceRow, targetCol, targetRow);
                            return; // Return early after handling en passant
                        }

                        PROMOTION targetPromotion = null;
                        if (bestMove.length() == 5)
                        {
                            char promotionChar = bestMove.charAt(4);
                            if (promotionChar == 'q')
                            {
                                targetPromotion = PROMOTION.PROMOTED_AS_QUEEN;
                            }
                            else if (promotionChar == 'r')
                            {
                                targetPromotion = PROMOTION.PROMOTED_AS_ROOK;
                            }
                            else if (promotionChar == 'b')
                            {
                                targetPromotion = PROMOTION.PROMOTED_AS_BISHOP;
                            }
                            else if (promotionChar == 'n')
                            {
                                targetPromotion = PROMOTION.PROMOTED_AS_KNIGHT;
                            }
                        }

                        movePieceForOpponent(sourceRow, sourceCol, targetRow, targetCol,targetPromotion);
                        if (sourceCol == 3 && sourceRow == 0 && targetCol == 1 && targetRow == 0)
                        {
                            int rookSourceRow = 0; // The row of the king
                            int rookSourceCol = 0; // The original column of the kingside rook
                            int rookTargetRow = 0; // The row of the king
                            int rookTargetCol = 2; // The target column of the kingside rook

                            squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
                            squares[rookSourceRow][rookSourceCol].setGraphic(null);
                        }
                        else if (sourceCol == 3 && sourceRow == 0 && targetCol == 5 && targetRow == 0)
                        {
                            // Queenside castling for the black king
                            int rookSourceRow = 0; // The row of the king
                            int rookSourceCol = 7; // The original column of the queenside rook
                            int rookTargetRow = 0; // The row of the king
                            int rookTargetCol = 4; // The target column of the queenside rook

                            squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
                            squares[rookSourceRow][rookSourceCol].setGraphic(null);
                        }

                    });


                    bestMoveFound = true;
                    break;

                }
            }

            if (!bestMoveFound) {
                System.out.println("Stockfish did not provide a valid best move.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFirstMoveAsWhite()
    {

        String bestMove="e7e5";

        bestMove=convertMoveFromWhiteToBlackPerspective(bestMove);

        String sourceSquare = bestMove.substring(0, 2);
        String targetSquare = bestMove.substring(2, 4);

        // Convert source and target squares into rows and columns
        int sourceRow = sourceSquare.charAt(1) - '1';
        int sourceCol = 'h' - sourceSquare.charAt(0);
        int targetRow = targetSquare.charAt(1) - '1';
        int targetCol = 'h' - targetSquare.charAt(0);



        Platform.runLater(()->
        {
            movePieceForOpponent(sourceRow, sourceCol, targetRow, targetCol,null);
        });

        moves.add(bestMove);
    }

    public String convertMoveFromBlackToWhitePerspective(String whiteMove)
    {
        char sourceCol = whiteMove.charAt(0);
        char sourceRow = whiteMove.charAt(1);
        char targetCol = whiteMove.charAt(2);
        char targetRow = whiteMove.charAt(3);

        char newSourceCol = sourceCol;
        char newSourceRow = (char) ('8' - (sourceRow - '1'));
        char newTargetCol = targetCol;
        char newTargetRow = (char) ('8' - (targetRow - '1'));

        return "" + newSourceCol + newSourceRow + newTargetCol + newTargetRow;
    }

    public String convertMoveFromWhiteToBlackPerspective(String blackMove) {
        char sourceCol = blackMove.charAt(0);
        char sourceRow = blackMove.charAt(1);
        char targetCol = blackMove.charAt(2);
        char targetRow = blackMove.charAt(3);

        char newSourceCol = sourceCol;
        char newSourceRow = (char) ('8' - (sourceRow - '1'));
        char newTargetCol = targetCol;
        char newTargetRow = (char) ('8' - (targetRow - '1'));

        return "" + newSourceCol + newSourceRow + newTargetCol + newTargetRow;
    }


    public void castleKingsideRook(int selectedRow,int selectedCol,int targetRow,int targetCol)
    {
        kingSideCastle=false;
        queenSideCastle=false;

        movePiece(targetRow, targetCol);

        // Move the kingside rook
        int rookSourceRow = selectedRow; // The row of the king
        int rookSourceCol = 0; // The original column of the kingside rook
        int rookTargetRow = selectedRow; // The row of the king
        int rookTargetCol = 2; // The target column of the kingside rook

        squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
        squares[rookSourceRow][rookSourceCol].setGraphic(null);

        // Clear the selected square and update the board state
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    public void castleQueensideRook(int selectedRow, int selectedCol, int targetRow, int targetCol)
    {
        kingSideCastle=false;
        queenSideCastle=false;

        movePiece(targetRow, targetCol);

        // Move the queenside rook
        int rookSourceRow = selectedRow; // The row of the king
        int rookSourceCol = 7; // The original column of the queenside rook
        int rookTargetRow = selectedRow; // The row of the king
        int rookTargetCol = 4; // The target column of the queenside rook

        squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
        squares[rookSourceRow][rookSourceCol].setGraphic(null);

        // Clear the selected square and update the board state
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    private boolean isEnPassantCapture(int sourceCol, int sourceRow, int targetCol, int targetRow) {
        // Check if this is a valid en passant capture
        // - The moving piece must be a pawn.
        // - The target square must be empty.
        // - The source square must be next to the target square.

        // Implement the conditions and return true if it's en passant, false otherwise.

        if (sourceRow < 0 || sourceRow > 7 || targetRow < 0 || targetRow > 7 ||
                sourceCol < 0 || sourceCol > 7 || targetCol < 0 || targetCol > 7) {
            return false; // Invalid row or column values
        }

        // Get the image of the piece on the source square
        ImageView sourceSquareImage = (ImageView) squares[sourceRow][sourceCol].getGraphic();

        if (sourceSquareImage == null) {
            return false; // Source square is empty
        }

        Image sourceImage = sourceSquareImage.getImage();

        if (sourceImage == null) {
            return false; // Source square image is null
        }

        // Check if the source square contains a pawn
        if (!sourceImage.getUrl().contains("Pawn")) {
            return false; // Moving piece is not a pawn
        }

        // Check if the target square is empty
        if (squares[targetRow][targetCol].getGraphic() != null) {
            return false; // Target square is not empty
        }

        // Check if the source square is next to the target square
        int rowDifference = Math.abs(sourceRow - targetRow);
        int colDifference = Math.abs(sourceCol - targetCol);

        if (rowDifference == 1 && colDifference == 1) {
            return true; // It's a valid en passant capture
        }

        return false;
    }

    private void handleEnPassantCapture(int sourceCol, int sourceRow, int targetCol, int targetRow)
    {
        isMyTurn=true;
        // Remove the captured pawn from the board
        squares[sourceRow][targetCol].setGraphic(null);

        // Move the capturing pawn to the target square
        squares[targetRow][targetCol].setGraphic(squares[sourceRow][sourceCol].getGraphic());
        squares[sourceRow][sourceCol].setGraphic(null);
        makeCaptureSoundEffect();

    }

    private void makeTimer()
    {
        this.timer=new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                Platform.runLater(()->
                {
                    BlackChessBoardStockFish.this.setPrefHeight(550);
                    BlackChessBoardStockFish.this.setPrefWidth(550);

                    BlackChessBoardStockFish.this.chessBoard.setPrefHeight(550);
                    BlackChessBoardStockFish.this.chessBoard.setPrefWidth(550);

                    BlackChessBoardStockFish.this.requestLayout();
                    BlackChessBoardStockFish.this.chessBoard.requestLayout();

                    for (int row=0;row<8;row++)
                    {
                        for (int col=0;col<8; col++)
                        {
                            Button button = squares[row][col];
                            button.setPrefWidth(BlackChessBoardStockFish.this.getPrefWidth());
                            button.setPrefHeight(BlackChessBoardStockFish.this.getPrefHeight());
                        }
                    }
                });
            }
        });
        timer.start();
    }

}

class PawnMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;
    public int direction=-1;

    public PawnMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol) {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForPawn()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            int initialRow = 6; // Initial row for white (6) and black (1) pawns.

            if (c.selectedRow + direction == targetRow && c.selectedCol == targetCol && c.squares[targetRow][targetCol].getGraphic() == null)
            {
                c.movePiece(targetRow, targetCol);
            }
            else if (c.selectedRow + 2 * direction == targetRow && c.selectedCol == targetCol && c.selectedRow == initialRow && c.squares[targetRow][targetCol].getGraphic() == null)
            {
                c.movePiece(targetRow, targetCol);
            }
            else if (c.selectedRow + direction == targetRow && Math.abs(c.selectedCol - targetCol) == 1 && c.squares[targetRow][targetCol].getGraphic() != null)
            {
                c.movePiece(targetRow, targetCol);
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }
    public boolean isValidMoveForPawnBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            int initialRow = 6; // Initial row for white (6) and black (1) pawns.

            // Enforce pawn's movement rules: one step forward or two steps forward on the first move.
            if (c.selectedRow + direction == targetRow && c.selectedCol == targetCol && c.squares[targetRow][targetCol].getGraphic() == null)
            {
                return true;
            }
            else if (c.selectedRow + 2 * direction == targetRow && c.selectedCol == targetCol && c.selectedRow == initialRow && c.squares[targetRow][targetCol].getGraphic() == null)
            {
                return true;
            }
            // Enforce pawn's capture rule: diagonal movement to capture opponent's piece.
            else if (c.selectedRow + direction == targetRow && Math.abs(c.selectedCol - targetCol) == 1 && c.squares[targetRow][targetCol].getGraphic() != null)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
}

class RookMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;

    public RookMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol)
    {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForRook()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) == 0 && Math.abs(c.selectedCol - targetCol) != 0 || Math.abs(c.selectedRow - targetRow) != 0 && Math.abs(c.selectedCol - targetCol) == 0)
            {
                if (!c.isPathObstructed(targetRow, targetCol))
                {
                    c.movePiece(targetRow, targetCol);
                }
                else
                {
                    c.makeMoveErrorSoundEffect();
                }
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }

    public boolean isValidMoveForRookBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) == 0 && Math.abs(c.selectedCol - targetCol) != 0 || Math.abs(c.selectedRow - targetRow) != 0 && Math.abs(c.selectedCol - targetCol) == 0)
            {
                if (!c.isPathObstructed(targetRow, targetCol))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
}

class KnightMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;

    public KnightMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol)
    {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForKnight()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if ((Math.abs(c.selectedRow - targetRow) == 2 && Math.abs(c.selectedCol - targetCol) == 1) || (Math.abs(c.selectedRow - targetRow) == 1 && Math.abs(c.selectedCol - targetCol) == 2))
            {
                c.movePiece(targetRow, targetCol);
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }
    public boolean isValidMoveForKnightBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if ((Math.abs(c.selectedRow - targetRow) == 2 && Math.abs(c.selectedCol - targetCol) == 1) || (Math.abs(c.selectedRow - targetRow) == 1 && Math.abs(c.selectedCol - targetCol) == 2))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
}

class BishopMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;

    public BishopMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol)
    {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForBishop()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) == Math.abs(c.selectedCol - targetCol))
            {
                if (!c.isPathObstructed(targetRow, targetCol))
                {
                    c.movePiece(targetRow, targetCol);
                }
                else
                {
                    c.makeMoveErrorSoundEffect();
                }
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }
    public boolean isValidMoveForBishopBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) == Math.abs(c.selectedCol - targetCol))
            {
                if (!c.isPathObstructed(targetRow, targetCol))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
}

class QueenMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;

    public QueenMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol)
    {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForQueen()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            int rowDifference = Math.abs(c.selectedRow - targetRow);
            int colDifference = Math.abs(c.selectedCol - targetCol);

            if ((rowDifference == colDifference && !c.isPathObstructed(targetRow, targetCol)) ||
                    (rowDifference == 0 && colDifference != 0 && !c.isPathObstructed(targetRow, targetCol)) ||
                    (rowDifference != 0 && colDifference == 0 && !c.isPathObstructed(targetRow, targetCol)))
            {
                c.movePiece(targetRow, targetCol);
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }

    public boolean isValidMoveForQueenBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            int rowDifference = Math.abs(c.selectedRow - targetRow);
            int colDifference = Math.abs(c.selectedCol - targetCol);

            if ((rowDifference == colDifference && !c.isPathObstructed(targetRow, targetCol)) ||
                    (rowDifference == 0 && colDifference != 0 && !c.isPathObstructed(targetRow, targetCol)) ||
                    (rowDifference != 0 && colDifference == 0 && !c.isPathObstructed(targetRow, targetCol)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
}

class KingMoveValidatorForBlackStockFish
{
    BlackChessBoardStockFish c;
    int targetRow, targetCol;

    public KingMoveValidatorForBlackStockFish(BlackChessBoardStockFish c, int targetRow, int targetCol)
    {
        this.c = c;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public void isValidMoveForKing()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {

            if (Math.abs(c.selectedRow - targetRow) <= 1 && Math.abs(c.selectedCol - targetCol) <= 1)
            {
                if (isAdjacentToWhiteKing(targetRow, targetCol))
                {
                    c.makeMoveErrorSoundEffect();
                }
                else
                {
                    c.movePiece(targetRow, targetCol);
                }
            }
            else if (canCastleKingside(targetRow, targetCol))
            {
                c.castleKingsideRook(c.selectedRow,c.selectedCol,targetRow, targetCol);
                c.makeMoveSoundEffect();
            }
            else if (canCastleQueenside(targetRow, targetCol))
            {
                c.castleQueensideRook(c.selectedRow,c.selectedCol,targetRow, targetCol);
                c.makeMoveSoundEffect();
            }
            else
            {
                c.makeMoveErrorSoundEffect();
            }
//            if (Math.abs(c.selectedRow - targetRow) <= 1 && Math.abs(c.selectedCol - targetCol) <= 1)
//            {
//                if (isAdjacentToBlackKing(targetRow, targetCol))
//                {
//                    c.makeMoveErrorSoundEffect();
//                }
//                else
//                {
//                    c.movePiece(targetRow, targetCol);
//                }
//            }
//            else
//            {
//                c.makeMoveErrorSoundEffect();
//            }
        }
        else
        {
            c.makeMoveErrorSoundEffect();
        }
    }

    private boolean canCastleKingside(int targetRow, int targetCol)
    {
        // Check if it's a kingside castling move
        if (targetRow == c.selectedRow && targetCol == (c.selectedCol - 2)) {
            // Conditions for kingside castling:
            // 1. The king and kingside rook have not moved.
            // 2. The squares between the king and kingside rook are unoccupied.
            // 3. The king is not in check, moves through check, or ends up in check.

//            if (!c.hasWhiteKingsideRookMoved() &&
//                    !c.hasWhiteKingMoved() &&
//                    c.squares[c.selectedRow][7].getGraphic() == null &&  // Check if squares are empty
//                    c.squares[c.selectedRow][6].getGraphic() == null &&
//                    !isMoveIntoCheck(c.selectedRow, c.selectedCol + 1) &&
//                    !isMoveIntoCheck(c.selectedRow, c.selectedCol + 2)) {
//                return true;
//            }

            if (c.kingSideCastle &&c.squares[c.selectedRow][1].getGraphic() == null && c.squares[c.selectedRow][2].getGraphic() == null && !isMoveIntoCheck(c.selectedRow, c.selectedCol - 1) && !isMoveIntoCheck(c.selectedRow, c.selectedCol - 2))
            {
                return true;
            }
        }
        return false;
    }

    private boolean canCastleQueenside(int targetRow, int targetCol)
    {
        // Check if it's a queenside castling move
        if (targetRow == c.selectedRow && targetCol == (c.selectedCol + 2))
        {
            // Conditions for queenside castling:
            // 1. The king and queenside rook have not moved.
            // 2. The squares between the king and queenside rook are unoccupied.
            // 3. The king is not in check, moves through check, or ends up in check.

            if (c.queenSideCastle &&c.squares[c.selectedRow][4].getGraphic() == null && c.squares[c.selectedRow][5].getGraphic() == null && c.squares[c.selectedRow][6].getGraphic() == null && !isMoveIntoCheck(c.selectedRow, c.selectedCol + 1)  && !isMoveIntoCheck(c.selectedRow, c.selectedCol + 2) && !isMoveIntoCheck(c.selectedRow, c.selectedCol + 3))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isValidMoveForKingBoolean()
    {
        if (!isMoveIntoCheck(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) <= 1 && Math.abs(c.selectedCol - targetCol) <= 1)
            {
                if (!isAdjacentToWhiteKing(targetRow, targetCol))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidMoveForWhiteKingBoolean()
    {
        if (!isMoveIntoCheckForWhite(targetRow, targetCol))
        {
            if (Math.abs(c.selectedRow - targetRow) <= 1 && Math.abs(c.selectedCol - targetCol) <= 1)
            {
                if (!isAdjacentToBlackKing(targetRow, targetCol))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMoveIntoCheck(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheck();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }
    private boolean isKingInCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }
    public boolean isMoveIntoCheckForWhite(int row, int col)
    {
        // Simulate the king's move
        Button sourceButton = c.squares[c.selectedRow][c.selectedCol];
        Button targetButton = c.squares[row][col];
        ImageView originalImageView = (ImageView) sourceButton.getGraphic();
        ImageView targetImageView = (ImageView) targetButton.getGraphic();

        // Temporarily move the king
        targetButton.setGraphic(originalImageView);
        sourceButton.setGraphic(null);

        // Check if the king is in check after the move
        boolean isCheck = isKingInCheckForWhite();

        // Restore the board to its original state
        sourceButton.setGraphic(originalImageView);
        targetButton.setGraphic(targetImageView);

        return isCheck;
    }

    private boolean isKingInCheckForWhite()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = c.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreatForWhite(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    public boolean isValidThreatForWhite(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = c.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = (imageView != null) ? imageView.getImage() : null;

        if (image != null)
        {
            String pieceType = image.getUrl();

            int orignalRow=this.c.selectedRow;
            int orignalCol=this.c.selectedCol;

            this.c.selectedRow=fromRow;
            this.c.selectedCol=fromCol;

            if (pieceType.contains("Black%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForKingBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            this.c.selectedRow=orignalRow;
            this.c.selectedCol=orignalCol;

        }
        return false; // If the logic for the specific piece determines that the move threatens the king, return true
    }


    private boolean isAdjacentToWhiteKing(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = targetRow + dr;
                int newCol = targetCol + dc;

                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    Button button = c.squares[newRow][newCol];
                    ImageView imageView = (ImageView) button.getGraphic();
                    if (imageView != null) {
                        Image image = imageView.getImage();
                        if (image != null && image.getUrl().contains("White%20King")) {
                            return true; // Moving adjacent to the black king
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isAdjacentToBlackKing(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = targetRow + dr;
                int newCol = targetCol + dc;

                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    Button button = c.squares[newRow][newCol];
                    ImageView imageView = (ImageView) button.getGraphic();
                    if (imageView != null) {
                        Image image = imageView.getImage();
                        if (image != null && image.getUrl().contains("Black%20King")) {
                            return true; // Moving adjacent to the black king
                        }
                    }
                }
            }
        }
        return false;
    }
}

class ChessButtonActionListenerForBlackStockFish implements EventHandler
{
    private BlackChessBoardStockFish c;
    private int row;
    private int col;

    public ChessButtonActionListenerForBlackStockFish(BlackChessBoardStockFish c, int row, int col)
    {
        this.c = c;
        this.row = row;
        this.col = col;
    }

    public void handle(Event event)
    {
        CheckValidatorForOpponentMoveForBlackStockFish checkValidatorForOpponentMoveForBlackStockFish=new CheckValidatorForOpponentMoveForBlackStockFish(c);
        if (checkValidatorForOpponentMoveForBlackStockFish.isKingHasCheck())
        {
            ImageView whiteImageView= (ImageView) c.squares[row][col].getGraphic();
            Image whiteImage=null;
            if (whiteImageView!=null)
            {
                whiteImage=whiteImageView.getImage();
            }
            if ((whiteImage != null && whiteImage.getUrl().contains("Black")))
            {

                if (c.selectedRow != -1 && c.selectedCol != -1)
                {
                    // Check if the clicked piece is of the same color as the currently selected piece (white)
                    ImageView selectedImageView = (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                    Image selectedImage = (selectedImageView != null) ? selectedImageView.getImage() : null;

                    if (selectedImage != null && selectedImage.getUrl().contains("Black"))
                    {
                        if (c.selectedRow==row && c.selectedCol==col)
                        {
                            return;
                        }
                        c.selectedRow=-1;
                        c.selectedCol=-1;
                        c.makeMoveErrorSoundEffect();
                        return;
                    }
                }
                c.selectedRow = row;
                c.selectedCol = col;
            }
            else
            {
                if (c.selectedRow != -1 && c.selectedCol != -1)
                {
                    if (isValidMove(row,col))
                    {
                        if (isKingSitllInCheckAfterMove())
                        {
                            c.selectedRow=-1;
                            c.selectedCol=-1;
                            c.makeMoveErrorSoundEffect();
                        }
                        else
                        {
                            c.movePiece(row,col);
                        }
                    }
                    else
                    {
                        c.selectedRow=-1;
                        c.selectedCol=-1;
                        c.makeMoveErrorSoundEffect();
                    }
                }
            }
        }
        else
        {
            ImageView whiteImageView= (ImageView) c.squares[row][col].getGraphic();
            Image whiteImage=null;
            if (whiteImageView!=null)
            {
                whiteImage=whiteImageView.getImage();
            }
            if ((whiteImage != null && whiteImage.getUrl().contains("Black")))
            {

                if (c.selectedRow != -1 && c.selectedCol != -1)
                {
                    // Check if the clicked piece is of the same color as the currently selected piece (white)
                    ImageView selectedImageView = (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                    Image selectedImage = (selectedImageView != null) ? selectedImageView.getImage() : null;

                    if (selectedImage != null && selectedImage.getUrl().contains("Black"))
                    {
                        if (c.selectedRow==row && c.selectedCol==col)
                        {
                            return;
                        }
                        c.selectedRow=-1;
                        c.selectedCol=-1;
                        c.makeMoveErrorSoundEffect();
                        return;
                    }
                }
                c.selectedRow = row;
                c.selectedCol = col;
            }
            else
            {
                if (c.selectedRow != -1 && c.selectedCol != -1)
                {
                    whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                    whiteImage=null;
                    if (whiteImageView!=null)
                    {
                        whiteImage=whiteImageView.getImage();
                    }
                    if(whiteImage.getUrl().contains("Pawn"))
                    {
                        PawnMoveValidatorForBlackStockFish p=new PawnMoveValidatorForBlackStockFish(c, row, col);
                        p.isValidMoveForPawn();
                    }
                    else
                    {
                        whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                        whiteImage=null;
                        if (whiteImageView!=null)
                        {
                            whiteImage=whiteImageView.getImage();
                        }
                        if(whiteImage.getUrl().contains("Rook"))
                        {
                            RookMoveValidatorForBlackStockFish r=new RookMoveValidatorForBlackStockFish(c, row, col);
                            r.isValidMoveForRook();
                        }
                        else
                        {
                            whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                            whiteImage=null;
                            if (whiteImageView!=null)
                            {
                                whiteImage=whiteImageView.getImage();
                            }
                            if(whiteImage.getUrl().contains("Knight"))
                            {
                                KnightMoveValidatorForBlackStockFish k=new KnightMoveValidatorForBlackStockFish(c, row, col);
                                k.isValidMoveForKnight();
                            }
                            else
                            {
                                whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                                whiteImage=null;
                                if (whiteImageView!=null)
                                {
                                    whiteImage=whiteImageView.getImage();
                                }
                                if(whiteImage.getUrl().contains("Bishop"))
                                {
                                    BishopMoveValidatorForBlackStockFish b=new BishopMoveValidatorForBlackStockFish(c, row, col);
                                    b.isValidMoveForBishop();
                                }
                                else
                                {
                                    whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                                    whiteImage=null;
                                    if (whiteImageView!=null)
                                    {
                                        whiteImage=whiteImageView.getImage();
                                    }
                                    if(whiteImage.getUrl().contains("Queen"))
                                    {
                                        QueenMoveValidatorForBlackStockFish q=new QueenMoveValidatorForBlackStockFish(c, row, col);
                                        q.isValidMoveForQueen();
                                    }
                                    else
                                    {
                                        whiteImageView= (ImageView) c.squares[c.selectedRow][c.selectedCol].getGraphic();
                                        whiteImage=null;
                                        if (whiteImageView!=null)
                                        {
                                            whiteImage=whiteImageView.getImage();
                                        }
                                        if(whiteImage.getUrl().contains("King"))
                                        {
                                            KingMoveValidatorForBlackStockFish kn=new KingMoveValidatorForBlackStockFish(c, row, col);
                                            kn.isValidMoveForKing();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    c.selectedRow=-1;
                    c.selectedCol=-1;
                }
            }
        }
    }

    private boolean isKingSitllInCheckAfterMove()
    {
        Button tempFromButton = this.c.squares[c.selectedRow][c.selectedCol];
        Button tempToButton = this.c.squares[row][col];
        ImageView tempImageView = (ImageView) tempFromButton.getGraphic();

        // Make the move
        tempToButton.setGraphic(tempImageView);
        tempFromButton.setGraphic(null);

        CheckValidatorForOpponentMoveForBlackStockFish checkValidatorForOpponentMoveForBlackStockFish=new CheckValidatorForOpponentMoveForBlackStockFish(c);

        boolean check=checkValidatorForOpponentMoveForBlackStockFish.isKingHasCheck();

        tempFromButton.setGraphic(tempImageView);
        tempToButton.setGraphic(null);
        return check;
    }

    private boolean isValidMove(int targetRow,int targetCol)
    {
        Button button = this.c.squares[c.selectedRow][c.selectedCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image=null;
        if (imageView!=null)
        {
            image = imageView.getImage();

            String pieceType = image.getUrl();  // Get the type of black piece

            if (pieceType.contains("Black%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();
            }
            else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();
            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();
            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();
            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();
            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.c,targetRow,targetCol);
                return kingMoveValidatorForBlackStockFish.isValidMoveForKingBoolean();
            }
        }
        return false;
    }
}

class CheckValidatorForPlayerMoveForBlackStockFish
{
    BlackChessBoardStockFish board;
    public CheckValidatorForPlayerMoveForBlackStockFish(BlackChessBoardStockFish blackChessBoard)
    {
        this.board=blackChessBoard;
    }

    public boolean isKingHasCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    private boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = board.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image=null;
        if (imageView!=null)
        {
            image = imageView.getImage();

            String pieceType = image.getUrl();  // Get the type of black piece

            int orignalRow=this.board.selectedRow;
            int orignalCol=this.board.selectedCol;

            this.board.selectedRow=fromRow;
            this.board.selectedCol=fromCol;

            if (pieceType.contains("Black%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForKingBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            this.board.selectedRow=orignalRow;
            this.board.selectedCol=orignalCol;
            // If the logic for the specific piece determines that the move threatens the king, return true
            // Otherwise, return false
        }
        return false;
    }

    public boolean isKingHasCheckMate() {
        // Find the king's position
        int kingRow = -1;
        int kingCol = -1;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = this.board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null) {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White%20King")) {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1) {
            return false; // King not found, not in checkmate
        }

        // Check if the king is in check
        if (!isKingHasCheck()) {
            return false; // King is not in check, not in checkmate
        }

        // Generate all possible moves for the white player
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Button fromButton = this.board.squares[fromRow][fromCol];
                ImageView fromImageView = (ImageView) fromButton.getGraphic();
                if (fromImageView != null) {
                    Image fromImage = fromImageView.getImage();
                    if (fromImage != null && fromImage.getUrl().contains("White")) {
                        // This square has a white piece

                        for (int toRow = 0; toRow < 8; toRow++) {
                            for (int toCol = 0; toCol < 8; toCol++) {
                                if (isValidMove(fromRow, fromCol, toRow, toCol))
                                {
                                    // Simulate making the move on a copy of the board
                                    Button tempFromButton = this.board.squares[fromRow][fromCol];
                                    Button tempToButton = this.board.squares[toRow][toCol];
                                    ImageView tempFromImageView = (ImageView) tempFromButton.getGraphic();
                                    ImageView tempToImageView = (ImageView) tempToButton.getGraphic();

                                    // Make the move
                                    tempToButton.setGraphic(tempFromImageView);
                                    tempFromButton.setGraphic(null);

                                    // Check if the king is still in check after the move
                                    if (isKingHasCheck())
                                    {
                                        tempFromButton.setGraphic(tempFromImageView);
                                        tempToButton.setGraphic(tempToImageView);

                                        this.board.checkmateList.add(true);
                                    }
                                    else
                                    {
                                        tempFromButton.setGraphic(tempFromImageView);
                                        tempToButton.setGraphic(tempToImageView);

                                        this.board.checkmateList.add(false);
                                    }

                                    tempFromButton.setGraphic(tempFromImageView);
                                    tempToButton.setGraphic(tempToImageView);
                                }
                            }
                        }
                    }
                }
            }
        }

        // If no legal move can remove the check on the king, it's checkmate
        return true;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        // Check if the move is within the bounds of the chessboard
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }

        // Check if the source and target positions are the same
        if (fromRow == toRow && fromCol == toCol) {
            return false;
        }

        // Check if there is a piece at the source position
        Button fromButton = this.board.squares[fromRow][fromCol];
        ImageView fromImageView = (ImageView) fromButton.getGraphic();
        if (fromImageView == null) {
            return false;
        }

        // Check if there is a piece of the same color at the target position
        Button toButton = this.board.squares[toRow][toCol];
        ImageView toImageView = (ImageView) toButton.getGraphic();
        if (toImageView != null && toImageView.getImage() != null
                && toImageView.getImage().getUrl().contains("White")) {
            return false;
        }

        // Check the specific rules for each piece type
        Image fromImage = fromImageView.getImage();
        String pieceType = fromImage.getUrl();

        this.board.selectedRow=fromRow;
        this.board.selectedCol=fromCol;

        if (pieceType.contains("White%20Pawn"))
        {
            PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.board,toRow,toCol);
            pawnMoveValidatorForBlackStockFish.direction=1;

            boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }else if (pieceType.contains("White%20Rook"))
        {
            RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Knight"))
        {
            KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Bishop"))
        {
            BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Queen"))
        {
            QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20King"))
        {
            KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }

        this.board.selectedRow=-1;
        this.board.selectedCol=-1;

        return false;
    }
    private boolean isKingSitllInCheckAfterMove(int row,int col)
    {
        Button tempFromButton = this.board.squares[board.selectedRow][board.selectedCol];
        Button tempToButton = this.board.squares[row][col];
        ImageView tempImageView = (ImageView) tempFromButton.getGraphic();

        // Make the move
        tempToButton.setGraphic(tempImageView);
        tempFromButton.setGraphic(null);

        CheckValidatorForPlayerMoveForBlackStockFish checkValidatorForPlayerMoveForBlackStockFish=new CheckValidatorForPlayerMoveForBlackStockFish(board);

        boolean check=checkValidatorForPlayerMoveForBlackStockFish.isKingHasCheck();

        tempFromButton.setGraphic(tempImageView);
        tempToButton.setGraphic(null);
        return check;
    }
    public boolean isKingAsDraw()
    {
        // Iterate through all white pieces on the board
        for (int fromRow = 0; fromRow < 8; fromRow++)
        {
            for (int fromCol = 0; fromCol < 8; fromCol++)
            {
                Button fromButton = this.board.squares[fromRow][fromCol];
                ImageView fromImageView = (ImageView) fromButton.getGraphic();
                if (fromImageView != null)
                {
                    Image fromImage = fromImageView.getImage();
                    if (fromImage != null && fromImage.getUrl().contains("White"))
                    {

                        // This square has a black piece
                        for (int toRow = 0; toRow < 8; toRow++)
                        {
                            for (int toCol = 0; toCol < 8; toCol++)
                            {
                                if (isValidMove(fromRow, fromCol, toRow, toCol))
                                {
                                    // If there is at least one legal move, it's not a draw
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // If there are no legal moves left for any black piece, it's a draw
        return true;
    }
}

class CheckValidatorForOpponentMoveForBlackStockFish
{
    BlackChessBoardStockFish board;
    public CheckValidatorForOpponentMoveForBlackStockFish(BlackChessBoardStockFish blackChessBoard)
    {
        this.board=blackChessBoard;
    }

    public boolean isKingHasCheck()
    {
        int kingRow = -1;
        int kingCol = -1;

        // Find the king's position
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();

                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King"))
                    {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1)
        {
            return false; // King not found, not in check
        }

        // Check if any black piece can threaten the king without immediate capture
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Button button = board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null)
                {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("White"))
                    {
                        // Check if the black piece can attack the white king
                        if (isValidThreat(row, col, kingRow, kingCol))
                        {
                            return true; // White king is in check
                        }
                    }
                }
            }
        }
        return false; // King not threatened
    }

    private boolean isValidThreat(int fromRow, int fromCol, int toRow, int toCol)
    {
        Button button = board.squares[fromRow][fromCol];
        ImageView imageView = (ImageView) button.getGraphic();
        Image image=null;
        if (imageView!=null)
        {
            image = imageView.getImage();

            String pieceType = image.getUrl();  // Get the type of black piece

            int orignalRow=this.board.selectedRow;
            int orignalCol=this.board.selectedCol;

            this.board.selectedRow=fromRow;
            this.board.selectedCol=fromCol;

            if (pieceType.contains("White%20Pawn"))
            {
                PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.board,toRow,toCol);
                pawnMoveValidatorForBlackStockFish.direction=1;

                boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.board,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForWhiteKingBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            this.board.selectedRow=orignalRow;
            this.board.selectedCol=orignalCol;
            // If the logic for the specific piece determines that the move threatens the king, return true
            // Otherwise, return false
        }
        return false;
    }

    public boolean isKingHasCheckMate() {
        // Find the king's position
        int kingRow = -1;
        int kingCol = -1;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = this.board.squares[row][col];
                ImageView imageView = (ImageView) button.getGraphic();
                if (imageView != null) {
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().contains("Black%20King")) {
                        kingRow = row;
                        kingCol = col;
                        break;
                    }
                }
            }
        }

        if (kingRow == -1 || kingCol == -1) {
            return false; // King not found, not in checkmate
        }

        // Check if the king is in check
        if (!isKingHasCheck()) {
            return false; // King is not in check, not in checkmate
        }

        // Generate all possible moves for the white player
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Button fromButton = this.board.squares[fromRow][fromCol];
                ImageView fromImageView = (ImageView) fromButton.getGraphic();
                if (fromImageView != null) {
                    Image fromImage = fromImageView.getImage();
                    if (fromImage != null && fromImage.getUrl().contains("Black")) {
                        // This square has a white piece

                        for (int toRow = 0; toRow < 8; toRow++) {
                            for (int toCol = 0; toCol < 8; toCol++) {
                                if (isValidMove(fromRow, fromCol, toRow, toCol))
                                {
                                    // Simulate making the move on a copy of the board
                                    Button tempFromButton = this.board.squares[fromRow][fromCol];
                                    Button tempToButton = this.board.squares[toRow][toCol];
                                    ImageView tempFromImageView = (ImageView) tempFromButton.getGraphic();
                                    ImageView tempToImageView = (ImageView) tempToButton.getGraphic();

                                    // Make the move
                                    tempToButton.setGraphic(tempFromImageView);
                                    tempFromButton.setGraphic(null);

                                    // Check if the king is still in check after the move
                                    if (isKingHasCheck())
                                    {
                                        tempFromButton.setGraphic(tempFromImageView);
                                        tempToButton.setGraphic(tempToImageView);

                                        this.board.checkmateList.add(true);
                                    }
                                    else
                                    {
                                        tempFromButton.setGraphic(tempFromImageView);
                                        tempToButton.setGraphic(tempToImageView);

                                        this.board.checkmateList.add(false);
                                    }

                                    tempFromButton.setGraphic(tempFromImageView);
                                    tempToButton.setGraphic(tempToImageView);
                                }
                            }
                        }
                    }
                }
            }
        }

        // If no legal move can remove the check on the king, it's checkmate
        return true;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        // Check if the move is within the bounds of the chessboard
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }

        // Check if the source and target positions are the same
        if (fromRow == toRow && fromCol == toCol) {
            return false;
        }

        // Check if there is a piece at the source position
        Button fromButton = this.board.squares[fromRow][fromCol];
        ImageView fromImageView = (ImageView) fromButton.getGraphic();
        if (fromImageView == null) {
            return false;
        }

        // Check if there is a piece of the same color at the target position
        Button toButton = this.board.squares[toRow][toCol];
        ImageView toImageView = (ImageView) toButton.getGraphic();
        if (toImageView != null && toImageView.getImage() != null
                && toImageView.getImage().getUrl().contains("Black")) {
            return false;
        }

        // Check the specific rules for each piece type
        Image fromImage = fromImageView.getImage();
        String pieceType = fromImage.getUrl();

        this.board.selectedRow=fromRow;
        this.board.selectedCol=fromCol;

        if (pieceType.contains("Black%20Pawn"))
        {
            PawnMoveValidatorForBlackStockFish pawnMoveValidatorForBlackStockFish=new PawnMoveValidatorForBlackStockFish(this.board,toRow,toCol);
            boolean returnType= pawnMoveValidatorForBlackStockFish.isValidMoveForPawnBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }else if (pieceType.contains("Black%20Rook"))
        {
            RookMoveValidatorForBlackStockFish rookMoveValidatorForBlackStockFish=new RookMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = rookMoveValidatorForBlackStockFish.isValidMoveForRookBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Knight"))
        {
            KnightMoveValidatorForBlackStockFish knightMoveValidatorForBlackStockFish=new KnightMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = knightMoveValidatorForBlackStockFish.isValidMoveForKnightBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Bishop"))
        {
            BishopMoveValidatorForBlackStockFish bishopMoveValidatorForBlackStockFish=new BishopMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = bishopMoveValidatorForBlackStockFish.isValidMoveForBishopBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Queen"))
        {
            QueenMoveValidatorForBlackStockFish queenMoveValidatorForBlackStockFish=new QueenMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = queenMoveValidatorForBlackStockFish.isValidMoveForQueenBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20King"))
        {
            KingMoveValidatorForBlackStockFish kingMoveValidatorForBlackStockFish=new KingMoveValidatorForBlackStockFish(this.board,toRow,toCol);

            boolean returnType = kingMoveValidatorForBlackStockFish.isValidMoveForKingBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }

        this.board.selectedRow=-1;
        this.board.selectedCol=-1;

        return false;
    }
    private boolean isKingSitllInCheckAfterMove(int row,int col)
    {
        Button tempFromButton = this.board.squares[board.selectedRow][board.selectedCol];
        Button tempToButton = this.board.squares[row][col];
        ImageView tempImageView = (ImageView) tempFromButton.getGraphic();

        // Make the move
        tempToButton.setGraphic(tempImageView);
        tempFromButton.setGraphic(null);

        CheckValidatorForOpponentMoveForBlackStockFish checkValidatorForOpponentMoveForBlackStockFish=new CheckValidatorForOpponentMoveForBlackStockFish(board);

        boolean check=checkValidatorForOpponentMoveForBlackStockFish.isKingHasCheck();

        tempFromButton.setGraphic(tempImageView);
        tempToButton.setGraphic(null);
        return check;
    }

    public boolean isKingAsDraw()
    {
        // Iterate through all white pieces on the board
        for (int fromRow = 0; fromRow < 8; fromRow++)
        {
            for (int fromCol = 0; fromCol < 8; fromCol++)
            {
                Button fromButton = this.board.squares[fromRow][fromCol];
                ImageView fromImageView = (ImageView) fromButton.getGraphic();
                if (fromImageView != null)
                {
                    Image fromImage = fromImageView.getImage();
                    if (fromImage != null && fromImage.getUrl().contains("Black"))
                    {

                        // This square has a black piece
                        for (int toRow = 0; toRow < 8; toRow++)
                        {
                            for (int toCol = 0; toCol < 8; toCol++)
                            {
                                if (isValidMove(fromRow, fromCol, toRow, toCol))
                                {
                                    // If there is at least one legal move, it's not a draw
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // If there are no legal moves left for any black piece, it's a draw
        return true;
    }

}