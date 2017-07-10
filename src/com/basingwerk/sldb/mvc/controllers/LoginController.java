package com.basingwerk.sldb.mvc.controllers;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.hibernate.Session;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.basingwerk.sldb.mvc.model.User;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginController")

public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(LoginController.class);

    public LoginController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msg;
        RequestDispatcher rd = null;
        try {

            String database = request.getParameter("database");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
//            database = "hibtest";
//            username = "hibtest";
//            password = "hibtest";

            Configuration cfg = new Configuration();
            
            cfg.getProperties().setProperty("event.merge.entity_copy_observer", "allow");
            
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.NodeType.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.NodeSet.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.Cluster.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.ClusterSet.class);

            // cfg.configure("hibernate.cfg.xml"); //hibernate config xml file name
            
            cfg.getProperties().setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/" + database);
            cfg.getProperties().setProperty("hibernate.connection.password", username);
            cfg.getProperties().setProperty("hibernate.connection.username", password);

            HttpSession session = request.getSession();
            SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
            session.setAttribute("sessionFactory", sessionFactory);

//            Session dbsession = sessionFactory.openSession();
//            Session hibSession  = ((SessionFactory)httpSession.getAttribute("sessionFactory")).openSession();

            User user = new User(username, password);
            request.setAttribute("user", user);

            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        catch (HibernateException e) {
            e.printStackTrace();
            msg = "HibernateException when trying to connect to database" + e.getStackTrace();
        }
        logger.error(msg);

        request.setAttribute("theMessage",
                "Error when trying to connect to connect to the database. You can try to login again.");
        request.setAttribute("theJsp", "login.jsp");
        rd = request.getRequestDispatcher("/recoverable_message.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
