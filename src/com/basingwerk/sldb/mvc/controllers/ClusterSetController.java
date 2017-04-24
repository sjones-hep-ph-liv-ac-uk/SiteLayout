package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.Site;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
import com.basingwerk.sldb.mvc.model.AccessObject;

@WebServlet("/SiteController")

public class SiteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(SiteController.class);

    public SiteController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        String act = null;
        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        act = request.getParameter("New");
        if (act != null) {
            rd = request.getRequestDispatcher("/new_site.jsp");
            rd.forward(request, response);
            return;
        }
        Map params = request.getParameterMap();
        Iterator i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String order = "ASC";
            if (key.startsWith("SORT")) {
                String sortCmd = key.substring(4, key.length()).trim().replaceAll("\\.[xy]$", "");
                String c = "";
                if (sortCmd.startsWith("UP.")) {
                    order = "ASC";
                    c = sortCmd.substring(3, sortCmd.length()).trim();
                } else {
                    order = "DESC";
                    c = sortCmd.substring(5, sortCmd.length()).trim();
                }

                try {
                    Site.refreshListOfSites(request, c, order);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when trying to refresh Sites, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/site.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("DEL.")) {
                String site = key.substring(4, key.length());
                try {
                    Site.deleteSite(request, site);
                } catch (ModelException e1) {
                    if (e1 instanceof ModelExceptionRollbackWorked) {
                        logger.info("Rollback worked.");
                        request.setAttribute("theMessage", "Could not delete that site at this time. Please try again.");
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
                
                try {
                    Site.refreshListOfSites(request, "siteName", order);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when trying to refresh Sites, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/site.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String site = key.substring(3, key.length());
                
                Site s;
                try {
                    s = Site.queryOneSite(request, site);
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
                request.setAttribute("site", s);
                String next = "/edit_site.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
