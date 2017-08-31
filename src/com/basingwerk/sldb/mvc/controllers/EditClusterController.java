package com.basingwerk.sldb.mvc.controllers;

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
import com.basingwerk.sldb.mvc.dao.*;

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
        //DataAccessObject dao = DataAccessObject.getInstance();

        try {
            ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
            
            clusterDao.updateCluster(request);
        } catch (WTFException e) {
            logger.error("WTF! Cannot update that cluster, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;

        } catch (RoutineException e) {
            request.setAttribute("theMessage",
                    "Could not update that cluster at this time. Please try again. " + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
            clusterDao.loadClusters(request, "clusterName", "ASC");
            String next = "/cluster.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error when trying to refresh list of clusters, ", e);
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
