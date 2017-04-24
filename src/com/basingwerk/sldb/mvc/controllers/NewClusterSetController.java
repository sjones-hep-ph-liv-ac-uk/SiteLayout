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
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
import com.basingwerk.sldb.mvc.model.ClusterSet;

@WebServlet("/NewClusterSetController")

public class NewClusterSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewClusterSetController.class);

    public NewClusterSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        try {
            ClusterSet.addClusterSet(request);
        } catch (ModelException e) {
            if (e instanceof ModelExceptionRollbackWorked) {
                logger.info("A rollback worked.");
                request.setAttribute("theMessage", "The cluster set could not be added. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! Rollback failed.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }

        ArrayList<ClusterSet> clusterSetList = null;
        try {
            clusterSetList = ClusterSet.queryClusterSetList(request);
        } catch (ModelException e1) {
            if (e1 instanceof ModelExceptionRollbackWorked) {
                logger.info("Rollback worked.");
                request.setAttribute("theMessage", "Could not update that cluster set at this time. Please try again.");
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

        request.setAttribute("clusterSetList", clusterSetList);
        String next = "/cluster_set.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
