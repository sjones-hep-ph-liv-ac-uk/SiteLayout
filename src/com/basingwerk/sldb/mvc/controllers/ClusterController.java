package com.basingwerk.sldb.mvc.controllers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.dao.*;

@WebServlet("/ClusterController")

public class ClusterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(ClusterController.class);

    public ClusterController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        String act = null;

        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        act = request.getParameter("Refresh");
        if (act != null) {
            try {
                ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");  
                if (clusterDao == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterDao.loadClusters(request, "clusterName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using loadClusters");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/cluster.jsp");
            rd.forward(request, response);
            return;
        }
        
        act = request.getParameter("New");
        if (act != null) {

            try {
                ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");  
                if (clusterSetDao == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterSetDao.loadClusterSets(request, "clusterSetName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using loadClusterSets, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/new_cluster.jsp");
            rd.forward(request, response);
            return;
        }

        Map params = request.getParameterMap();
        Iterator i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();

            String order = "ASC";
            if (key.startsWith("SORT")) {
                String sortCmd = key.substring(4, key.length()).trim().replaceAll("\\.[xy]$", "");
                String c = "";
                if (sortCmd.startsWith("UP.")) {
                    order = "ASC";
                    c = sortCmd.substring(3, sortCmd.length()).trim();
                } else {
                    order = "DESC";
                    c = sortCmd.substring(5, sortCmd.length()).trim();
                }

                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.loadClusters(request, c, order);
                } catch (WTFException e) {
                    logger.error("WTF! Error while using loadClusters");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/cluster.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("DEL.")) {
                String cluster = key.substring(4, key.length());
                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.deleteCluster(request, cluster);
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not delete that cluster at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error using deleteCluster, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.loadClusters(request, "clusterName", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error while using loadClusters");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/cluster.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String cluster = key.substring(3, key.length());
                Integer index = Integer.parseInt(cluster);
                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.loadIndexedCluster(request, index);
                    ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                    if (clusterSetDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterSetDao.loadClusterSets(request, "clusterSetName", "ASC");
                } catch (RoutineException e) {

                    request.setAttribute("theMessage",
                            "Could not edit that cluster at this time. Please try again." + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error while editing a cluster");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/edit_cluster.jsp";
                rd = request.getRequestDispatcher(next);
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
