package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.HostSystem;

public interface HostSystemDao {

    List<HostSystem> readHostSystemList(Session hibSession, String col, String order);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.HostSystemDao#readOneHostSystem(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.HostSystemDao#readOneHostSystem(org.hibernate.Session, java.lang.String)
     */
    HostSystem readOneHostSystem(Session hibSession, String hostname);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.HostSystemDao#loadHostSystems(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.HostSystemDao#loadHostSystems(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    void loadHostSystems(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}