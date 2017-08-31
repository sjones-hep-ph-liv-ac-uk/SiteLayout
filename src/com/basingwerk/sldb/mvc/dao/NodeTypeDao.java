package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.NodeType;

public interface NodeTypeDao {

    void updateNodeType(HttpServletRequest request) throws WTFException, RoutineException;

    List<NodeType> readNodeTypeList(Session hibSession, String col, String order);

    NodeType readOneNodeType(Session hibSession, String nodeTypeName);

    void addNodeType(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, RoutineException;

    void loadIndexedNodeType(HttpServletRequest request, Integer nodeTypeIndex) throws WTFException, RoutineException;

    void loadNodeTypes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void setBaselineNodeType(HttpServletRequest request) throws RoutineException, WTFException;

}