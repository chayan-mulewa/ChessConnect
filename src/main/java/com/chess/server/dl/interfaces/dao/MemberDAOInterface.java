package com.chess.server.dl.interfaces.dao;
import java.util.*;
import com.chess.server.dl.dto.MemberDTO;
import com.chess.server.dl.exceptions.DAOException;
public interface MemberDAOInterface
{
    public List<MemberDTO> getAll() throws DAOException;
}
