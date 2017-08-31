package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterSetDao;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;
import com.basingwerk.sldb.mvc.dao.InstallationDao;
import com.basingwerk.sldb.mvc.dao.InstallationImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

@WebServlet("/SelectReportController")

public class SelectReportController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(SelectReportController.class);

    public SelectReportController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

        try {
            
            String act = request.getParameter("SomeButton");
            if (act == null) {
                logger.error("WTF! Null found when looking at SomeButton.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            String next = "";
            
            if (act.equals("Worker node, cluster and node type report")) {
                ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                clusterSetDao.loadClusterSets(request, "clusterSetName", "ASC");
                next = "/select_cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            
            if (act.equals("Service node and services report")) {
                InstallationDao installationDao = (InstallationDao) request.getSession().getAttribute("installationDao"); 
                installationDao.loadInstallations(request, "serviceNode", "ASC");
                next = "/services_report.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            
            logger.error("WTF! Never seen that button before.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (WTFException e) {
            logger.error("WTF! Error using SelectReportController, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
