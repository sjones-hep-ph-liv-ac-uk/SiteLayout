package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.DataAccessObject;

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
            DataAccessObject dao = DataAccessObject.getInstance();
            
            String act = request.getParameter("SomeButton");
            if (act == null) {
                logger.error("WTF! Null found when looking at SomeButton.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            String next = "";
            if (act.equals("Edit cluster sets")) {
                dao.loadClusterSets(request, "clusterSetName", "ASC");
                next = "/cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (act.equals("Edit node types")) {
                dao.loadNodeTypes(request, "nodeTypeName", "ASC");
                next = "/nodetype.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (act.equals("Edit clusters")) {
                dao.loadClusters(request, "clusterName", "ASC");
                next = "/cluster.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Edit node sets")) {
                dao.loadNodeSets(request, "nodeSetName", "ASC");
                next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            if (act.equals("Edit nodes")) {
                dao.loadNodes(request, "nodeName", "ASC");
                next = "/node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            if (act.equals("Reports")) {
                dao.loadClusterSets(request, "clusterSetName", "ASC");
                next = "/select_cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            logger.error("WTF! Never seen that button before.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (WTFException e) {
            logger.error("WTF! Error using MainScreenController, ", e);
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
