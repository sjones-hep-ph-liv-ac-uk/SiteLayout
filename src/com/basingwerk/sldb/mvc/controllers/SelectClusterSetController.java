package com.basingwerk.sldb.mvc.controllers;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.basingwerk.sldb.mvc.dbfacade.DbFacade;
import com.basingwerk.sldb.mvc.exceptions.ModelException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

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

        String clusterSetName = request.getParameter("clusterSetList");

        ArrayList<String> clusters = null;
        try {
            DbFacade.refreshNodeTypes(request, "nodeTypeName", "ASC");
            DbFacade.refreshClusters(request, "clusterName", "ASC");
            DbFacade.setBaselineNodeType(request);
            clusters = DbFacade.listClustersOfClusterSet(request, clusterSetName);
            java.util.HashMap<String, ArrayList> joinMap = new java.util.HashMap<String, ArrayList>();

            Iterator<String> c = clusters.iterator();
            while (c.hasNext()) {
                String cluster = c.next();
                ArrayList<NodeSetNodeTypeJoin> nsntj =
                DbFacade.getJoinForCluster(request, cluster);
                joinMap.put(cluster, nsntj);
            }
            request.setAttribute("joinMap", joinMap);
            rd = request.getRequestDispatcher("/reports.jsp");
            rd.forward(request, response);
            return;
        } catch (HibernateException e) {
            logger.error("WTF! Error using SelectClusterSetController, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
