package com.chess.common;
import com.chess.common.ENUM.REQUEST_TYPE;
public class Request implements java.io.Serializable
{
    private int from;
    private int to;
    private REQUEST_TYPE requestType;

    public void setFrom(int from)
    {
        this.from = from;
    }
    public int getFrom()
    {
        return from;
    }
    public void setTo(int to)
    {
        this.to = to;
    }
    public int getTo()
    {
        return to;
    }
    public REQUEST_TYPE getRequestType() {
        return requestType;
    }
    public void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }
}
