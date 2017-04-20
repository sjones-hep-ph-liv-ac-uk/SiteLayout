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
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
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
                NodeSet.refreshListOfNodeSets(request, "nodeSetName", "ASC");
            } catch (ModelException e) {
                logger.error("WTF! ModelException while refreshing the list of node sets, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
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
                logger.error("WTF! ModelException when preparing data, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
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
                    NodeSet.refreshListOfNodeSets(request, c, order);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when trying to refresh node sets, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("DEL.")) {
                String nodeSet = key.substring(4, key.length());
                try {
                    NodeSet.deleteNodeSet(request, nodeSet);
                } catch (ModelException e) {
                    logger.error("Error deleting a node set, ", e);
                    logger.error("WTF! ModelException when deleteing a node set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    NodeSet.refreshListOfNodeSets(request, "nodeSetName", "ASC");
                } catch (ModelException e) {
                    logger.error("WTF! Model error when trying refreshListOfNodeSets, ", e);
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
                String nodeSetName = key.substring(3, key.length());
                NodeSet ns;
                try {
                    ns = NodeSet.queryOneNodeSet(request, nodeSetName);
                } catch (ModelException e) {
                    if (e instanceof ModelExceptionRollbackWorked) {
                        logger.info("A rollback worked.");
                        request.setAttribute("theMessage",
                                "Could not read one node set at this time. Please try again.");
                        request.setAttribute("theJsp", "main_screen.jsp");
                        rd = request.getRequestDispatcher("/recoverable_message.jsp");
                        rd.forward(request, response);
                        return;
                    } else {
                        logger.error("WTF! Rollback failed.");
                        rd = request.getRequestDispatcher("/error.jsp");
                        rd.forward(request, response);
                        return;
                    }
                }
                request.setAttribute("nodeSet", ns);
                ArrayList<String> cl = new ArrayList<String>();
                ArrayList<String> nt = new ArrayList<String>();
                try {
                    cl = Cluster.listAllClusterNames(request);
                    nt = NodeType.listAllNodeTypes(request);

                } catch (ModelException e) {
                    logger.error("WTF! ModelException when preparing data, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                request.setAttribute("clusterList", cl);
                request.setAttribute("nodeTypeList", nt);

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
