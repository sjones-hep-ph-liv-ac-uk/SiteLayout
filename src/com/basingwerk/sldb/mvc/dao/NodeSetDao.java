package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeSet;

public interface NodeSetDao {

    void updateNodeSet(HttpServletRequest request) throws WTFException, RoutineException;

    List<NodeSet> readNodeSetList(Session hibSession, String col, String order);

    NodeSet readOneNodeSet(Session hibSession, String nodeSetName);

    void addNodeSet(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, RoutineException;

    void loadIndexedNodeSet(HttpServletRequest request, Integer nodeSetIndex) throws WTFException, RoutineException;

    void loadNodeSets(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}