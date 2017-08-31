package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.ClusterSet;

public interface ClusterSetDao {

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#updateClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#updateClusterSet(javax.servlet.http.HttpServletRequest)
     */
    void updateClusterSet(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readClusterSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readClusterSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    List<ClusterSet> readClusterSetList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readOneClusterSet(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readOneClusterSet(org.hibernate.Session, java.lang.String)
     */
    ClusterSet readOneClusterSet(Session hibSession, String clusterSetName);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#addClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#addClusterSet(javax.servlet.http.HttpServletRequest)
     */
    void addClusterSet(HttpServletRequest request) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#deleteClusterSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#deleteClusterSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    void deleteClusterSet(HttpServletRequest request, String clusterSetName) throws WTFException, RoutineException;

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadClusterSets(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadClusterSets(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    void loadClusterSets(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void loadIndexedClusterSet(HttpServletRequest request, Integer clusterSetIndex)
            throws WTFException, RoutineException;

}