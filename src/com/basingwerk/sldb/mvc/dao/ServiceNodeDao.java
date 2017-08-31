package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.ServiceNode;

public interface ServiceNodeDao {

    void updateServiceNode(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readOneServiceNode(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readOneServiceNode(org.hibernate.Session, java.lang.String)
     */
    ServiceNode readOneServiceNode(Session hibSession, String hostname);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readServiceNodeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readServiceNodeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    List<ServiceNode> readServiceNodeList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#addServiceNode(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#addServiceNode(javax.servlet.http.HttpServletRequest)
     */
    void addServiceNode(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#deleteServiceNode(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#deleteServiceNode(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    void deleteServiceNode(HttpServletRequest request, String hostname) throws WTFException, RoutineException;

    void loadIndexedServiceNode(HttpServletRequest request, Integer serviceNodeIndex)
            throws WTFException, RoutineException;

    void loadServiceNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}