package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.model.User;
import com.basingwerk.sldb.mvc.dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

        String msg;

        try {

            String database = request.getParameter("database");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Configuration cfg = new Configuration();

            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.NodeType.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.NodeSet.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.Cluster.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.ClusterSet.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.Node.class);
            cfg.addAnnotatedClass(com.basingwerk.sldb.mvc.model.NodeState.class);

            cfg.getProperties().setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/" + database);
            cfg.getProperties().setProperty("hibernate.connection.password", password);
            cfg.getProperties().setProperty("hibernate.connection.username", username);

            SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
            HttpSession session = request.getSession();

            session.setAttribute("sessionFactory", sessionFactory);
            Session hibSession = sessionFactory.openSession();
            hibSession.setFlushMode(FlushMode.COMMIT);

            if (!hibSession.isConnected()) {
                hibSession.close();
                logger.error("Could not connect");

                request.setAttribute("theMessage",
                        "Error when trying to connect to connect to the database. You can try to login again.");
                request.setAttribute("theJsp", "login.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
            }
            hibSession.close();
            User user = new User(username, password);
            session.setAttribute("user", user);
            
            HostSystemDao hostSystemDao = new HostSystemImpl();
            ClusterDao clusterDao = new ClusterImpl();
            ClusterSetDao clusterSetDao = new ClusterSetImpl();
            ServiceDao serviceDao = new ServiceImpl();
            NodeDao nodeDao = new NodeImpl();
            NodeTypeDao nodeTypeDao = new NodeTypeImpl();
            NodeSetDao nodeSetDao = new NodeSetImpl();
            ServiceNodeDao serviceNodeDao = new ServiceNodeImpl();
            InstallationDao installationDao = new InstallationImpl();
            NodeStateDao nodeStateDao = new NodeStateImpl();
            
            session.setAttribute("hostSystemDao", hostSystemDao);
            session.setAttribute("clusterDao", clusterDao);
            session.setAttribute("clusterSetDao", clusterSetDao);
            session.setAttribute("serviceDao", serviceDao);
            session.setAttribute("nodeDao", nodeDao);
            session.setAttribute("nodeTypeDao", nodeTypeDao);
            session.setAttribute("nodeSetDao", nodeSetDao);
            session.setAttribute("serviceNodeDao", serviceNodeDao);
            session.setAttribute("installationDao", installationDao);
            session.setAttribute("nodeStateDao", nodeStateDao);
            

            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        } catch (HibernateException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();         
            msg = "HibernateException when trying to connect to database: " + exceptionAsString;
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
    
    public static void logout(HttpServletRequest request) {
        HttpSession httpSession = null;

        httpSession = request.getSession();
        httpSession.invalidate();
        return;
    }
    
}
