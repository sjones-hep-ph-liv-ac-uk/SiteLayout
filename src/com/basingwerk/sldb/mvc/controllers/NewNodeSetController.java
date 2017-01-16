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
import com.basingwerk.sldb.mvc.model.NodeSet;

/**
 * Servlet implementation class NewNodeSetController
 */
@WebServlet("/NewNodeSetController")
public class NewNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeSetController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewNodeSetController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        String clusterName = request.getParameter("clusterList");
        String nodeTypeName = request.getParameter("nodeTypeList");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String sqlCommand = "INSERT INTO nodeSet (nodeSetName ,nodeTypeName, nodeCount, cluster) VALUES" + "('"
                + nodeSetName + "','" + nodeTypeName + "'," + nodeCount + ",'" + clusterName + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = dbHolder.theConnection.createStatement();
            result = statement.executeUpdate(sqlCommand);
            dbHolder.theConnection.commit();
        } catch (SQLException e) {
            logger.error("Error inserting a node set.",e);
            try {
                logger.info("could not add new node set, rolling back.");
                dbHolder.theConnection.rollback();
            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
            }
            logger.error("Error while adding a node set, " , e);
            request.setAttribute("TheMessage", "It did not work. Perhaps that node set exists already?");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

            ResultSet r = dbHolder.query("select nodeSetName ,nodeTypeName, nodeCount, cluster from nodeSet");
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
            logger.error("Error while adding a node set, " , e);
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
