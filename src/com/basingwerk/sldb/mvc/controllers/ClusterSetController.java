package com.basingwerk.sldb.mvc.controllers;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.dbfacade.DbFacade;
import com.basingwerk.sldb.mvc.exceptions.DbFacadeException;
import com.basingwerk.sldb.mvc.exceptions.ModelException;
import com.basingwerk.sldb.mvc.model.ClusterSet;

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
        String act = null;
        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
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

                    DbFacade.refreshClusterSets(request, c, order);
                } catch (HibernateException e) {
                    logger.error("WFT! ModelException when trying to refresh cluster set, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
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
                    DbFacade.deleteClusterSet(request, clusterSet);
                } catch (HibernateException e1) {
                    request.setAttribute("theMessage",
                            "Could not update that cluster set at this time. Please try again.");
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (DbFacadeException e) {
                    logger.error("WTF! Error using deleteClusterSet, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    DbFacade.refreshClusterSets(request, "ClusterSetName", order);
                } catch (HibernateException e) {
                    logger.error("WFT! HibernateException when trying to refresh cluster sets, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
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

                ClusterSet cs = null;
                try {
                    cs = DbFacade.queryOneClusterSet(request, clusterSet);
                } catch (HibernateException e1) {
                    logger.error("WTF! Error using queryOneClusterSet, ", e1);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                request.setAttribute("clusterSet", cs);
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
