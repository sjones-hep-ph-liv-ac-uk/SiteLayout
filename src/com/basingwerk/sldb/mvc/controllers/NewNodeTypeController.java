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
import com.basingwerk.sldb.mvc.model.NodeType;

@WebServlet("/NewNodeTypeController")

public class NewNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeTypeController.class);

    public NewNodeTypeController() {
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

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerSlot = request.getParameter("memPerSlot");

        String sqlCommand = "INSERT INTO nodeType (nodeTypeName,cpu,slot,hs06PerSlot,memPerSlot) VALUES ('"
                + nodeTypeName + "','" + cpu + "','" + slot + "','" + hs06PerSlot + "','" + memPerSlot + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = ao.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();

        } catch (SQLException ex) {
            logger.info("Could not add new nodeType, rolling back.");
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex1) {
                logger.error("Rollback failed, ", ex1);
            }
            request.setAttribute("TheMessage", "Problem while adding a node type. Please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();

            ResultSet r = ao.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerSlot from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerSlot"));
                nodeTypeList.add(n);

            }
            request.setAttribute("nodeTypeList", nodeTypeList);
            String next = "/nodetype.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (SQLException e) {
            logger.error("Error while adding a node type. ", e);
        }
        return;

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
