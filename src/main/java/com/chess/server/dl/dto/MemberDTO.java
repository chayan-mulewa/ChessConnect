package com.chess.server.dl.dto;

import com.chess.common.Friend;
import com.chess.server.dl.interfaces.dto.MemberDTOInterface;

import java.util.List;

public class MemberDTO implements java.io.Serializable, MemberDTOInterface
{
    private int userID;
    private String name;
    private String email;
    private String username;
    private String password;
    private int gamePlayed;
    private int elo;
    private List<Friend> friendsList;
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
    public void setFriends(List<Friend> friendsList)
    {
        this.friendsList=friendsList;
    }
    public List<Friend> getFriends()
    {
        return this.friendsList;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getPassword()
    {
        return this.password;
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