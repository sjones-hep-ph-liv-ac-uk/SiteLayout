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

/**
 * Servlet implementation class EditNodeSetController
 */
@WebServlet("/EditNodeSetController")
public class EditNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeSetController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditNodeSetController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccessObject dbHolder = null;
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        dbHolder = (AccessObject) session.getAttribute("AccessObject");
        if (dbHolder == null) {
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
            statement = dbHolder.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            dbHolder.getTheConnection().commit();
        } catch (SQLException e) {
            logger.error("Error while editing a node set" , e);
            try {
                logger.info("Could not update node set, rolling back.");
                dbHolder.getTheConnection().rollback();
            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
            }
            request.setAttribute("TheMessage", "Failed to save edit. Try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeSet.refreshListOfNodeSets(request);
            String next = "/nodeset.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error("Had an Exception when trying to refreshListOfNodeSets, " , e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        return;

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
