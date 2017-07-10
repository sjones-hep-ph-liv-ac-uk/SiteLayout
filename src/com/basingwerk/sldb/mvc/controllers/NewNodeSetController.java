package com.basingwerk.sldb.mvc.controllers;
import org.hibernate.HibernateException;
import com.basingwerk.sldb.mvc.dbfacade.DbFacade;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.dbfacade.DbFacade;
import com.basingwerk.sldb.mvc.exceptions.DbFacadeException;
import com.basingwerk.sldb.mvc.exceptions.ModelException;
import com.basingwerk.sldb.mvc.model.NodeSet;

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

        HttpSession session = request.getSession();

        try {
            DbFacade.addNodeSet(request);
        } catch (HibernateException e1) {
                request.setAttribute("theMessage", "Could not add that data at this time. Please try again.");
                request.setAttribute("theJsp", "main_screen.jsp");
                rd = request.getRequestDispatcher("/recoverable_message.jsp");
                rd.forward(request, response);
                return;
        } catch (DbFacadeException e) {
                logger.error("WTF! Error using addNodeSet, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
        }
        
        try {
            DbFacade.refreshNodeSets(request, "nodeSetName", "ASC");
        } catch (HibernateException e) {
            logger.error("WTF! Error using refreshNodeSets, ", e);
            rd = request.getRequestDispatcher("/error.jsp");
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
