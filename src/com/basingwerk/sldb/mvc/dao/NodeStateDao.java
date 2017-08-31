package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeState;

public interface NodeStateDao {

    List<NodeState> readNodeStateList(Session hibSession, String col, String order);

    NodeState readOneNodeState(Session hibSession, String nodeStateName);

    void loadNodeStates(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}