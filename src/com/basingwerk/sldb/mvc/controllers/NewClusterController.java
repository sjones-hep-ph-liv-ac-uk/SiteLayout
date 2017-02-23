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

@WebServlet("/NewClusterController")

public class NewClusterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewClusterController.class);

    public NewClusterController() {
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
        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String sqlCommand = "INSERT INTO cluster (clusterName, Descr) VALUES ('" + clusterName + "','" + descr + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = ao.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();
        } catch (SQLException e) {
            logger.info("Could not add new cluster, rolling back.");
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed, ", ex);
            }
            request.setAttribute("TheMessage", "Sorry. Failed to add that cluster. Please try again.");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
            ResultSet r = ao.query("select clusterName,descr from cluster");
            while (r.next()) {
                Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"));
                clusterList.add(c);
            }
            request.setAttribute("clusterList", clusterList);
            String next = "/cluster.jsp";

            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
        } catch (SQLException e) {
            logger.error("Error while adding a cluster. ", e);
        }
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
