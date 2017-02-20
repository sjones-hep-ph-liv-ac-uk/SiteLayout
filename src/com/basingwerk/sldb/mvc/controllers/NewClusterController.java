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

import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.AccessObject;

/**
 * Servlet implementation class NewClusterController
 */
@WebServlet("/NewClusterController")
public class NewClusterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewClusterController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewClusterController() {
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
            logger.error("Error connecting to the database.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String sqlCommand = "INSERT INTO cluster (clusterName, Descr) VALUES ('" + clusterName + "','" + descr + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = dbHolder.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            dbHolder.getTheConnection().commit();
        } catch (SQLException e) {
            try {
                logger.info("Could not add new cluster, rolling back.");              
                dbHolder.getTheConnection().rollback();
            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
            }
            logger.error("Error while adding a cluster. " , e);
            request.setAttribute("TheMessage", "It did not work. Perhaps that cluster exists already?");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
            ResultSet r = dbHolder.query("select clusterName,descr from cluster");
            while (r.next()) {
                Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"));
                clusterList.add(c);
            }
            request.setAttribute("clusterList", clusterList);
            String next = "/cluster.jsp";

            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (SQLException e) {
            logger.error("Error while adding a cluster. " , e);
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
