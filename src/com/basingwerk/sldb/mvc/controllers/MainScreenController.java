package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.basingwerk.sldb.mvc.model.Cluster;

import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

@WebServlet("/MainScreenController")

public class MainScreenController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(MainScreenController.class);

    public MainScreenController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        try {

            String act = request.getParameter("SomeButton");
            if (act == null) {
                logger.error("Error when looking at SomeButton.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            String next = "";
            if (act.equals("Edit node types")) {
                NodeType.refreshListOfNodeTypes(request,"nodeTypeName","ASC");
                next = "/nodetype.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (act.equals("Edit clusters")) {
                Cluster.refreshListOfClusters(request,"clusterName","ASC");
                next = "/cluster.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Edit node sets")) {
                NodeSet.refreshListOfNodeSets(request,"nodeSetName","ASC");
                next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            if (act.equals("Reports")) {
                NodeType.refreshListOfNodeTypes(request,"nodeTypeName","ASC");
                Cluster.refreshListOfClusters(request,"clusterName","ASC");
                NodeType.setBaselineNodeType(request);
                ArrayList<String> clusters = Cluster.listAllClusterNames(request);
                java.util.HashMap<String, ArrayList> joinMap = new java.util.HashMap<String, ArrayList>();
                Iterator<String> c = clusters.iterator();
                while (c.hasNext()) {
                    String cluster = c.next();
                    ArrayList<NodeSetNodeTypeJoin> nsntj = NodeSetNodeTypeJoin.getJoinForCluster(request, cluster);
                    joinMap.put(cluster, nsntj);
                }
                request.setAttribute("joinMap", joinMap);
                next = "/reports.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            logger.error("Error due to strange input.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (ModelException e) {
            logger.error("Had a ModelException in the MainScreen, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
