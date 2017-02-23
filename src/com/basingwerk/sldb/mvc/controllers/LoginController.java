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
import com.basingwerk.sldb.mvc.model.AccessObjectException;
import com.basingwerk.sldb.mvc.model.User;

@WebServlet("/LoginController")

public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(LoginController.class);

    public LoginController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        String database = request.getParameter("database");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccessObject ao;
        try {
            ao = new AccessObject(database, username, password);
            if (ao != null) {
                HttpSession session = request.getSession();
                session.setAttribute("AccessObject", ao);

                rd = request.getRequestDispatcher("/main_screen.jsp");
                User user = new User(username, password);
                request.setAttribute("user", user);

            }
        } catch (AccessObjectException e) {
            logger.error("Error when trying to connect to database.");
            rd = request.getRequestDispatcher("/error.jsp");
        }
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
