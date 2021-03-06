package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterDao;
import com.basingwerk.sldb.mvc.dao.ClusterImpl;
import com.basingwerk.sldb.mvc.dao.NodeDao;
import com.basingwerk.sldb.mvc.dao.NodeSetDao;
import com.basingwerk.sldb.mvc.dao.NodeSetImpl;
import com.basingwerk.sldb.mvc.dao.NodeTypeDao;
import com.basingwerk.sldb.mvc.dao.NodeTypeImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

@WebServlet("/NodeSetController")

public class NodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NodeSetController.class);

    public NodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        String act = null;

        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        act = request.getParameter("Refresh");
        if (act != null) {
            try {
                NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
                if (nodeSetDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                
                nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using refreshNodeSets");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/nodeset.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("New");
        if (act != null) {

            try {
                ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                if (clusterDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterDao.loadClusters(request, "clusterName", "ASC");
                NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
                if (nodeTypeDao  == null)
                    throw new WTFException("Session timed out. Log back in.");

                nodeTypeDao.loadNodeTypes(request, "nodeTypeName", "ASC");

            } catch (WTFException e) {
                logger.error("WTF! Error preparing data, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/new_nodeset.jsp");
            rd.forward(request, response);
            return;
        }

        Map params = request.getParameterMap();
        Iterator i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();

            String order = "ASC";
            if (key.startsWith("SORT")) {
                String sortCmd = key.substring(4, key.length()).trim().replaceAll("\\.[xy]$", "");
                String c = "";
                if (sortCmd.startsWith("UP.")) {
                    order = "ASC";
                    c = sortCmd.substring(3, sortCmd.length()).trim();
                } else {
                    order = "DESC";
                    c = sortCmd.substring(5, sortCmd.length()).trim();
                }

                try {
                    NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
                    if (nodeSetDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    nodeSetDao.loadNodeSets(request, c, order);
                } catch (WTFException e) {
                    logger.error("WTF! Error using refreshNodeSets, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("DEL.")) {
                String nodeSetName = key.substring(4, key.length());
                try {
                    NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
                    if (nodeSetDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    nodeSetDao.deleteNodeSet(request, nodeSetName);
                } catch (RoutineException e) {
                    logger.error("WTF! Error deleting a node set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error deleting a node set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
                    if (nodeSetDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error using refreshNodeSets.");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("NODES.")) {
                String nodeSetName = key.substring(6, key.length());
                try {
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
                    if (nodeDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    
                    nodeDao.loadNodesOfNodeSet(request, nodeSetName, "nodeName", "ASC");
                    HttpSession httpSession = request.getSession();
                    httpSession.setAttribute("nodeSetName", nodeSetName);                    
                    rd = request.getRequestDispatcher("/node.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not view that data at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error deleting a node set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }                
            }
            
            if (key.startsWith("ED.")) {
                String nodeSet = key.substring(3, key.length());
                Integer index = Integer.parseInt(nodeSet);
                try {
                    NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
                    if (nodeSetDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    nodeSetDao.loadIndexedNodeSet(request, index);
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not edit that nodeSet at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error using queryOneNodeSet");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.loadClusters(request, "clusterName", "ASC");
                    NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
                    if (nodeTypeDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    nodeTypeDao.loadNodeTypes(request, "nodeTypeName", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error preparing data, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }

                String next = "/edit_nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
