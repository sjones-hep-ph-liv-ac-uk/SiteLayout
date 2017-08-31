package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterImpl;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
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
import com.basingwerk.sldb.mvc.dao.*;

@WebServlet("/ClusterSetController")

public class ClusterSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(ClusterSetController.class);

    public ClusterSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

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
                ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                clusterSetDao.loadClusterSets(request, "clusterSetName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using loadClusterSets");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/cluster_set.jsp");
            rd.forward(request, response);
            return;
        }
        
        act = request.getParameter("New");
        if (act != null) {
            rd = request.getRequestDispatcher("/new_cluster_set.jsp");
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

                    ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                    clusterSetDao.loadClusterSets(request, c, order);
                } catch (WTFException e) {
                    logger.error("WTF! Error while using loadClusterSets");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("DEL.")) {
                String clusterSet = key.substring(4, key.length());
                try {
                    ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                    clusterSetDao.deleteClusterSet(request, clusterSet);
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not update that cluster set at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error while using deleteClusterSet");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                    clusterSetDao.loadClusterSets(request, "clusterSetName", order);
                } catch (WTFException e) {
                    logger.error("WTF! Error while using loadClusterSets");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String clusterSet = key.substring(3, key.length());
                Integer index = Integer.parseInt(clusterSet);

                try {
                    ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                    clusterSetDao.loadIndexedClusterSet(request, index);
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not edit that clusterSet at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error while editing a cluster set");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/edit_cluster_set.jsp";
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
