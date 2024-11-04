package com.chess.framework.common;
import com.chess.common.Transfer;
public class Request implements java.io.Serializable
{
    private String servicePath;
    private Transfer argument;
    public void setServicePath(String servicePath)
    {
        this.servicePath=servicePath;
    }
    public String getServicePath()
    {
        return this.servicePath;
    }
    public void setArguments(Transfer argument)
    {
        this.argument=argument;
    }
    public Transfer getArguments()
    {
        return this.argument;
    }
}