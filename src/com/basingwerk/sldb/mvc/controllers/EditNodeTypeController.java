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

import com.basingwerk.sldb.mvc.model.NodeType;
import com.basingwerk.sldb.mvc.model.AccessObject;

/**
 * Servlet implementation class EditNodeTypeController
 */

@WebServlet("/EditNodeTypeController")
public class EditNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeTypeController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditNodeTypeController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

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

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        String sqlCommand = "UPDATE nodeType set cpu='" + cpu + "', slot='" + slot + "', hs06PerSlot='" + hs06PerSlot
                + "', memPerNode='" + memPerNode + "' where nodeTypeName='" + nodeTypeName + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = dbHolder.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            dbHolder.getTheConnection().commit();
        } catch (SQLException e) {
            try {
                logger.info ("Could not update node type, rolling back.");
                dbHolder.getTheConnection().rollback();
            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
            }
            logger.error("Error while editing a node type. " , e);
            request.setAttribute("TheMessage", "Did not save changes. Please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeType.refreshListOfNodeTypes(request);
            String next = "/nodetype.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error("Error when trying to refreshListOfNodeTypes, " , e );
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
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
