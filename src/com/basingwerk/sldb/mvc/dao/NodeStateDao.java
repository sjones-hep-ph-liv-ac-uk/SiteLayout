package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeState;

public interface NodeStateDao {

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readNodeStateList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readNodeStateList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    List<NodeState> readNodeStateList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readOneNodeState(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readOneNodeState(org.hibernate.Session, java.lang.String)
     */
    NodeState readOneNodeState(Session hibSession, String nodeStateName);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#loadNodeStates(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#loadNodeStates(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    void loadNodeStates(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}