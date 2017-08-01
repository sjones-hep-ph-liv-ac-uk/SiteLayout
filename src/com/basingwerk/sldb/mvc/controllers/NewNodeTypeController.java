package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.DataAccessObject;
import com.basingwerk.sldb.mvc.exceptions.ConflictException;

@WebServlet("/NewNodeTypeController")

public class NewNodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NewNodeTypeController.class);

    public NewNodeTypeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;

        DataAccessObject dao = DataAccessObject.getInstance();


        try {
            dao.addNodeType(request);
        } catch (ConflictException e) {
            request.setAttribute("theMessage", "The node type could not be added. Please try again." + e.getMessage());
            request.setAttribute("theJsp", "main_screen.jsp");
            rd = request.getRequestDispatcher("/recoverable_message.jsp");
            rd.forward(request, response);
            return;
        } catch (WTFException e) {
            logger.error("WTF! Error using addNodeType.");
            rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            dao.loadNodeTypes(request, "nodeTypeName", "ASC");
        } catch (WTFException e) {
            logger.error("WTF! Error using refreshNodeTypes.");
            rd = request.getRequestDispatcher("/error.jsp");
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
