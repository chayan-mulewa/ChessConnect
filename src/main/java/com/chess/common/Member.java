package com.chess.common;

import java.util.List;

public class Member implements java.io.Serializable
{
    private int userID;
    private String name;
    private String email;
    private String username;
    private String password;
    private int elo;
    private int gamePlayed;
    private List<Friend> frineds;
    private byte[] profile;

    public void setUserID(int userID)
    {
        this.userID=userID;
    }
    public int getUserID()
    {
        return this.userID;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getEmail()
    {
        return this.email;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public String getUsername()
    {
        return this.username;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getPassword()
    {
        return this.password;
    }
    public void setFriends(List<Friend> friends)
    {
        this.frineds=friends;
    }
    public List<Friend> getFriends()
    {
        return this.frineds;
    }
    public void setGamePlayed(int gamePlayed)
    {
        this.gamePlayed=gamePlayed;
    }
    public int getGamePlayed()
    {
        return this.gamePlayed;
    }
    public void setElo(int elo)
    {
        this.elo=elo;
    }
    public int getElo()
    {
        return this.elo;
    }
    public void setProfile(byte[] profile)
    {
        this.profile=profile;
    }
    public byte[] getProfile()
    {
        return this.profile;
    }
}