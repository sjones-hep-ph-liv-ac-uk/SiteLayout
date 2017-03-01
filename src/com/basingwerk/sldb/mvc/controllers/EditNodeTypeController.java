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

@WebServlet("/EditNodeTypeController")

public class EditNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeTypeController.class);

    public EditNodeTypeController() {
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

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerSlot = request.getParameter("memPerSlot");

        String sqlCommand = "UPDATE nodeType set cpu='" + cpu + "', slot='" + slot + "', hs06PerSlot='" + hs06PerSlot
                + "', memPerSlot='" + memPerSlot + "' where nodeTypeName='" + nodeTypeName + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = ao.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not update node type, rolling back.");
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex1) {
                logger.error("Rollback failed, ", ex1);
            }
            request.setAttribute("TheMessage", "Sorry. Failed to save changes. Please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeType.refreshListOfNodeTypes(request,"nodeTypeName","ASC");
            String next = "/nodetype.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error("Error when trying to refreshListOfNodeTypes, ", e);
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
