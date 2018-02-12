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

@WebServlet("/EditNodeSetController")

public class EditNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeSetController.class);

    public EditNodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

        try {
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao"); 
            if (nodeSetDao == null)
                throw new WTFException("Session timed out. Log back in.");

            nodeSetDao.updateNodeSet(request);
            
        } catch (WTFException e) {
            logger.error("WTF! Cannot update node set.");
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
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao"); 
            if (nodeSetDao == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error using refreshNodeSets");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        } catch (RoutineException e) {
            rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }
        String next = "/nodeset.jsp";
        rd = request.getRequestDispatcher(next);
        rd.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
