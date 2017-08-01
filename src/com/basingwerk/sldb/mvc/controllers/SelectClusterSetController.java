package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.DataAccessObject;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;

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
        DataAccessObject dao = DataAccessObject.getInstance();


        String clusterSetName = request.getParameter("clusterSetList");

        ArrayList<String> clusters = null;
        try {
            dao.loadNodeTypes(request, "nodeTypeName", "ASC");
            dao.loadClusters(request, "clusterName", "ASC");
            dao.setBaselineNodeType(request);
            clusters = dao.listClustersOfClusterSet(request, clusterSetName);
            java.util.HashMap<String, ArrayList> joinMap = new java.util.HashMap<String, ArrayList>();

            Iterator<String> c = clusters.iterator();
            while (c.hasNext()) {
                String cluster = c.next();
                ArrayList<NodeSetNodeTypeJoin> nsntj = dao.getJoinForCluster(request, cluster);
                joinMap.put(cluster, nsntj);
            }
            request.setAttribute("joinMap", joinMap);
            rd = request.getRequestDispatcher("/reports.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
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
