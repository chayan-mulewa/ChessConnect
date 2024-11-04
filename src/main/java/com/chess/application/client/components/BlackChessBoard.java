package com.chess.application.client.components;

import com.chess.application.client.ui.ChessUI;
import com.chess.common.ENUM.PROMOTION;
import com.chess.common.ENUM.RESIGN_TYPE;
import com.chess.common.Member;
import com.chess.common.Move;
import com.chess.common.Resign;
import com.chess.common.Transfer;
import com.google.gson.Gson;
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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class BlackChessBoard extends AnchorPane
{
    public Button[][] squares;
    public GridPane chessBoard;
    public int selectedRow=-1;
    public int selectedCol=1;
    public int whiteSelectedRow = -1;
    public int whiteSelectedCol = -1;
    public int whiteTargetRow = -1;
    public int whiteTargetCol = -1;
    public javax.swing.Timer timerForMove,timerForResign;
    public ChessUI chessUI;
    public List<Boolean> checkmateList;
    public boolean  isMyTurn=false;
    public boolean kingSideCastle=true;
    public boolean queenSideCastle=true;

    public BlackChessBoard(ChessUI chessUI)
    {
        this.checkmateList=new LinkedList<>();
        this.chessUI=chessUI;
        this.setPrefSize(550,550);
        initComponents();
        setAppearance();
        addListeners();
        makeTimerForMove();
        makeTimerForResign();
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
                    button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                else
                {
                    button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                    button.setOnMouseEntered(e -> {
                        button.setOpacity(0.85);
                    });

                    button.setOnMouseExited(e -> {
                        button.setOpacity(1);
                    });
                }
                button.setPrefWidth(getPrefWidth());
                button.setPrefHeight(getPrefHeight());
                button.setOnAction(new ChessButtonActionListenerForBlack(this,row,col));
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
            for (int row=0;row<8;row++)
            {
                for (int col=0;col<8; col++)
                {
                    Button button = squares[row][col];
                    // Set the button's color based on the chess board pattern
                    if((row+col)%2==0)
                    {
                        button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                    }
                    else
                    {
                        button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                    }
                }
            }
            chessUI.chessPage.timelineForPlayer.stop();
            chessUI.chessPage.timelineForOpponent.play();

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
                    Image image=new Image(WhiteChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Queen.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_QUEEN;
                }else if (response.get() == knight)
                {
                    Image image=new Image(WhiteChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Knight.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_KNIGHT;
                }else if (response.get() == bishop)
                {
                    Image image=new Image(WhiteChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Bishop.png").toExternalForm());
                    ImageView tempImageView=new ImageView(image);
                    targetButton.setGraphic(null);
                    targetButton.setGraphic(tempImageView);
                    promotion=PROMOTION.PROMOTED_AS_BISHOP;
                } else if (response.get() == rook)
                {
                    Image image=new Image(WhiteChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/Black Rook.png").toExternalForm());
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

            sendMoveToServer(selectedRow,selectedCol,targetRow,targetCol, promotion);

            CheckValidatorForPlayerMoveForBlack checkValidatorForPlayerMoveForBlack=new CheckValidatorForPlayerMoveForBlack(this);

            if (checkValidatorForPlayerMoveForBlack.isKingHasCheck())
            {
                if (checkValidatorForPlayerMoveForBlack.isKingHasCheckMate())
                {
                    if (checkmateList.contains(false))
                    {
                        makeCheckSoundEffect();
                    }
                    else
                    {
                        makeCheckMateSoundEffect();
                        timerForResign.stop();
                        timerForMove.stop();
                        Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                        Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);
                        int playerRating = player.getElo();
                        int opponentRating = opponent.getElo();
                        int k=EloCalculator.calculateDynamicK(player.getGamePlayed());
                        double actualScore = 0;
                        // Calculate expected scores
                        double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);
                        // Calculate new ratings
                        int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);
                        try
                        {
                            Transfer tempTransfer=new Transfer();
                            tempTransfer.user_ID=chessUI.user_ID;
                            tempTransfer.elo=newPlayerRating;
                            chessUI.client.execute("/Chess_Server/Update_Player_Elo",tempTransfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                        try
                        {
                            Transfer transfer=new Transfer();
                            transfer.user_ID=chessUI.user_ID;
                            chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                        chessUI.chessTopBar.isPlaying=false;
                        chessUI.chessTopBar.opponent_user_ID=0;
                        chessUI.chessTopBar.opponent_elo=0;
                        chessUI.chessTopBar.player_elo=0;
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
                            chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                                chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                        else
                        {
                            chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                                chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                    }
                    checkmateList.clear();
                }
                else
                {
                    if (checkValidatorForPlayerMoveForBlack.isKingAsDraw())
                    {
                        makeCheckMateSoundEffect();
                        timerForResign.stop();
                        timerForMove.stop();
                        Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                        Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);
                        int playerRating = player.getElo();
                        int opponentRating = opponent.getElo();
                        int k=EloCalculator.calculateDynamicK(player.getGamePlayed());
                        double actualScore = 0;
                        // Calculate expected scores
                        double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);
                        // Calculate new ratings
                        int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);
                        try
                        {
                            Transfer tempTransfer=new Transfer();
                            tempTransfer.user_ID=chessUI.user_ID;
                            tempTransfer.elo=newPlayerRating;
                            chessUI.client.execute("/Chess_Server/Update_Player_Elo",tempTransfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                        try
                        {
                            Transfer transfer=new Transfer();
                            transfer.user_ID=chessUI.user_ID;
                            chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                        }catch (Throwable t)
                        {
                            System.out.println("Member List : "+t);
                        }
                        chessUI.chessTopBar.isPlaying=false;
                        chessUI.chessTopBar.opponent_user_ID=0;
                        chessUI.chessTopBar.opponent_elo=0;
                        chessUI.chessTopBar.player_elo=0;
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
                            chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                    chessUI.chessMenuBar.home.setDisable(false);
                            chessUI.chessMenuBar.social.setDisable(false);
                            chessUI.chessMenuBar.profile.setDisable(false);
                            chessUI.chessMenuBar.home.fire();
                        }
                        else
                        {
                            chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                            AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                            AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                            chessUI.chessPage.chessPane.getChildren().clear();
                            chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                    chessUI.chessMenuBar.home.setDisable(false);
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
        else
        {
            makeMoveErrorSoundEffect();
        }

        isMyTurn=false;
    }

    void movePieceForOpponent(int whiteSelectedRow,int whiteSelectedCol,int whiteTargetRow,int whiteTargetCol,PROMOTION promotion)
    {
//        System.out.println("from  row : "+whiteSelectedRow);
//        System.out.println("from  col : "+whiteSelectedCol);
//        System.out.println("to  row : "+whiteTargetRow);
//        System.out.println("from  col : "+whiteTargetCol);
        for (int row=0;row<8;row++)
        {
            for (int col=0;col<8; col++)
            {
                Button button = squares[row][col];
                // Set the button's color based on the chess board pattern
                if((row+col)%2==0)
                {
                    button.setStyle("-fx-background-color: #eeeed2; -fx-cursor: hand; -fx-background-radius: 0;");
                }
                else
                {
                    button.setStyle("-fx-background-color: #769655; -fx-cursor: hand; -fx-background-radius: 0;");
                }
            }
        }
        chessUI.chessPage.timelineForPlayer.play();
        chessUI.chessPage.timelineForOpponent.stop();

        isMyTurn=true;

        Button sourceButton=squares[whiteSelectedRow][whiteSelectedCol];
        if (sourceButton.getStyle().contains("#eeeed2"))
        {
            sourceButton.setStyle("-fx-background-color: #f4f680; -fx-cursor: hand; -fx-background-radius: 0;");
        }
        else
        {
            sourceButton.setStyle("-fx-background-color: #bbcc44; -fx-cursor: hand; -fx-background-radius: 0;");
        }
        Button targetButton=squares[whiteTargetRow][whiteTargetCol];
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
                Image image=new Image(BlackChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Queen.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_KNIGHT)
            {
                Image image=new Image(BlackChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Knight.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_BISHOP)
            {
                Image image=new Image(BlackChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Bishop.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            } else if (promotion==PROMOTION.PROMOTED_AS_ROOK)
            {
                Image image=new Image(BlackChessBoard.class.getResource("/com/chess/application/client/components/ChessPage/Chess Pieces/White Rook.png").toExternalForm());
                ImageView tempImageView=new ImageView(image);
                targetButton.setGraphic(null);
                targetButton.setGraphic(tempImageView);

            }
        }

        if (whiteSelectedCol == 3 && whiteSelectedRow == 0 && whiteTargetCol == 1 && whiteTargetRow == 0)
        {
            int rookSourceRow = 0; // The row of the king
            int rookSourceCol = 0; // The original column of the kingside rook
            int rookTargetRow = 0; // The row of the king
            int rookTargetCol = 2; // The target column of the kingside rook

            squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
            squares[rookSourceRow][rookSourceCol].setGraphic(null);
        }
        else if (whiteSelectedCol == 3 && whiteSelectedRow == 0 && whiteTargetCol == 5 && whiteTargetRow == 0)
        {
            // Queenside castling for the black king
            int rookSourceRow = 0; // The row of the king
            int rookSourceCol = 7; // The original column of the queenside rook
            int rookTargetRow = 0; // The row of the king
            int rookTargetCol = 4; // The target column of the queenside rook

            squares[rookTargetRow][rookTargetCol].setGraphic(squares[rookSourceRow][rookSourceCol].getGraphic());
            squares[rookSourceRow][rookSourceCol].setGraphic(null);
        }

        CheckValidatorForOpponentMoveForBlack checkValidatorForOpponentMoveForBlack=new CheckValidatorForOpponentMoveForBlack(this);

        if (checkValidatorForOpponentMoveForBlack.isKingHasCheck())
        {
            if (checkValidatorForOpponentMoveForBlack.isKingHasCheckMate())
            {
                if (checkmateList.contains(false))
                {
                    makeCheckSoundEffect();
                }
                else
                {
                    makeCheckMateSoundEffect();

                    timerForResign.stop();
                    timerForMove.stop();

                    Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                    Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);

                    int playerRating = player.getElo();
                    int opponentRating = opponent.getElo();

                    int k=EloCalculator.calculateDynamicK(player.getGamePlayed());

                    double actualScore = 0;

                    // Calculate expected scores
                    double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);

                    // Calculate new ratings
                    int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);

                    try
                    {
                        Transfer tempTransfer=new Transfer();
                        tempTransfer.user_ID=chessUI.user_ID;
                        tempTransfer.elo=newPlayerRating;

                        chessUI.client.execute("/Chess_Server/Update_Player_Elo",tempTransfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }

                    try
                    {
                        Transfer transfer=new Transfer();
                        transfer.user_ID=chessUI.user_ID;

                        chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }

                    chessUI.chessTopBar.isPlaying=false;
                    chessUI.chessTopBar.opponent_user_ID=0;
                    chessUI.chessTopBar.opponent_elo=0;
                    chessUI.chessTopBar.player_elo=0;

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Lose");
                    alert.setContentText("Checkmate You Lose :(");
                    alert.getDialogPane().setStyle("-fx-background-color: lightgray;");
                    ButtonType okWin=new ButtonType("Ok");
                    alert.getButtonTypes().setAll(okWin);
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.setX(564);
                    alertStage.setY(384);
                    java.util.Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okWin)
                    {
                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                    else
                    {
                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                        chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                }
                checkmateList.clear();
            }
            else
            {
                if (checkValidatorForOpponentMoveForBlack.isKingAsDraw())
                {
                    makeCheckMateSoundEffect();

                    timerForResign.stop();
                    timerForMove.stop();

                    Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                    Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);

                    int playerRating = player.getElo();
                    int opponentRating = opponent.getElo();

                    int k=EloCalculator.calculateDynamicK(player.getGamePlayed());

                    double actualScore = 0;

                    // Calculate expected scores
                    double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);

                    // Calculate new ratings
                    int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);

                    try
                    {
                        Transfer tempTransfer=new Transfer();
                        tempTransfer.user_ID=chessUI.user_ID;
                        tempTransfer.elo=newPlayerRating;

                        chessUI.client.execute("/Chess_Server/Update_Player_Elo",tempTransfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }

                    try
                    {
                        Transfer transfer=new Transfer();
                        transfer.user_ID=chessUI.user_ID;

                        chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                    }catch (Throwable t)
                    {
                        System.out.println("Member List : "+t);
                    }

                    chessUI.chessTopBar.isPlaying=false;
                    chessUI.chessTopBar.opponent_user_ID=0;
                    chessUI.chessTopBar.opponent_elo=0;
                    chessUI.chessTopBar.player_elo=0;

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
                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                chessUI.chessMenuBar.home.setDisable(false);
                        chessUI.chessMenuBar.social.setDisable(false);
                        chessUI.chessMenuBar.profile.setDisable(false);
                        chessUI.chessMenuBar.home.fire();
                    }
                    else
                    {
                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                        chessUI.chessPage.chessPane.getChildren().clear();
                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                chessUI.chessMenuBar.home.setDisable(false);
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

    void sendMoveToServer(int selectedRow,int selectedCol,int targetRow, int targetCol,PROMOTION promotion)
    {
        Move move=new Move();
        move.setFromPlayer(chessUI.user_ID);
        move.setToPlayer(chessUI.chessTopBar.opponent_user_ID);
        move.setFromX(7 - selectedRow);
        move.setFromY(7 - selectedCol);
        move.setToX(7 - targetRow);
        move.setToY(7 - targetCol);
        move.setPromotion(promotion);

        Transfer transfer=new Transfer();
        transfer.move=move;

        new Thread(()->
        {
            try
            {
                chessUI.client.execute("/Chess_Server/Submit_Move",transfer);
            }
            catch(Throwable ne)
            {
                System.out.println("sendMoveToServer : "+ne.getMessage());
            }
        }).start();
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

    private void makeTimerForMove()
    {
        this.timerForMove=new Timer(100, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.opponent_ID=chessUI.chessTopBar.opponent_user_ID;

                    java.util.List<Move> opponentMove = new LinkedList<>();
                    java.util.List<Object> objects = (java.util.List<Object>) chessUI.client.execute("/Chess_Server/Get_Move",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Move move = gson.fromJson(json, Move.class);
                            opponentMove.add(move);
                        }
                    }
                    if(opponentMove.size()!=0)
                    {
                        for(Move m:opponentMove)
                        {
                            if(m.getToPlayer()==chessUI.user_ID && m.getFromPlayer()==chessUI.chessTopBar.opponent_user_ID)
                            {
                                whiteSelectedRow=m.getFromX();
                                whiteSelectedCol=m.getFromY();
                                whiteTargetRow=m.getToX();
                                whiteTargetCol=m.getToY();

                                Platform.runLater(() ->
                                {
                                    BlackChessBoard.this.movePieceForOpponent(whiteSelectedRow,whiteSelectedCol,whiteTargetRow,whiteTargetCol,m.getPromotion());
                                });

                                opponentMove.clear();
                            }
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
    }

    private void makeTimerForResign()
    {
        this.timerForResign=new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    Transfer transfer=new Transfer();
                    transfer.user_ID=chessUI.user_ID;

                    java.util.List<Resign> resignList=new LinkedList<>();
                    java.util.List<Object> objects = (java.util.List<Object>) chessUI.client.execute("/Chess_Server/Get_Resign",transfer);
                    if (objects != null)
                    {
                        for (Object object : objects)
                        {
                            Gson gson = new Gson();
                            String json = gson.toJson(object);
                            Resign resign = gson.fromJson(json, Resign.class);
                            resignList.add(resign);
                        }
                    }
                    if(resignList.size()!=0)
                    {
                        for(Resign resign:resignList)
                        {
                            if(resign.getTo()==chessUI.user_ID && resign.getFrom()==chessUI.chessTopBar.opponent_user_ID && resign.getResignType()== RESIGN_TYPE.RESIGN_AS_BUTTON || resign.getTo()==chessUI.user_ID && resign.getFrom()==chessUI.chessTopBar.opponent_user_ID && resign.getResignType()== RESIGN_TYPE.RESIGN_AS_TIMEOUT)
                            {
                                timerForResign.stop();
                                timerForMove.stop();

                                Member player=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.user_ID);
                                Member opponent=chessUI.chessTopBar.getMemberDetailsUsingID(chessUI.chessTopBar.opponent_user_ID);

                                int playerRating = player.getElo();
                                int opponentRating = opponent.getElo();

                                int k=EloCalculator.calculateDynamicK(player.getGamePlayed());

                                double actualScore = 1;

                                // Calculate expected scores
                                double expectedPlayerScore = EloCalculator.calculateExpectedScore(playerRating, opponentRating);

                                // Calculate new ratings
                                int newPlayerRating = EloCalculator.calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);

                                try
                                {
                                    Transfer tempTransfer=new Transfer();
                                    tempTransfer.user_ID=chessUI.user_ID;
                                    tempTransfer.elo=newPlayerRating;

                                    chessUI.client.execute("/Chess_Server/Update_Player_Elo",tempTransfer);
                                }catch (Throwable t)
                                {
                                    System.out.println("Member List : "+t);
                                }

                                try
                                {
                                    transfer=new Transfer();
                                    transfer.user_ID=chessUI.user_ID;

                                    chessUI.client.execute("/Chess_Server/Update_Game_Played",transfer);
                                }catch (Throwable t)
                                {
                                    System.out.println("Member List : "+t);
                                }

                                try
                                {
                                    transfer=new Transfer();
                                    transfer.user_ID=chessUI.user_ID;
                                    chessUI.client.execute("/Chess_Server/Remove_Resign",transfer);
                                }catch (Throwable t)
                                {
                                    System.out.println("Member List : "+t);
                                }

                                chessUI.chessTopBar.isPlaying=false;
                                chessUI.chessTopBar.opponent_user_ID=0;
                                chessUI.chessTopBar.opponent_elo=0;
                                chessUI.chessTopBar.player_elo=0;

                                Platform.runLater(() ->
                                {
                                    if (resign.getResignType()==RESIGN_TYPE.RESIGN_AS_TIMEOUT)
                                    {
                                        chessUI.chessPage.resignAlertForOpponent.setContentText("Win By Time Out");
                                    }

                                    java.util.Optional<ButtonType> result = chessUI.chessPage.resignAlertForOpponent.showAndWait();

                                    if (result.isPresent() && result.get() == chessUI.chessPage.okResign)
                                    {
                                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                                        chessUI.chessPage.chessPane.getChildren().clear();
                                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                                        chessUI.chessMenuBar.home.setDisable(false);
                                        chessUI.chessMenuBar.social.setDisable(false);
                                        chessUI.chessMenuBar.profile.setDisable(false);
                                        chessUI.chessMenuBar.home.fire();
                                    }
                                    else
                                    {
                                        chessUI.chessPage.blackChessBoard=new BlackChessBoard(chessUI);
                                        AnchorPane.setTopAnchor(chessUI.chessPage.blackChessBoard,50.0);
                                        AnchorPane.setLeftAnchor(chessUI.chessPage.blackChessBoard,60.0);
                                        chessUI.chessPage.chessPane.getChildren().clear();
                                        chessUI.chessPage.chessPane.getChildren().addAll(chessUI.chessPage.inviteAndPlay,chessUI.chessPage.robotImageView,chessUI.chessPage.choiceBox,chessUI.chessPage.whiteChessButton,chessUI.chessPage.blackChessButton);                                        chessUI.chessMenuBar.home.setDisable(false);
                                        chessUI.chessMenuBar.social.setDisable(false);
                                        chessUI.chessMenuBar.profile.setDisable(false);
                                        chessUI.chessMenuBar.home.fire();
                                    }
                                });
                            }
                        }
                    }
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
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

}

class PawnMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;
    public int direction=-1;

    public PawnMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol) {
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

class RookMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;

    public RookMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

class KnightMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;

    public KnightMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

class BishopMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;

    public BishopMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

class QueenMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;

    public QueenMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

class KingMoveValidatorForBlack
{
    BlackChessBoard c;
    int targetRow, targetCol;

    public KingMoveValidatorForBlack(BlackChessBoard c, int targetRow, int targetCol)
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

            if (c.kingSideCastle && c.squares[c.selectedRow][1].getGraphic() == null && c.squares[c.selectedRow][2].getGraphic() == null && !isMoveIntoCheck(c.selectedRow, c.selectedCol - 1) && !isMoveIntoCheck(c.selectedRow, c.selectedCol - 2))
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.c.selectedRow=orignalRow;
                this.c.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForKingBoolean();

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

class ChessButtonActionListenerForBlack implements EventHandler
{
    private BlackChessBoard c;
    private int row;
    private int col;

    public ChessButtonActionListenerForBlack(BlackChessBoard c, int row, int col)
    {
        this.c = c;
        this.row = row;
        this.col = col;
    }

    public void handle(Event event)
    {
        CheckValidatorForOpponentMoveForBlack checkValidatorForOpponentMoveForBlack=new CheckValidatorForOpponentMoveForBlack(c);
        if (checkValidatorForOpponentMoveForBlack.isKingHasCheck())
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
                        PawnMoveValidatorForBlack p=new PawnMoveValidatorForBlack(c, row, col);
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
                            RookMoveValidatorForBlack r=new RookMoveValidatorForBlack(c, row, col);
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
                                KnightMoveValidatorForBlack k=new KnightMoveValidatorForBlack(c, row, col);
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
                                    BishopMoveValidatorForBlack b=new BishopMoveValidatorForBlack(c, row, col);
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
                                        QueenMoveValidatorForBlack q=new QueenMoveValidatorForBlack(c, row, col);
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
                                            KingMoveValidatorForBlack kn=new KingMoveValidatorForBlack(c, row, col);
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

        CheckValidatorForOpponentMoveForBlack checkValidatorForOpponentMoveForBlack=new CheckValidatorForOpponentMoveForBlack(c);

        boolean check=checkValidatorForOpponentMoveForBlack.isKingHasCheck();

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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.c,targetRow,targetCol);
                return pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();
            }
            else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.c,targetRow,targetCol);
                return rookMoveValidatorForBlack.isValidMoveForRookBoolean();
            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.c,targetRow,targetCol);
                return knightMoveValidatorForBlack.isValidMoveForKnightBoolean();
            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.c,targetRow,targetCol);
                return bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();
            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.c,targetRow,targetCol);
                return queenMoveValidatorForBlack.isValidMoveForQueenBoolean();
            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.c,targetRow,targetCol);
                return kingMoveValidatorForBlack.isValidMoveForKingBoolean();
            }
        }
        return false;
    }
}

class CheckValidatorForPlayerMoveForBlack
{
    BlackChessBoard board;
    public CheckValidatorForPlayerMoveForBlack(BlackChessBoard blackChessBoard)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("Black%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("Black%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForKingBoolean();

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
            PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.board,toRow,toCol);
            pawnMoveValidatorForBlack.direction=1;

            boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }else if (pieceType.contains("White%20Rook"))
        {
            RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Knight"))
        {
            KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Bishop"))
        {
            BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20Queen"))
        {
            QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("White%20King"))
        {
            KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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

        CheckValidatorForPlayerMoveForBlack checkValidatorForPlayerMoveForBlack=new CheckValidatorForPlayerMoveForBlack(board);

        boolean check=checkValidatorForPlayerMoveForBlack.isKingHasCheck();

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

class CheckValidatorForOpponentMoveForBlack
{
    BlackChessBoard board;
    public CheckValidatorForOpponentMoveForBlack(BlackChessBoard blackChessBoard)
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
                PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.board,toRow,toCol);
                pawnMoveValidatorForBlack.direction=1;

                boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }else if (pieceType.contains("White%20Rook"))
            {
                RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Knight"))
            {
                KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Bishop"))
            {
                BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20Queen"))
            {
                QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

                this.board.selectedRow=orignalRow;
                this.board.selectedCol=orignalCol;

                return returnType;

            }
            else if (pieceType.contains("White%20King"))
            {
                KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.board,toRow,toCol);

                boolean returnType = kingMoveValidatorForBlack.isValidMoveForWhiteKingBoolean();

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
            PawnMoveValidatorForBlack pawnMoveValidatorForBlack=new PawnMoveValidatorForBlack(this.board,toRow,toCol);
            boolean returnType= pawnMoveValidatorForBlack.isValidMoveForPawnBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }else if (pieceType.contains("Black%20Rook"))
        {
            RookMoveValidatorForBlack rookMoveValidatorForBlack=new RookMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = rookMoveValidatorForBlack.isValidMoveForRookBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Knight"))
        {
            KnightMoveValidatorForBlack knightMoveValidatorForBlack=new KnightMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = knightMoveValidatorForBlack.isValidMoveForKnightBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Bishop"))
        {
            BishopMoveValidatorForBlack bishopMoveValidatorForBlack=new BishopMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = bishopMoveValidatorForBlack.isValidMoveForBishopBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20Queen"))
        {
            QueenMoveValidatorForBlack queenMoveValidatorForBlack=new QueenMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = queenMoveValidatorForBlack.isValidMoveForQueenBoolean();

            this.board.selectedRow=-1;
            this.board.selectedCol=-1;

            return returnType;

        }
        else if (pieceType.contains("Black%20King"))
        {
            KingMoveValidatorForBlack kingMoveValidatorForBlack=new KingMoveValidatorForBlack(this.board,toRow,toCol);

            boolean returnType = kingMoveValidatorForBlack.isValidMoveForKingBoolean();

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

        CheckValidatorForOpponentMoveForBlack checkValidatorForOpponentMoveForBlack=new CheckValidatorForOpponentMoveForBlack(board);

        boolean check=checkValidatorForOpponentMoveForBlack.isKingHasCheck();

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