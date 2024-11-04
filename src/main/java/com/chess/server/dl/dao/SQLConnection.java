package com.chess.server.dl.dao;
import com.chess.server.dl.exceptions.DAOException;

import java.sql.*;
public class SQLConnection
{
    private SQLConnection()
    {

    }
    public static Connection getConnection() throws DAOException
    {
        Connection connection=null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/chess_db","root","root");
            
        }catch (Exception exception)
        {
            throw new DAOException(exception.getMessage());
        }
        return connection;
    }
}