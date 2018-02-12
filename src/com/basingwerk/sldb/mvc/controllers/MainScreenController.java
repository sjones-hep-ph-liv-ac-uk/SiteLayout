package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterDao;
import com.basingwerk.sldb.mvc.dao.ClusterImpl;
import com.basingwerk.sldb.mvc.dao.ClusterSetDao;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;
import com.basingwerk.sldb.mvc.dao.InstallationDao;
import com.basingwerk.sldb.mvc.dao.InstallationImpl;
import com.basingwerk.sldb.mvc.dao.NodeDao;
import com.basingwerk.sldb.mvc.dao.NodeImpl;
import com.basingwerk.sldb.mvc.dao.NodeSetDao;
import com.basingwerk.sldb.mvc.dao.NodeSetImpl;
import com.basingwerk.sldb.mvc.dao.NodeTypeDao;
import com.basingwerk.sldb.mvc.dao.NodeTypeImpl;
import com.basingwerk.sldb.mvc.dao.ServiceNodeDao;
import com.basingwerk.sldb.mvc.dao.ServiceNodeImpl;
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

@WebServlet("/MainScreenController")

public class MainScreenController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(MainScreenController.class);

    public MainScreenController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        try {
            
            String act = request.getParameter("SomeButton");
            if (act == null) {
                logger.error("WTF! Null found when looking at SomeButton.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            String next = "";
            if (act.equals("Edit cluster sets")) {
                ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
                if (clusterSetDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterSetDao.loadClusterSets(request, "clusterSetName", "ASC");
                next = "/cluster_set.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (act.equals("Edit node types")) {
                NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
                if (nodeTypeDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                nodeTypeDao.loadNodeTypes(request, "nodeTypeName", "ASC");
                next = "/nodetype.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (act.equals("Edit clusters")) {
                ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                if (clusterDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterDao.loadClusters(request, "clusterName", "ASC");
                next = "/cluster.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Edit node sets")) {
                NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");            
                if (nodeSetDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
                next = "/nodeset.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Edit service nodes")) {
                ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao");            
                if (serviceNodeDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                serviceNodeDao.loadServiceNodes(request, "hostname", "ASC");
                next = "/service_node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Edit service installations")) {
                InstallationDao installationDao = (InstallationDao) request.getSession().getAttribute("installationDao"); 
                if (installationDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                installationDao.loadInstallations(request, "serviceNode", "ASC");
                next = "/installation.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (act.equals("Reports")) {
                next = "/select_report.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            if (act.equals("Logout")) {
                LoginController.logout(request);
                next = "/login.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;

            }
            
            logger.error("WTF! Never seen that button before.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (WTFException e) {
            logger.error("WTF! Error using MainScreenController, ", e);
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
