package com.chess.server.dl.dao;

import java.sql.*;
import java.util.*;
import com.chess.common.Friend;
import com.chess.server.dl.dto.MemberDTO;
import com.chess.server.dl.exceptions.DAOException;
import com.chess.server.dl.interfaces.dao.MemberDAOInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MemberDAO implements MemberDAOInterface,java.io.Serializable
{
    public List<MemberDTO> getAll() throws DAOException
    {
        List<MemberDTO> memberDTOs=new LinkedList<>();
        try
        {
            Connection connection=SQLConnection.getConnection();
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from members");
            while(resultSet.next())
            {
                MemberDTO memberDTO=new MemberDTO();
                memberDTO.setUserID(resultSet.getInt("user_id"));
                memberDTO.setName(resultSet.getString("name"));
                memberDTO.setEmail(resultSet.getString("email"));
                memberDTO.setUsername(resultSet.getString("username"));
                memberDTO.setPassword(resultSet.getString("password"));
                memberDTO.setGamePlayed(resultSet.getInt("gamePlayed"));
                memberDTO.setElo(resultSet.getInt("elo"));
                memberDTO.setProfile(resultSet.getBytes("profile"));

                JsonObject jsonObject = JsonParser.parseString(resultSet.getString("friendship")).getAsJsonObject();
                // Extract the "friends" array
                JsonArray friendsArray = jsonObject.getAsJsonArray("friends");
                // Create a list to store Friends objects
                List<Friend> friendsList = new ArrayList<>();
                // Iterate through the array elements and convert them to Friends objects
                for (int i = 0; i < friendsArray.size(); i++)
                {
                    Friend friend = new Friend();
                    friend.setID(friendsArray.get(i).getAsInt());
                    friendsList.add(friend);
                }

                memberDTO.setFriends(friendsList);
                memberDTOs.add(memberDTO);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return memberDTOs;
        }catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage());
        }
    }
}
