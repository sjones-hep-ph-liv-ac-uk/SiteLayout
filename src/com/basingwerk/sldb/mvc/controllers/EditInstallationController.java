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

@WebServlet("/EditInstallationController")

public class EditInstallationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditInstallationController.class);

    public EditInstallationController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

        try {
            InstallationDao installationDao = (InstallationDao) request.getSession().getAttribute("installationDao");
            if (installationDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            installationDao.updateInstallation(request);
            
        } catch (WTFException e) {
            logger.error("WTF! Cannot update installation.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            request.setAttribute("theMessage", "The task could not be done. Please try again later.");
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }            
        try {
            InstallationDao installationDao = (InstallationDao) request.getSession().getAttribute("installationDao");
            if (installationDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            installationDao.loadInstallations(request, "serviceNode", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error using refreshInstallations");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }
        String next = "/installation.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
