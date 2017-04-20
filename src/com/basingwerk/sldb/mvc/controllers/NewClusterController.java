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
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackFailed;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
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
        ao = (AccessObject) session.getAttribute("accessObject");
        if (ao == null) {
            logger.error("Error when trying to connect to database.");
            request.setAttribute("theMessage", "No access to database. You can try to login again.");
            request.setAttribute("theJsp", "login.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            Cluster.addCluster(request);
        } catch (ModelException e1) {
            if (e1 instanceof ModelExceptionRollbackWorked) {
                logger.info("Rollback worked.");
                request.setAttribute("theMessage", "Could not add that cluster at this time. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! failed to roll back, ", e1);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }

        try {
            ArrayList<Cluster> clusterList = Cluster.queryCluster(request);
            request.setAttribute("clusterList", clusterList);
            String next = "/cluster.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
            return;
        } catch (ModelException e1) {
            if (e1 instanceof ModelExceptionRollbackWorked) {
                logger.info("Rollback worked.");
                request.setAttribute("theMessage", "Could not get the cluster at this time. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! failed to roll back, ", e1);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
