package com.chess.server.main;

import com.chess.framework.server.*;

import com.chess.server.ChessServer;

public class Main
{
    public static void main(String[] args)
    {
        ChessServer chessServer=new ChessServer();
        NFrameworkServer server=new NFrameworkServer();
        server.registerClass(ChessServer.class);
        server.start();
    }   
}
