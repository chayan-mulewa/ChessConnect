package com.chess.application.client.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class ChessGame {
    public static void main(String[] args) {
        try {
            // Specify the full path to the Stockfish binary
            String stockfishPath = "src\\main\\java\\com\\chess\\stockfish\\stockfish"; // Update this with the actual path

            // Start Stockfish engine as a process
            Process stockfishProcess = Runtime.getRuntime().exec(stockfishPath);

            // Connect to Stockfish's input and output streams
            PrintWriter stockfishIn = new PrintWriter(new OutputStreamWriter(stockfishProcess.getOutputStream()), true);
            BufferedReader stockfishOut = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));

            // Send UCI commands to Stockfish to initialize the engine
            stockfishIn.println("uci");
            stockfishIn.println("isready");
            stockfishIn.println("ucinewgame");

            // Main game loop
            while (true) {
                // You can implement your white moves here
                String yourMove = "e2e4"; // Example: Pawn to e4
                stockfishIn.println("position startpos moves " + yourMove);

                // Ask Stockfish for its move
                stockfishIn.println("go depth 1"); // You can change the depth as needed

                // Read Stockfish's response (Stockfish's move)
                String stockfishMove = stockfishOut.readLine();
                if (stockfishMove.startsWith("bestmove")) {
                    System.out.println("Stockfish's move: " + stockfishMove.split(" ")[1]);
                }

                // You can implement your code to make the corresponding black move here

                // Delay for demonstration purposes
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

