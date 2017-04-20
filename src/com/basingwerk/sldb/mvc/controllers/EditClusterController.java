package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;

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

@WebServlet("/EditClusterController")

public class EditClusterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditClusterController.class);

    public EditClusterController() {
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

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String siteName = request.getParameter("siteList");

        try {
            Cluster.updateSingleCluster(request, new Cluster(clusterName, descr, siteName));
        } catch (ModelException e) {
            logger.error("WTF! A ModelException occurred, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            Cluster.refreshListOfClusters(request, "clusterName", "ASC");
        } catch (ModelException e) {
            logger.error("WTF! A ModelException occurred, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        String next = "/cluster.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
