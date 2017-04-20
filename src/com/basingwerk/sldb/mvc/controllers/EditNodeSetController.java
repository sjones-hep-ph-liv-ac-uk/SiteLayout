package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.AccessObject;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
import com.basingwerk.sldb.mvc.model.NodeSet;

@WebServlet("/EditNodeSetController")

public class EditNodeSetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(EditNodeSetController.class);

    public EditNodeSetController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccessObject ao = null;
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        ao = (AccessObject) session.getAttribute("accessObject");
        if (ao == null) {
            logger.error("Error when trying to connect to database.");
            request.setAttribute("theMessage", "No access to database. You can try to login again.");
            request.setAttribute("theJsp", "login.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        }
        try {
            NodeSet.updateNodeSet(request);
        } catch (ModelException e) {
            if (e instanceof ModelExceptionRollbackWorked) {
                logger.info("A rollback worked.");
                request.setAttribute("theMessage", "The task could not be done. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! Rollback failed.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }

        try {
            NodeSet.refreshListOfNodeSets(request, "nodeSetName", "ASC");
            String next = "/nodeset.jsp";
            rd = request.getRequestDispatcher(next);
            rd.forward(request, response);
            return;
        } catch (ModelException e) {
            if (e instanceof ModelExceptionRollbackWorked) {
                logger.info("A rollback worked.");
                request.setAttribute("theMessage", "The task could not be done. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
            } else {
                logger.error("WTF! Rollback failed.");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
