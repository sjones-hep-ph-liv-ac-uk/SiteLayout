package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeType;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.AccessObject;

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
                NodeSet.refreshListOfNodeSets(request,"nodeSetName","ASC");
            } catch (ModelException e) {
                logger.error("Error refreshing ... ", e);
                request.setAttribute("TheMessage", e.getMessage());
                request.setAttribute("TheJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/nodeset.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("New");
        if (act != null) {

            ArrayList<String> cl = new ArrayList<String>();
            ArrayList<String> nt = new ArrayList<String>();
            try {
                cl = Cluster.listAllClusterNames(request);
                nt = NodeType.listAllNodeTypes(request);

            } catch (ModelException e) {
                logger.error("Error getting the clusters and node types, ", e);
                request.setAttribute("TheMessage", e.getMessage());
                request.setAttribute("TheJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            }
            request.setAttribute("clusterList", cl);
            request.setAttribute("nodeTypeList", nt);
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
                String sortCmd = key.substring(4, key.length()).trim().replaceAll("\\.[xy]$","");
                String c = "";
                if (sortCmd.startsWith("UP.")) {
                    order = "ASC";
                    c = sortCmd.substring(3, sortCmd.length()).trim();
                }
                else {
                    order = "DESC";
                    c = sortCmd.substring(5, sortCmd.length()).trim();
                }
                
                try {
                    NodeSet.refreshListOfNodeSets(request, c,order);
                } catch (ModelException e) {
                    request.setAttribute("TheMessage", e.getMessage());
                    request.setAttribute("TheJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            
            if (key.startsWith("DEL.")) {
                String nodeSet = key.substring(4, key.length() - 1);
                try {
                    NodeSet.deleteNodeSet(request, nodeSet);
                } catch (ModelException e) {
                    logger.error("Error deleting a node set, ", e);
                    request.setAttribute("TheMessage", e.getMessage());
                    request.setAttribute("TheJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    NodeSet.refreshListOfNodeSets(request,"nodeSetName","ASC");
                } catch (ModelException e) {
                    logger.error("Model error when trying refreshListOfNodeSets, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String nodeSet = key.substring(3, key.length() - 1);
                try {
                    HttpSession session = request.getSession();
                    AccessObject ao = (AccessObject) session.getAttribute("AccessObject");
                    if (ao != null) {
                        ResultSet r;
                        String sql = "select  nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet where"
                                + " nodeSetName = '" + nodeSet + "'";

                        r = ao.query(sql);
                        NodeSet n = null;
                        while (r.next()) {
                            n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"),
                                    r.getInt("nodeCount"), r.getString("cluster"));
                        }
                        request.setAttribute("nodeSet", n);
                        ArrayList<String> cl = new ArrayList<String>();
                        ArrayList<String> nt = new ArrayList<String>();
                        try {
                            cl = Cluster.listAllClusterNames(request);
                            nt = NodeType.listAllNodeTypes(request);

                        } catch (ModelException e) {
                            logger.error("Error getting the clusters and node types, ", e);
                            request.setAttribute("TheMessage", e.getMessage());
                            request.setAttribute("TheJsp", "main_screen.jsp");
                            rd = request.getRequestDispatcher("/recoverable_message.jsp");
                            rd.forward(request, response);
                            return;
                        }
                        request.setAttribute("clusterList", cl);
                        request.setAttribute("nodeTypeList", nt);
                    }
                } catch (SQLException e) {
                    logger.error("Error with this node set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
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
