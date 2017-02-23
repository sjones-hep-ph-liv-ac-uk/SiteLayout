package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.NodeSet;

@WebServlet("/NewNodeSetController")

public class NewNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeSetController.class);

    public NewNodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AccessObject ao = null;
        RequestDispatcher rd = null;

        HttpSession session = request.getSession();
        ao = (AccessObject) session.getAttribute("AccessObject");
        if (ao == null) {
            logger.error("Error connecting to the database.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        String clusterName = request.getParameter("clusterList");
        String nodeTypeName = request.getParameter("nodeTypeList");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String sqlCommand = "INSERT INTO nodeSet (nodeSetName ,nodeTypeName, nodeCount, cluster) VALUES" + "('"
                + nodeSetName + "','" + nodeTypeName + "'," + nodeCount + ",'" + clusterName + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = ao.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not add new node set, rolling back.");
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex1) {
                logger.error("Rollback failed, ", ex1);
            }
            request.setAttribute("TheMessage", "Problem while adding a node set, please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

            ResultSet r = ao.query("select nodeSetName ,nodeTypeName, nodeCount, cluster from nodeSet");
            while (r.next()) {
                NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
                nodeSetList.add(n);

            }
            request.setAttribute("nodeSetList", nodeSetList);
            String next = "/nodeset.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (SQLException e) {
            logger.error("Error while adding a node set, ", e);
        }
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
