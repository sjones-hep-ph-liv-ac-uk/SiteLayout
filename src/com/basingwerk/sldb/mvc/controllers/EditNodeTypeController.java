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

@WebServlet("/EditNodeTypeController")

public class EditNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeTypeController.class);

    public EditNodeTypeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        try {
            NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
            if (nodeTypeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeTypeDao.updateNodeType(request);
            
        } catch (RoutineException e) {
            request.setAttribute("theMessage", "The task could not be done. Please try again.");
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error using updateNodeType");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");            
            if (nodeTypeDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeTypeDao.loadNodeTypes(request, "nodeTypeName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error when using updateNodeType, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }
        String next = "/nodetype.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
