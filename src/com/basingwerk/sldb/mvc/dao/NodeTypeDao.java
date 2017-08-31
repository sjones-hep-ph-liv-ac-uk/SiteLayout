package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeType;

public interface NodeTypeDao {

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#updateNodeType(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#updateNodeType(javax.servlet.http.HttpServletRequest)
     */
    void updateNodeType(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readNodeTypeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readNodeTypeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    List<NodeType> readNodeTypeList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readOneNodeType(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readOneNodeType(org.hibernate.Session, java.lang.String)
     */
    NodeType readOneNodeType(Session hibSession, String nodeTypeName);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#addNodeType(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#addNodeType(javax.servlet.http.HttpServletRequest)
     */
    void addNodeType(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#deleteNodeType(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#deleteNodeType(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, RoutineException;

    void loadIndexedNodeType(HttpServletRequest request, Integer nodeTypeIndex) throws WTFException, RoutineException;

    void loadNodeTypes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void setBaselineNodeType(HttpServletRequest request) throws RoutineException, WTFException;

}