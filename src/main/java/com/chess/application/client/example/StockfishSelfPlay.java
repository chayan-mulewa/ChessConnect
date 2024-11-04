package com.chess.application.client.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class StockfishSelfPlay {
    public static void main(String[] args) {
        try {
            Process stockfishProcess = Runtime.getRuntime().exec("src\\main\\java\\com\\chess\\stockfish\\stockfish"); // Specify the path to Stockfish binary

            BufferedReader stockfishIn = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            PrintWriter stockfishOut = new PrintWriter(new OutputStreamWriter(stockfishProcess.getOutputStream()), true);

            // Send commands to Stockfish
            stockfishOut.println("uci");
            stockfishOut.println("isready");
            stockfishOut.println("ucinewgame");
            stockfishOut.println("position startpos moves e2e4 e7e5"); // Example moves

            // Start the game loop
            while (true) {
                stockfishOut.println("go movetime 1000"); // Set your desired move time (in milliseconds)

                String line;
                while ((line = stockfishIn.readLine()) != null) {
                    System.out.println(line);

                    if (line.startsWith("bestmove")) {
                        String bestMove = line.split(" ")[1];
                        System.out.println("Stockfish suggests move: " + bestMove);

                        // You can send this move to your chessboard and update the game state

                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

