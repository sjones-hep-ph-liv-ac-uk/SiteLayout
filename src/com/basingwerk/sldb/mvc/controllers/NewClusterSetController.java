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
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.exceptions.ConflictException;

@WebServlet("/NewClusterSetController")

public class NewClusterSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewClusterSetController.class);

    public NewClusterSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        try {
            DbFacade.addClusterSet(request);
        } catch (ConflictException e) {
            request.setAttribute("theMessage",
                    "The cluster set could not be added. Please try again. " + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error when using addClusterSet");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            DbFacade.loadClusterSets(request, "clusterSetName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error when using queryClusterSetList");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        String next = "/cluster_set.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
