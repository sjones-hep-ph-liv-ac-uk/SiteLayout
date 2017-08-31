package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeSet;

public interface NodeSetDao {

    void updateNodeSet(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#readNodeSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#readNodeSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    List<NodeSet> readNodeSetList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#readOneNodeSet(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#readOneNodeSet(org.hibernate.Session, java.lang.String)
     */
    NodeSet readOneNodeSet(Session hibSession, String nodeSetName);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#addNodeSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#addNodeSet(javax.servlet.http.HttpServletRequest)
     */
    void addNodeSet(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#deleteNodeSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#deleteNodeSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#loadIndexedNodeSet(javax.servlet.http.HttpServletRequest, java.lang.Integer)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeSetDao#loadIndexedNodeSet(javax.servlet.http.HttpServletRequest, java.lang.Integer)
     */
    void loadIndexedNodeSet(HttpServletRequest request, Integer nodeSetIndex) throws WTFException, RoutineException;

    void loadNodeSets(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}