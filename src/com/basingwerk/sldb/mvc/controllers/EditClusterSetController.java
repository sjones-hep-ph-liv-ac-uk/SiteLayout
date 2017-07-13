package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.basingwerk.sldb.mvc.exceptions.ConflictException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;

@WebServlet("/EditClusterSetController")

public class EditClusterSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditClusterSetController.class);

    public EditClusterSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        try {
            DbFacade.updateClusterSet(request);
        } catch (WTFException e) {
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (ConflictException e) {
            request.setAttribute("theMessage",
                    "Could not update that cluster set at this time. Please try again. " + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            DbFacade.loadClusterSets(request, "clusterSetName", "ASC");
            String next = "/cluster_set.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error when trying to refresh list of cluster sets, ", e);
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
