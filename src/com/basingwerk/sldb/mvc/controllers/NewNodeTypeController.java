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

import com.basingwerk.sldb.mvc.model.DBConnectionHolder;
import com.basingwerk.sldb.mvc.model.NodeType;

/**
 * Servlet implementation class NewNodeTypeController
 */
@WebServlet("/NewNodeTypeController")
public class NewNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeTypeController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewNodeTypeController() {
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

        DBConnectionHolder dbHolder = null;
        RequestDispatcher rd = null;

        HttpSession session = request.getSession();
        dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");
        if (dbHolder == null) {
            logger.error("Error connecting to the database.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        String sqlCommand = "INSERT INTO nodeType (nodeTypeName,cpu,slot,hs06PerSlot,memPerNode) VALUES ('"
                + nodeTypeName + "','" + cpu + "','" + slot + "','" + hs06PerSlot + "','" + memPerNode + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = dbHolder.theConnection.createStatement();
            result = statement.executeUpdate(sqlCommand);
            dbHolder.theConnection.commit();

        } catch (SQLException e) {
            try {
                logger.info("Could not add new nodeType, rolling back.");
                dbHolder.theConnection.rollback();
            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
            }

            logger.error("Error while adding a node type. " , e);
            request.setAttribute("TheMessage", "It did not work. Perhaps that node type exists already?");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();

            ResultSet r = dbHolder.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nodeTypeList.add(n);

            }
            request.setAttribute("nodeTypeList", nodeTypeList);
            String next = "/nodetype.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (SQLException e) {
            logger.error("Error while adding a node type. " , e);
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
