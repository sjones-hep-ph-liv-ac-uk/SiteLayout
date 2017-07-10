package com.basingwerk.sldb.mvc.controllers;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.dbfacade.DbFacade;
import com.basingwerk.sldb.mvc.exceptions.ModelException;
import com.basingwerk.sldb.mvc.exceptions.DbFacadeException;
import com.basingwerk.sldb.mvc.model.Cluster;

@WebServlet("/EditClusterController")

public class EditClusterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditClusterController.class);

    public EditClusterController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;
        HttpSession session = request.getSession();

        try {
            DbFacade.updateCluster(request);
        } catch (DbFacadeException e1) {
            logger.error("WTF! Cannot update that cluster, ", e1);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (HibernateException e1) {
            request.setAttribute("theMessage", "Could not update that cluster at this time. Please try again.");
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            DbFacade.refreshClusters(request, "clusterName", "ASC");
            String next = "/cluster.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
            return;
        } catch (Exception e) {
            logger.error("WTF! Error when trying to refresh list of clusters, ", e);
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
