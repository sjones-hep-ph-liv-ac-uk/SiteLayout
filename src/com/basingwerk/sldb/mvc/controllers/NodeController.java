package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.NodeDao;
import com.basingwerk.sldb.mvc.dao.NodeImpl;
import com.basingwerk.sldb.mvc.dao.NodeSetDao;
import com.basingwerk.sldb.mvc.dao.NodeSetImpl;
import com.basingwerk.sldb.mvc.dao.NodeStateDao;
import com.basingwerk.sldb.mvc.dao.NodeStateImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

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
        
        String act = null;

        act = request.getParameter("Back");
        if (act != null) {
            //rd = request.getRequestDispatcher("/main_screen.jsp");
            rd = request.getRequestDispatcher("/nodeset.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("Refresh");
        if (act != null) {
            try {
                NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
                HttpSession httpSession = request.getSession();
                String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
                nodeDao.loadNodesOfNodeSet(request, nodeSetName , "nodeName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using refreshNodes");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/node.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("Toggle");
        if (act != null) {

            try {
                NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
                nodeDao.toggleCheckedNodes(request);
                HttpSession httpSession = request.getSession();
                String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
                nodeDao.loadNodesOfNodeSet(request, nodeSetName , "nodeName", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using refreshNodes");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
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
                NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao"); 
                nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
                NodeStateDao nodeStateDao = (NodeStateDao) request.getSession().getAttribute("nodeStateDao"); 
                nodeStateDao.loadNodeStates(request, "state", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error preparing data, ", e);
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
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
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
                    HttpSession httpSession = request.getSession();
                    String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
                    nodeDao.loadNodesOfNodeSet(request, nodeSetName , c, order);

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

            if (key.startsWith("DEL.")) {
                String node = key.substring(4, key.length());
                try {
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
                    nodeDao.deleteNode(request, node);
                } catch (RoutineException e) {
                    logger.error("WTF! Error deleting a node, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error deleting a node, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
                    HttpSession httpSession = request.getSession();
                    String nodeSetName = (String) httpSession.getAttribute("nodeSetName");
                    nodeDao.loadNodesOfNodeSet(request, nodeSetName , "nodeName", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadNodes.");
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
            if (key.startsWith("ED.")) {
                String node = key.substring(3, key.length());
                Integer index = Integer.parseInt(node);
                try {
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
                    nodeDao.loadIndexedNode(request, index);
                } catch (RoutineException e) {
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
                    NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao"); 
                    nodeSetDao.loadNodeSets(request, "nodeSetName", "ASC");
                    NodeStateDao nodeStateDao = (NodeStateDao) request.getSession().getAttribute("nodeStateDao"); 
                    nodeStateDao.loadNodeStates(request, "state", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error preparing data, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }

                String next = "/edit_node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("TGL.")) {
                String node = key.substring(4, key.length());
                Integer index = Integer.parseInt(node);
                try {
                    NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao"); 
                    nodeDao.loadIndexedNode(request, index);
                    nodeDao.toggleIndexedNode(request, index);
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
                rd = request.getRequestDispatcher("/node.jsp");
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
