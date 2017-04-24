package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.NodeType;
import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.ModelExceptionRollbackWorked;
import com.basingwerk.sldb.mvc.model.AccessObject;

@WebServlet("/NodeTypeController")

public class NodeTypeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NodeTypeController.class);

    public NodeTypeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        String act = null;
        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        act = request.getParameter("New");
        if (act != null) {
            rd = request.getRequestDispatcher("/new_nodetype.jsp");
            rd.forward(request, response);
            return;
        }
        Map params = request.getParameterMap();
        Iterator i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String order = "ASC";
            if (key.startsWith("SORT")) {
                String sortCmd = key.substring(4, key.length()).trim().replaceAll("\\.[xy]$", "");
                String c = "";
                if (sortCmd.startsWith("UP.")) {
                    order = "ASC";
                    c = sortCmd.substring(3, sortCmd.length()).trim();
                } else {
                    order = "DESC";
                    c = sortCmd.substring(5, sortCmd.length()).trim();
                }

                try {
                    NodeType.refreshListOfNodeTypes(request, c, order);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when refreshing node types, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodetype.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("DEL.")) {
                String nodeType = key.substring(4, key.length());
                try {
                    NodeType.deleteNodeType(request, nodeType);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when deleteling a node type, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    NodeType.refreshListOfNodeTypes(request, "nodeTypeName", order);
                } catch (ModelException e) {
                    logger.error("WTF! ModelException when trying to refresh NodeTypes, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/nodetype.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String nodeType = key.substring(3, key.length());
                NodeType n = null;
                try {
                    n = NodeType.queryOneNodeType(request, nodeType);
                } catch (ModelException e) {
                    if (e instanceof ModelExceptionRollbackWorked) {
                        logger.info("A rollback worked.");
                        request.setAttribute("theMessage",
                                "Could not read one node set at this time. Please try again.");
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
                // logger.error("SJDEBUG3: " + n.toString());
                request.setAttribute("nodeType", n);
                String next = "/edit_nodetype.jsp";
                rd = request.getRequestDispatcher(next);
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
