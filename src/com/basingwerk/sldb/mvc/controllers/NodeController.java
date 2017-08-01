package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.DataAccessObject;
import com.basingwerk.sldb.mvc.exceptions.ConflictException;

@WebServlet("/NodeController")

public class NodeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NodeController.class);

    public NodeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        DataAccessObject dao = DataAccessObject.getInstance();

        String act = null;
        act = request.getParameter("Back");
        if (act != null) {
            rd = request.getRequestDispatcher("/main_screen.jsp");
            rd.forward(request, response);
            return;
        }
        act = request.getParameter("Refresh");
        if (act != null) {
            try {
                dao.loadNodes(request, "nodeName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using refreshNodes");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/node.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("New");
        if (act != null) {

            try {
                dao.loadNodeSets(request, "nodeSetName", "ASC");
                dao.loadNodeStates(request, "state", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error preparing data, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/new_node.jsp");
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
                    dao.loadNodes(request, c, order);
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadNodes, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("DEL.")) {
                String nodeSetName = key.substring(4, key.length());
                try {
                    dao.deleteNode(request, nodeSetName);
                } catch (ConflictException e) {
                    logger.error("WTF! Error deleting a node, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    dao.loadNodes(request, "nodeName", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadNodes.");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String nodeSet = key.substring(3, key.length());
                Integer index = Integer.parseInt(nodeSet);
                try {
                    dao.loadIndexedNode(request, index);
                } catch (ConflictException e) {
                    request.setAttribute("theMessage",
                            "Could not edit that node at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadIndexedNode");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                try {
                    dao.loadNodeSets(request, "nodeSetName", "ASC");
                    dao.loadNodeStates(request, "state", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error preparing data, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                String next = "/edit_node.jsp";
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
