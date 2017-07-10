package com.basingwerk.sldb.mvc.controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;
import com.basingwerk.sldb.mvc.exceptions.DbFacadeException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.NodeType;

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
        act = request.getParameter("New");
        if (act != null) {

            ArrayList<String> s = new ArrayList<String>();
            try {
//                s = DbFacade.listCl u s t erSetNames(request);
              DbFacade.refreshClusterSets(request, "clusterSetName", "ASC");
            } catch (HibernateException e) {
                logger.error("WTF! Error getting the cluster sets, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
//            request.setAttribute("clusterSetList", s);
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
                    DbFacade.refreshClusters(request, c, order);
                } catch (HibernateException e) {
                    logger.error("WTF! A HibernateException occurred, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
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
                logger.error("cluster:" + cluster + ":");
                try {

                    DbFacade.deleteCluster(request, cluster);
                } catch (HibernateException e1) {
                    request.setAttribute("theMessage", "Could not delete that cluster at this time. Please try again.");
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (DbFacadeException e) {
                    logger.error("WTF! Error using deleteCluster, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                try {
                    DbFacade.refreshClusters(request, "clusterName", "ASC");
                } catch (HibernateException e) {
                    logger.error("WTF! Had a HibernateException when trying to setListOfClusters, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
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
                try {

                    ArrayList<String> s = new ArrayList<String>();
                    DbFacade.setSingleCluster(request, cluster);
                    DbFacade.refreshClusterSets(request, "clusterSetName", "ASC");
//                    s = DbFacade.li s t C l u sterSetNames(request);
//                    request.setAttribute("cl u s t e r S etList", s);
                } catch (HibernateException e) {
                    logger.error("WTF! A HibernateException occurred, ", e);
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
