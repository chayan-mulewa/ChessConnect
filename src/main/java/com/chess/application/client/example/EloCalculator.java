package com.chess.application.client.example;

public class EloCalculator {
    public static double calculateExpectedScore(int playerRating, int opponentRating) {
        return 1.0 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
    }

    public static int calculateNewRating(int playerRating, double expectedScore, double actualScore, double k) {
        return (int) (playerRating + k * (actualScore - expectedScore));
    }

    public static void main(String[] args) {
        int playerRating = 700; // Initial rating for the player
        int opponentRating = 700; // Initial rating for the opponent
        double k = 32; // Weight of the game (adjust as needed)

        // Simulate a game outcome (0 for loss, 0.5 for draw, 1 for win)
        double actualScore = 1.0;

        // Calculate expected scores
        double expectedPlayerScore = calculateExpectedScore(playerRating, opponentRating);
        double expectedOpponentScore = calculateExpectedScore(opponentRating, playerRating);

        // Calculate new ratings
        int newPlayerRating = calculateNewRating(playerRating, expectedPlayerScore, actualScore, k);
        int newOpponentRating = calculateNewRating(opponentRating, expectedOpponentScore, 1.0 - actualScore, k);

        // Update ratings
        playerRating = newPlayerRating;
        opponentRating = newOpponentRating;

        // Display updated ratings
        System.out.println("Player's New Rating: " + playerRating);
        System.out.println("Opponent's New Rating: " + opponentRating);
    }
}
