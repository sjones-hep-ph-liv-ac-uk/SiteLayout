package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.ServiceNode;

public interface ServiceNodeDao {

    void updateServiceNode(HttpServletRequest request) throws WTFException, RoutineException;

    ServiceNode readOneServiceNode(Session hibSession, String hostname);

    List<ServiceNode> readServiceNodeList(Session hibSession, String col, String order);

    void addServiceNode(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteServiceNode(HttpServletRequest request, String hostname) throws WTFException, RoutineException;

    void loadIndexedServiceNode(HttpServletRequest request, Integer serviceNodeIndex)
            throws WTFException, RoutineException;

    void loadServiceNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}