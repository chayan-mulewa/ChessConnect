package com.chess.common;
import com.chess.common.ENUM.GAME_TYPE;
import com.chess.common.ENUM.INVITATION_TYPE;
public class Invitation implements java.io.Serializable
{
    private int from;
    private int to;
    private INVITATION_TYPE type;
    private GAME_TYPE gameType;
    private int time;

    public void setFrom(int from)
    {
        this.from=from;
    }
    public int getFrom()
    {
        return this.from;
    }
    public void setTo(int to)
    {
        this.to=to;
    }
    public int getTo()
    {
        return this.to;
    }
    public void setType(INVITATION_TYPE type)
    {
        this.type=type;
    }
    public INVITATION_TYPE getType()
    {
        return this.type;
    }
    public void setGameType(GAME_TYPE gameType)
    {
        this.gameType=gameType;
    }
    public GAME_TYPE getGameType()
    {
        return this.gameType;
    }
    public void setTime(int time)
    {
        this.time=time;
    }
    public int getTime()
    {
        return this.time;
    }
}
