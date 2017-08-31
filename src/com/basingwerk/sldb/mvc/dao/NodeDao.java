package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Node;

public interface NodeDao {

    void updateNode(HttpServletRequest request) throws WTFException, RoutineException;

    List<Node> readNodeList(Session hibSession, String col, String order);

    Node readOneNode(Session hibSession, String nodeName);

    void addNode(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteNode(HttpServletRequest request, String nodeName) throws WTFException, RoutineException;

    void loadIndexedNode(HttpServletRequest request, Integer nodeIndex) throws WTFException, RoutineException;

    void loadNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void toggleCheckedNodes(HttpServletRequest request) throws RoutineException, WTFException;

    void toggleIndexedNode(HttpServletRequest request, Integer nodeIndex) throws RoutineException, WTFException;

}