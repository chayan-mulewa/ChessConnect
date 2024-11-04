package com.chess.common;

import com.chess.common.ENUM.PROMOTION;

public class Move implements java.io.Serializable
{
    private int fromPlayer;
    private int toPlayer;
    private PROMOTION promotion;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public void setFromPlayer(int fromPlayer)
    {
        this.fromPlayer=fromPlayer;
    }
    public int getFromPlayer()
    {
        return this.fromPlayer;
    }
    public void setToPlayer(int toPlayer)
    {
        this.toPlayer=toPlayer;
    }
    public int getToPlayer()
    {
        return this.toPlayer;
    }
    public void setPromotion(PROMOTION promotion)
    {
        this.promotion=promotion;
    }
    public PROMOTION getPromotion()
    {
        return this.promotion;
    }
    public void setFromX(int fromX)
    {
        this.fromX=fromX;
    }
    public int getFromX()
    {
        return this.fromX;
    }
    public void setFromY(int fromY)
    {
        this.fromY=fromY;
    }
    public int getFromY()
    {
        return this.fromY;
    }
    public void setToX(int toX)
    {
        this.toX=toX;
    }
    public int getToX()
    {
        return this.toX;
    }
    public void setToY(int toY)
    {
        this.toY=toY;
    }
    public int getToY()
    {
        return this.toY;
    }
}
