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
import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.User;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")

public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(LoginController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher rd = null;

        String database = request.getParameter("database");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccessObject ao = new AccessObject(database,username, password);
        
        HttpSession session = request.getSession();
        session.setAttribute("AccessObject", ao);
        
        rd = request.getRequestDispatcher("/main_screen.jsp");
        User user = new User(username, password);
        request.setAttribute("user", user);
        rd.forward(request, response);

//        AccessObject.setDatabase(database);
//        AccessObject.setUsename(username);
//        AccessObject.setPassword(password);
//
//        AccessObject c = AccessObject.getInitialDbCon();
//
//        if (c != null) {
//            HttpSession session = request.getSession();
//            session.setAttribute("AccessObject", c);
//            rd = request.getRequestDispatcher("/main_screen.jsp");
//            User user = new User(username, password);
//            request.setAttribute("user", user);
//        } else {
//            logger.error("Error when trying to connect to database.");
//            rd = request.getRequestDispatcher("/error.jsp");
//        }
//        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
