package com.chess.common;

import com.chess.common.ENUM.GAME_TYPE;

public class Transfer implements java.io.Serializable
{
    public String search;

    public String member;

    public String username;
    public String password;

    public int user_ID;
    public int opponent_ID;
    public int time;

    public  Move move;

    public Resign resign;

    public GAME_TYPE gameType;

    public String name;
    public String email;
    public String user;
    public String pass;
    public byte[] profilePhotoData;
    public int elo;
}
