package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.NodeSet;

@WebServlet("/EditNodeSetController")

public class EditNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeSetController.class);

    public EditNodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccessObject ao = null;
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        ao = (AccessObject) session.getAttribute("AccessObject");
        if (ao == null) {
            logger.error("Could not connect to database.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        String sqlCommand = "UPDATE nodeSet SET nodeSetName='" + nodeSetName + "', nodeTypeName='" + nodeTypeName
                + "', nodeCount='" + nodeCount + "', cluster='" + clusterName + "' where nodeSetName='" + nodeSetName
                + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = ao.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not edit that node set, rollback", ex);
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex2) {
                logger.error("Rollback failed, ", ex2);
            }
            request.setAttribute("TheMessage", "Sorry, failed to save that edit. Please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeSet.refreshListOfNodeSets(request,"nodeSetName","ASC");
            String next = "/nodeset.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (Exception ex) {
            logger.error("Problem when trying to refreshListOfNodeSets, ", ex);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
