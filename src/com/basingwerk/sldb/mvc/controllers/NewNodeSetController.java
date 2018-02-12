package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.NodeDao;
import com.basingwerk.sldb.mvc.dao.NodeImpl;
import com.basingwerk.sldb.mvc.dao.NodeSetDao;
import com.basingwerk.sldb.mvc.dao.NodeSetImpl;
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

@WebServlet("/NewNodeSetController")

public class NewNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeSetController.class);

    public NewNodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;

        try {
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
            if (nodeSetDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeSetDao.addNodeSet(request);
        } catch (RoutineException e) {
            request.setAttribute("theMessage",
                    "Could not add that data at this time. Please try again. " + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error using addNodeSet, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
            if (nodeSetDao  == null)
                throw new WTFException("Session timed out. Log back in.");
            nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
            
        } catch (WTFException e) {
            logger.error("WTF! Error using loadNodeSets, ", e);
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
