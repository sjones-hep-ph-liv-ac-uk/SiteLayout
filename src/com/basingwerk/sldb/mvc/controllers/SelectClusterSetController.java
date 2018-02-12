package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterDao;
import com.basingwerk.sldb.mvc.dao.ClusterImpl;
import com.basingwerk.sldb.mvc.dao.NodeTypeDao;
import com.basingwerk.sldb.mvc.dao.NodeTypeImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

@WebServlet("/SelectClusterSetController")

public class SelectClusterSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(SelectClusterSetController.class);

    public SelectClusterSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

        String clusterSetName = request.getParameter("clusterSetList");

        ArrayList<String> clusters = null;
        try {
            NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
            if (nodeTypeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeTypeDao.loadNodeTypes(request, "nodeTypeName", "ASC");
            ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
            if (clusterDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            clusterDao.loadClustersOfClusterSet(request, clusterSetName, "clusterName", "ASC");
            nodeTypeDao.setBaselineNodeType(request);

            rd = request.getRequestDispatcher("/clusters_report.jsp");
            
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error using SelectClusterSetController, ", e);
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
