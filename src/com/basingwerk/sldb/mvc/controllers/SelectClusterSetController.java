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
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

@WebServlet("/SelectSiteController")

public class SelectSiteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(SelectSiteController.class);

    public SelectSiteController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AccessObject ao = null;
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        ao = (AccessObject) session.getAttribute("accessObject");
        if (ao == null) {
            request.setAttribute("theMessage", "No access to database. You can try to login again.");
            request.setAttribute("theJsp", "login.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }

        // String clusterName = request.getParameter("clusterName");
        // String descr = request.getParameter("descr");
        String siteName = request.getParameter("siteList");

        ArrayList<String> clusters = null;
        try {
            NodeType.refreshListOfNodeTypes(request, "nodeTypeName", "ASC");

            Cluster.refreshListOfSiteClusters(request, siteName, "clusterName", "ASC");
            NodeType.setBaselineNodeType(request);

            clusters = Cluster.listSiteClusterNames(request, siteName);
            java.util.HashMap<String, ArrayList> joinMap = new java.util.HashMap<String, ArrayList>();
            Iterator<String> c = clusters.iterator();
            while (c.hasNext()) {
                String cluster = c.next();
                ArrayList<NodeSetNodeTypeJoin> nsntj = NodeSetNodeTypeJoin.getJoinForCluster(request, cluster);
                joinMap.put(cluster, nsntj);
            }
            request.setAttribute("joinMap", joinMap);
            rd = request.getRequestDispatcher("/reports.jsp");
            rd.forward(request, response);
            return;
        } catch (ModelException e) {
            logger.error("WTF! Had a ModelException in SelectSiteController, ", e);
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
