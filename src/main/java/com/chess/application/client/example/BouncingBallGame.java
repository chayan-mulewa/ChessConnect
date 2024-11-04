package com.chess.application.client.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BouncingBallGame extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BALL_RADIUS = 20;
    private static final double GRAVITY = 0.2;
    private List<Ball> balls = new ArrayList<>();
    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }
    public void makeMoveSoundEffect()
    {
        try
        {
            URL soundUrl = getClass().getResource("/com/chess/application/client/components/ChessPage/Chess Sound/ball.wav");
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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bouncing Ball Game");
        Pane root = new Pane();
        root.setStyle("-fx-background-color : #1e1e1b");
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Clear the canvas
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                // Update and draw all the balls
                List<Ball> newBalls = new ArrayList<>();
                for (Ball ball : balls) {
                    ball.update();
                    ball.draw(gc);
                    if (ball.getY() + BALL_RADIUS >= HEIGHT) {
                        newBalls.add(new Ball());
                        makeMoveSoundEffect();
                    }
                }
                balls.addAll(newBalls);

                // Remove balls that are out of the screen
                balls.removeIf(ball -> ball.getY() > HEIGHT);
            }
        };

        // Initialize the game with a single ball
        balls.add(new Ball());

        timer.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class Ball {
        private double x;
        private double y;
        private double speedY;
        private Color color;

        public Ball() {
            x = random.nextDouble() * (WIDTH - 2 * BALL_RADIUS) + BALL_RADIUS;
            y = 0;
            speedY = random.nextDouble() * 5 + 5;
            color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        public double getY() {
            return y;
        }

        public void update() {
            y += speedY;
            speedY += GRAVITY;

            if (y + BALL_RADIUS > HEIGHT) {
                y = HEIGHT - BALL_RADIUS;
                speedY *= -0.8; // Bounce off the bottom with some energy loss
            }
        }

        public void draw(GraphicsContext gc) {
            gc.setFill(color);
            gc.fillOval(x - BALL_RADIUS, y - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
        }
    }
}
