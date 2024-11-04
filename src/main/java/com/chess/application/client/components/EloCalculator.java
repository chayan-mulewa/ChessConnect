package com.chess.application.client.components;

public class EloCalculator
{
    public static double calculateExpectedScore(int playerRating, int opponentRating)
    {
        return 1.0 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
    }

    public static int calculateNewRating(int playerRating, double expectedScore, double actualScore, double k)
    {
        return (int) (playerRating + k * (actualScore - expectedScore));
    }

    public static int calculateDynamicK(int totalGamesPlayed)
    {
        if (totalGamesPlayed <= 10) {
            // High k for new players
            return 30;
        }
        else if (totalGamesPlayed <= 20)
        {
            // Moderate k for intermediate players
            return 20;
        }
        else
        {
            // Low k for experienced players
            return 10;
        }
    }
}
