package com.chess.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import com.chess.application.client.components.HomePage;
import com.google.gson.Gson;
import com.chess.common.*;
import com.chess.common.ENUM.GAME_TYPE;
import com.chess.common.ENUM.REQUEST_TYPE;
import com.chess.common.ENUM.INVITATION_TYPE;
import com.chess.framework.server.annotations.*;
import com.chess.server.dl.dao.MemberDAO;
import com.chess.server.dl.dao.SQLConnection;
import com.chess.server.dl.dto.MemberDTO;
import com.chess.server.dl.exceptions.DAOException;
import javax.swing.Timer;

@Path("/Chess_Server")
public class ChessServer
{
    static Map<String,Member> members;
    static Map<Integer,Member> membersID;
    static Map<Integer,List<Invitation>> invitations;
    static Map<Integer,List<Invitation>> acceptedInvitation;
    static Map<Integer,List<Invitation>> rejectedInvitation;
    static Map<Integer,List<Request>> requests;
    static Map<Integer,List<Integer>> pendingRequests;
    static Map<Integer,List<Move>> moves;
    static Map<Integer,List<Resign>> resigns;

    static Timer timerForMembers;
    static
    {
        populateDataStructure();
    }

    public ChessServer()
    {

    }

    public static void populateDataStructure()
    {
        members=new HashMap<>();
        membersID=new HashMap<>();
        timerForMembers=new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                    MemberDAO memberDAO=new MemberDAO();
                    List<MemberDTO> dlMembers=memberDAO.getAll();
                    Map<String,Member> tempMembers=new HashMap<>();
                    Map<Integer,Member> tempMembersID=new HashMap<>();
                    for(MemberDTO memberDTO:dlMembers)
                    {
                        Member member=new Member();
                        member.setUserID(memberDTO.getUserID());
                        member.setName(memberDTO.getName());
                        member.setEmail(memberDTO.getEmail());
                        member.setUsername(memberDTO.getUsername());
                        member.setPassword(memberDTO.getPassword());
                        member.setFriends(memberDTO.getFriends());
                        member.setGamePlayed(memberDTO.getGamePlayed());
                        member.setElo(memberDTO.getElo());
                        member.setProfile(memberDTO.getProfile());
                        tempMembers.put(member.getUsername(),member);


                        Member memberid=new Member();
                        memberid.setUserID(memberDTO.getUserID());
                        memberid.setName(memberDTO.getName());
                        memberid.setEmail(memberDTO.getEmail());
                        memberid.setUsername(memberDTO.getUsername());
                        memberid.setPassword(memberDTO.getPassword());
                        memberid.setFriends(memberDTO.getFriends());
                        memberid.setGamePlayed(memberDTO.getGamePlayed());
                        memberid.setElo(memberDTO.getElo());
                        memberid.setProfile(memberDTO.getProfile());
                        tempMembersID.put(memberid.getUserID(),memberid);
                    }
                    members.clear();
                    members=tempMembers;
                    membersID.clear();
                    membersID=tempMembersID;
                }catch (Throwable t)
                {
                    System.out.println("Member List : "+t);
                }
            }
        });
        timerForMembers.start();
        invitations=new HashMap<>();
        requests=new HashMap<>();
        pendingRequests=new HashMap<>();
        acceptedInvitation=new HashMap<>();
        rejectedInvitation=new HashMap<>();
        moves=new HashMap<>();
        resigns=new HashMap<>();
    }

    @Path("/Authenticate_Member")
    public boolean isUserAuthentic(Transfer transfer)
    {
        String username=transfer.username;
        String password= transfer.password;
        Member member=members.get(username);
        if(member==null)
        {
            return false;
        }
        String storedHashedPassword = member.getPassword();
        // Check if the provided password matches the stored hash
        if (BCrypt.checkpw(password, storedHashedPassword))
        {
            // Passwords match; allow the user to log in
            return true;
        }
        else
        {
            // Passwords do not match; deny access
            return false;
        }
    }

    @Path("/Is_Member_Exixts")
    public boolean isUserExists(Transfer transfer)
    {
        String username=transfer.username;
        Member member=members.get(username);
        if(member==null)
        {
            return false;
        }
        return true;
    }

    @Path("/Is_Email_Exixts")
    public boolean isEmailExists(Transfer transfer)
    {
        String email=transfer.email;
        for (String key:members.keySet())
        {
            Member member=members.get(key);
            if (member.getEmail().equals(email))
            {
                return true;
            }
        }
        return false;
    }

    @Path("/Create_Member")
    public void createMember(Transfer transfer)
    {
        String name=transfer.name;
        String email=transfer.email;
        String user=transfer.user;
        String pass=transfer.pass;
        byte[] profilePhotoData=transfer.profilePhotoData;
        String friend="{\"friends\": []}";

        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());

        if (profilePhotoData.length==0)
        {
            try
            {
                Connection connectionForUser = SQLConnection.getConnection();
                PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("INSERT INTO members (username, password, name, email, elo, gamePlayed, friendship) VALUES (?, ?, ?, ?, ?, ?, ?);");
                preparedStatementForUser.setString(1,user);
                preparedStatementForUser.setString(2,hashedPassword);
                preparedStatementForUser.setString(3,name);
                preparedStatementForUser.setString(4,email);
                preparedStatementForUser.setInt(5,800);
                preparedStatementForUser.setInt(6,0);
                preparedStatementForUser.setString(7,friend);
                preparedStatementForUser.executeUpdate();
            }
            catch (DAOException e)
            {
                throw new RuntimeException(e);
            }
            catch (SQLException e)
            {
                System.out.println("Failed To create member");
                throw new RuntimeException(e);
            }
        }
        else
        {
            try
            {
                Connection connectionForUser = SQLConnection.getConnection();
                PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("INSERT INTO members (username, password, name, email, profile, elo, gamePlayed, friendship) VALUES (?, ?, ?, ? ,?, ?, ?, ?);");
                preparedStatementForUser.setString(1,user);
                preparedStatementForUser.setString(2,hashedPassword);
                preparedStatementForUser.setString(3,name);
                preparedStatementForUser.setString(4,email);
                preparedStatementForUser.setBytes(5,profilePhotoData);
                preparedStatementForUser.setInt(6,800);
                preparedStatementForUser.setInt(7,0);
                preparedStatementForUser.setString(8,friend);
                preparedStatementForUser.executeUpdate();
            }
            catch (DAOException e)
            {
                throw new RuntimeException(e);
            }
            catch (SQLException e)
            {
                System.out.println("Failed To create member");
                throw new RuntimeException(e);
            }
        }

    }

    @Path("/Update_Username")
    public void updateUsername(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        String username=transfer.username;
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET username = ? WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,username);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Update_Email")
    public void updateEmail(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        String email=transfer.email;
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET email = ? WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,email);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }
    @Path("/Update_Name")
    public void updateName(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        String name=transfer.name;
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET name = ? WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,name);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Update_Password")
    public void updatePassword(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        String password=transfer.password;

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET password = ? WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,hashedPassword);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Update_Profile")
    public void updateProfile(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        byte[] profilePhotoData=transfer.profilePhotoData;

        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET profile = ? WHERE user_id = ? ;");
            preparedStatementForUser.setBytes(1,profilePhotoData);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Update_Game_Played")
    public void updateGamePlayed(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET gamePlayed = gamePlayed + 1 WHERE user_id = ? ;");
            preparedStatementForUser.setInt(1,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Update_Player_Elo")
    public void updatePlayerElo(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int elo=transfer.elo;
        try
        {
            Connection connectionForUser = SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET elo = ? WHERE user_id = ? ;");
            preparedStatementForUser.setInt(1,elo);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();
        }
        catch (DAOException e)
        {
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            System.out.println("Failed To create member");
            throw new RuntimeException(e);
        }

    }

    @Path("/Get_Member_Details")
    public Member getMemberDetails(Transfer transfer)
    {
        String username=transfer.username;
        Member tempMember=members.get(username);
        Member member=new Member();
        member.setUserID(tempMember.getUserID());
        member.setUsername(tempMember.getUsername());
        member.setName(tempMember.getName());
        member.setEmail(tempMember.getEmail());
        member.setFriends(tempMember.getFriends());
        member.setElo(tempMember.getElo());
        member.setGamePlayed(tempMember.getGamePlayed());
        member.setProfile(tempMember.getProfile());

        return member;
    }

    @Path("/Get_Member_Details_Using_ID")
    public Member getMemberDetailsUsingID(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        Member tempMember=membersID.get(user_ID);
        Member member=new Member();
        member.setUserID(tempMember.getUserID());
        member.setUsername(tempMember.getUsername());
        member.setName(tempMember.getName());
        member.setEmail(tempMember.getEmail());
        member.setFriends(tempMember.getFriends());
        member.setElo(tempMember.getElo());
        member.setGamePlayed(tempMember.getGamePlayed());
        member.setProfile(tempMember.getProfile());
        return member;
    }

    @Path("/Chess_Member")
    public List<Member> isMemberInChess(Transfer transfer)
    {
        String search=transfer.search;

        List<Member> existsMembers=new LinkedList<>();
        for (String key : members.keySet())
        {
            if (key.startsWith(search))
            {
                Member user=members.get(key);
                existsMembers.add(user);
            }
        }
        return existsMembers;
    }

    @Path("/Send_Request")
    public void sendRequest(Transfer transfer)
    {
        int from=transfer.user_ID;
        int to=transfer.opponent_ID;

        Request request=new Request();
        request.setFrom(from);
        request.setTo(to);
        request.setRequestType(REQUEST_TYPE.REQUEST);

        List<Request> tempRequests=requests.get(to);

        if(tempRequests==null)
        {
            tempRequests=new LinkedList<Request>();
            requests.put(to,tempRequests);
        }
        tempRequests.add(request);
    }

    @Path("/Set_Pending_Request")
    public void setPendingRequest(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        List<Integer> tempPendingRequests=pendingRequests.get(user_ID);
        if (tempPendingRequests==null)
        {
            tempPendingRequests=new LinkedList<>();
            pendingRequests.put(user_ID,tempPendingRequests);
        }
        tempPendingRequests.add(opponent_ID);
    }

    @Path("/Get_Pending_Requests")
    public List<Integer> getPendingRequests(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Integer> tempPendingRequests=pendingRequests.get(user_ID);
        if (tempPendingRequests==null)
        {
            pendingRequests.put(user_ID,new LinkedList<Integer>());
        }
        return tempPendingRequests;
    }

    @Path("/Remove_Pending_Request")
    public void removePendingRequest(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        if (pendingRequests.containsKey(opponent_ID))
        {
            List<Integer> tempPendingReuqests=pendingRequests.get(opponent_ID);
            if (tempPendingReuqests.size()!=0)
            {
                for (Integer pendingReuqest:tempPendingReuqests)
                {
                    if (pendingReuqest.equals(user_ID))
                    {
                        Integer integer=user_ID;
                        tempPendingReuqests.remove(integer);
                        break;
                    }
                }
            }
        }

    }

    @Path("/Get_Requests")
    public List<Request> getRequests(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Request> tempRequests=requests.get(user_ID);
        if(tempRequests==null )
        {
            requests.put(user_ID,new LinkedList<Request>());
        }
        return tempRequests;
    }

    @Path("/Request_Accepted")
    public void requestAccepted(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        String userString_ID=String.valueOf(user_ID);
        String opponentString_ID=String.valueOf(opponent_ID);

        if (requests.containsKey(user_ID))
        {
            List<Request> tempRequestList=requests.get(user_ID);
            if (tempRequestList.size()!=0)
            {
                for (Request request:tempRequestList)
                {
                    if (request.getFrom()==opponent_ID && request.getTo()==user_ID)
                    {
                        tempRequestList.remove(request);
                        break;
                    }
                }
            }
        }

        try
        {
            Connection connectionForUser= SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET friendship = JSON_ARRAY_APPEND (friendship,'$.friends', ? )WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,opponentString_ID);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();

            Connection connectionForOpponent=SQLConnection.getConnection();
            PreparedStatement preparedStatementOpponent=connectionForOpponent.prepareStatement("UPDATE members SET friendship = JSON_ARRAY_APPEND (friendship,'$.friends', ? )WHERE user_id = ? ;");
            preparedStatementOpponent.setString(1,userString_ID);
            preparedStatementOpponent.setInt(2,opponent_ID);
            preparedStatementOpponent.executeUpdate();

        }
        catch (DAOException daoException)
        {
            System.out.println("Failed To Add Friend");
        } catch (SQLException e)
        {
            System.out.println("Failed To Add Friend");
            throw new RuntimeException(e);
        }

    }

    @Path("/Request_Reject")
    public void requestReject(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        if (requests.containsKey(user_ID))
        {
            List<Request> tempRequestList=requests.get(user_ID);
            if (tempRequestList.size()!=0)
            {
                for (Request request:tempRequestList)
                {
                    if (request.getFrom()==opponent_ID && request.getTo()==user_ID)
                    {
                        tempRequestList.remove(request);
                        break;
                    }
                }
            }
        }

    }

    @Path("/Remove_Friend")
    public void removeFriend(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        String userString_ID=String.valueOf(user_ID);
        String opponentString_ID=String.valueOf(opponent_ID);

        try
        {
            Connection connectionForUser=SQLConnection.getConnection();
            PreparedStatement preparedStatementForUser=connectionForUser.prepareStatement("UPDATE members SET friendship = JSON_REMOVE(friendship, JSON_UNQUOTE(JSON_SEARCH(friendship, 'one', ? ))) WHERE user_id = ? ;");
            preparedStatementForUser.setString(1,opponentString_ID);
            preparedStatementForUser.setInt(2,user_ID);
            preparedStatementForUser.executeUpdate();

            Connection connectionForOpponent=SQLConnection.getConnection();
            PreparedStatement preparedStatementOpponent=connectionForOpponent.prepareStatement("UPDATE members SET friendship = JSON_REMOVE(friendship, JSON_UNQUOTE(JSON_SEARCH(friendship, 'one', ? ))) WHERE user_id = ? ;");
            preparedStatementOpponent.setString(1,userString_ID);
            preparedStatementOpponent.setInt(2,opponent_ID);
            preparedStatementOpponent.executeUpdate();

        }
        catch (DAOException daoException)
        {
            System.out.println("Failed To Add Friend");
        }
        catch (SQLException e)
        {
            System.out.println("Failed To Add Friend");
            throw new RuntimeException(e);
        }
    }


    @Path("/Send_Invitation")
    public void sendInvitation(Transfer transfer)
    {
        int from=transfer.user_ID;
        int to=transfer.opponent_ID;
        GAME_TYPE type=transfer.gameType;
        int time=transfer.time;

        Invitation invitation=new Invitation();
        invitation.setFrom(from);
        invitation.setTo(to);
        invitation.setType(INVITATION_TYPE.INVITATION);
        invitation.setGameType(type);
        invitation.setTime(time);

        List<Invitation> tempInvitations=invitations.get(to);

        if(tempInvitations==null)
        {
            tempInvitations=new LinkedList<Invitation>();
            invitations.put(to,tempInvitations);
        }

        tempInvitations.add(invitation);
    }

    @Path("/Get_Invitations")
    public List<Invitation> getInvitations(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Invitation> tempInvitations=invitations.get(user_ID);
        if(tempInvitations==null )
        {
            invitations.put(user_ID,new LinkedList<Invitation>());
        }
        return tempInvitations;
    }


    @Path("/Send_Accepted_Invitation")
    public void sendAcceptedInvitation(Transfer transfer)
    {
        int from=transfer.user_ID;
        int to=transfer.opponent_ID;
        GAME_TYPE gameType=transfer.gameType;
        int time=transfer.time;

        Invitation invitation=new Invitation();
        invitation.setFrom(from);
        invitation.setTo(to);
        invitation.setType(INVITATION_TYPE.INVITATION_ACCEPTED);
        invitation.setGameType(gameType);
        invitation.setTime(time);

        List<Invitation> tempInvitations=acceptedInvitation.get(to);

        if(tempInvitations==null)
        {
            tempInvitations=new LinkedList<Invitation>();
            acceptedInvitation.put(to,tempInvitations);
        }

        tempInvitations.add(invitation);
    }


    @Path("/Get_Accepted_Invitations")
    public List<Invitation> getAcceptedInvitations(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Invitation> tempInvitations=acceptedInvitation.get(user_ID);
        if(tempInvitations==null )
        {
            acceptedInvitation.put(user_ID,new LinkedList<Invitation>());
        }
        return tempInvitations;
    }

    @Path("/Remove_Accepted_Invitation")
    public void removeAcceptedInvitation(Transfer transfer)
    {
        int username=transfer.user_ID;
        int opponent=transfer.opponent_ID;

        if (acceptedInvitation.containsKey(username))
        {
            List<Invitation> tempInvitationList=acceptedInvitation.get(username);
            if (tempInvitationList.size()!=0)
            {
                for (Invitation invitation:tempInvitationList)
                {
                    if (invitation.getFrom()==opponent && invitation.getTo()==username)
                    {
                        tempInvitationList.remove(invitation);
                        break;
                    }
                }
            }
        }
    }

    @Path("/Send_Rejected_Invitation")
    public void sendRejectedInvitation(Transfer transfer)
    {
        int from=transfer.user_ID;
        int to=transfer.opponent_ID;

        Invitation invitation=new Invitation();
        invitation.setFrom(from);
        invitation.setTo(to);
        invitation.setType(INVITATION_TYPE.INVITATION_REJECTED);

        List<Invitation> tempInvitations=rejectedInvitation.get(to);

        if(tempInvitations==null)
        {
            tempInvitations=new LinkedList<Invitation>();
            rejectedInvitation.put(to,tempInvitations);
        }

        tempInvitations.add(invitation);
    }

    @Path("/Get_Rejected_Invitations")
    public List<Invitation> getRejectedInvitations(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Invitation> tempInvitations=rejectedInvitation.get(user_ID);
        if(tempInvitations==null )
        {
            rejectedInvitation.put(user_ID,new LinkedList<Invitation>());
        }
        return tempInvitations;
    }

    @Path("/Remove_Rejected_Invitation")
    public void removeRejectedInvitatio(Transfer transfer)
    {
        int username=transfer.user_ID;
        int opponent=transfer.opponent_ID;

        if (rejectedInvitation.containsKey(username))
        {
            List<Invitation> tempInvitationList=rejectedInvitation.get(username);
            if (tempInvitationList.size()!=0)
            {
                for (Invitation invitation:tempInvitationList)
                {
                    if (invitation.getFrom()==opponent && invitation.getTo()==username)
                    {
                        tempInvitationList.remove(invitation);
                        break;
                    }
                }
            }
        }
    }

    @Path("/Reject_Invitation")
    public void removeInvitation(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        if (invitations.containsKey(user_ID))
        {
            List<Invitation> tempInvitationList=invitations.get(user_ID);
            if (tempInvitationList.size()!=0)
            {
                for (Invitation invitation:tempInvitationList)
                {
                    if (invitation.getFrom()==opponent_ID && invitation.getTo()==user_ID)
                    {
                        tempInvitationList.remove(invitation);
                        break;
                    }
                }
            }
        }
    }


    @Path("/Reject_All_Invitations_Because_One_Invitation_Is_Accepted")
    public void rejectAllInvitation(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        int opponent_ID=transfer.opponent_ID;

        if (invitations.containsKey(user_ID))
        {
            List<Invitation> tempInvitationList=invitations.get(user_ID);
            if (tempInvitationList.size()!=0)
            {
                for (Invitation invitation:tempInvitationList)
                {
                    if (invitation.getFrom()==opponent_ID && invitation.getTo()==user_ID)
                    {
                        tempInvitationList.remove(invitation);
                        break;
                    }
                }
                for (Invitation invitation:tempInvitationList)
                {
                    Transfer tempTransfer=new Transfer();
                    tempTransfer.user_ID=invitation.getTo();
                    tempTransfer.opponent_ID=invitation.getFrom();
                    sendRejectedInvitation(tempTransfer);
                }
                tempInvitationList.clear();
            }
        }
    }


    @Path("/Get_Friends")
    public List<Friend> getFriends(Transfer transfer)
    {
        int user_ID=transfer.user_ID;
        Member friend=membersID.get(user_ID);
        List<Friend> friendsList=friend.getFriends();
        return  friendsList;
    }

    @Path("/Submit_Move")
    public void submitMove(Transfer transfer)
    {
        Move getOpponentsMove=transfer.move;
        Gson gson = new Gson();
        String json = gson.toJson(getOpponentsMove);
        Move move = gson.fromJson(json, Move.class);
        List<Move> m=moves.get(move.getFromPlayer());
        if(m==null)
        {
            m=new LinkedList<Move>();
            moves.put(move.getFromPlayer(),m);
        }
        m.add(move);
    }

    @Path("/Get_Move")
    public List<Move> getOpponentsMove(Transfer transfer)
    {
        int opponent_ID=transfer.opponent_ID;

        List<Move> m=moves.get(opponent_ID);
        if(m!=null && m.size()>0)
        {
            moves.put(opponent_ID,new LinkedList<Move>());
        }
        return m;
    }

    @Path("/Send_Resign")
    public void sendResign(Transfer transfer)
    {
        Resign getResign=transfer.resign;
        Gson gson = new Gson();
        String json = gson.toJson(getResign);
        Resign resign = gson.fromJson(json, Resign.class);
        List<Resign> resignList=resigns.get(resign.getTo());
        if(resignList==null)
        {
            resignList=new LinkedList<Resign>();
            resigns.put(resign.getTo(),resignList);
        }
        resignList.add(resign);
    }

    @Path("/Get_Resign")
    public List<Resign> getResign(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        List<Resign> resignList=resigns.get(user_ID);
        if(resignList!=null && resignList.size()>0)
        {
            resigns.put(user_ID,new LinkedList<Resign>());
        }
        return resignList;
    }

    @Path("/Remove_Resign")
    public void removeResign(Transfer transfer)
    {
        int user_ID=transfer.user_ID;

        if (resigns.containsKey(user_ID))
        {
            List<Resign> tempResignList=resigns.get(user_ID);
            tempResignList.clear();
        }
    }


}
