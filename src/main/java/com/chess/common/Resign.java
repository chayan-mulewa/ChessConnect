package com.chess.common;
import com.chess.common.ENUM.RESIGN_TYPE;
public class Resign implements java.io.Serializable
{
    private int from;
    private int to;
    private RESIGN_TYPE resignType;

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
    public void setResignType(RESIGN_TYPE resignType)
    {
        this.resignType=resignType;
    }
    public RESIGN_TYPE getResignType()
    {
        return this.resignType;
    }
}
