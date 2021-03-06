package com.basingwerk.sldb.mvc.controllers;

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
import com.basingwerk.sldb.mvc.dao.*;

@WebServlet("/EditNodeController")

public class EditNodeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeController.class);

    public EditNodeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        try {
            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
            if (nodeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeDao.updateNode(request);
            
        } catch (WTFException e) {
            logger.error("WTF! Cannot update node.");
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
            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
            if (nodeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            HttpSession httpSession = request.getSession();
            String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
            nodeDao.loadNodesOfNodeSet(request, nodeSetName , "nodeName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error using loadNodes");
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
