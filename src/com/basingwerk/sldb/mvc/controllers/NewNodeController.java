package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterSetDao;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;
import com.basingwerk.sldb.mvc.dao.NodeDao;
import com.basingwerk.sldb.mvc.dao.NodeImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

@WebServlet("/NewNodeController")

public class NewNodeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeController.class);

    public NewNodeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

        try {
            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
            if (nodeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeDao.addNode(request);
        } catch (RoutineException e) {
            request.setAttribute("theMessage",
                    "Could not add that data at this time. Please try again. " + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error using addNode, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
            if (nodeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            HttpSession httpSession = request.getSession();
            String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
            nodeDao.loadNodesOfNodeSet(request, nodeSetName , "nodeName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error using loadNodes, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }
        String next = "/node.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
