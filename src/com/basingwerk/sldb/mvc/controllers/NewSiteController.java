package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
import com.basingwerk.sldb.mvc.model.Site;

@WebServlet("/NewSiteController")

public class NewSiteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewSiteController.class);

    public NewSiteController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        try {
            Site.addSite(request);
        } catch (ModelException e) {
            if (e instanceof ModelExceptionRollbackWorked) {
                logger.info("A rollback worked.");
                request.setAttribute("theMessage", "The site type could not be added. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! Rollback failed.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }

        ArrayList<Site> siteList = null;
        try {
            siteList = Site.querySiteList(request);
        } catch (ModelException e1) {
            if (e1 instanceof ModelExceptionRollbackWorked) {
                logger.info("Rollback worked.");
                request.setAttribute("theMessage", "Could not update that site at this time. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! failed to roll back, ", e1);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }

        request.setAttribute("siteList", siteList);
        String next = "/site.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
