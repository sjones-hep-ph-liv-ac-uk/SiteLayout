package com.basingwerk.sldb.mvc.controllers;

import com.basingwerk.sldb.mvc.dao.ClusterDao;
import com.basingwerk.sldb.mvc.dao.ClusterImpl;
import com.basingwerk.sldb.mvc.dao.HostSystemDao;
import com.basingwerk.sldb.mvc.dao.HostSystemImpl;
import com.basingwerk.sldb.mvc.dao.ServiceNodeDao;
import com.basingwerk.sldb.mvc.dao.ServiceNodeImpl;
import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;

@WebServlet("/ServiceNodeController")

public class ServiceNodeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(ServiceNodeController.class);

    public ServiceNodeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        //DataAccessObject dao = DataAccessObject.getInstance();

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
                ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao"); 
                if (serviceNodeDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                
                serviceNodeDao.loadServiceNodes(request, "hostname", "ASC");
            } catch (WTFException e) {
                logger.error("WTF! Error while using loadServiceNodes");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                return;
            } catch (RoutineException e) {
                rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
                return;
            }
            rd = request.getRequestDispatcher("/service_node.jsp");
            rd.forward(request, response);
            return;
        }

        act = request.getParameter("New");
        if (act != null) {

            try {
                ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                if (clusterDao  == null)
                    throw new WTFException("Session timed out. Log back in.");
                clusterDao.loadClusters(request, "clusterName", "ASC");
                HostSystemDao hostSystemDao = (HostSystemDao) request.getSession().getAttribute("hostSystemDao");
                if (hostSystemDao == null)
                    throw new WTFException("Session timed out. Log back in.");

                hostSystemDao.loadHostSystems(request, "hostname", "ASC");
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
            rd = request.getRequestDispatcher("/new_service_node.jsp");
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
                    ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao"); 
                    if (serviceNodeDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    serviceNodeDao.loadServiceNodes(request, c, order);
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadServiceNodes, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/service_node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }

            if (key.startsWith("DEL.")) {
                String serviceNode = key.substring(4, key.length());
                
                try {
                    ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao"); 
                    if (serviceNodeDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    serviceNodeDao.deleteServiceNode(request, serviceNode);
                } catch (RoutineException e) {
                    logger.error("WTF! Error deleting a serviceNode, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error deleting a serviceNode, ", e);
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }
                try {
                    ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao"); 
                    if (serviceNodeDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    serviceNodeDao.loadServiceNodes(request, "hostname", "ASC");
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadServiceNodes.");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                } catch (RoutineException e) {
                    rd = request.getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                String next = "/service_node.jsp";
                rd = request.getRequestDispatcher(next);
                rd.forward(request, response);
                return;
            }
            if (key.startsWith("ED.")) {
                String serviceNode = key.substring(3, key.length());
                Integer index = Integer.parseInt(serviceNode);
                try {
                    ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao"); 
                    if (serviceNodeDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    serviceNodeDao.loadIndexedServiceNode(request, index);
                } catch (RoutineException e) {
                    request.setAttribute("theMessage",
                            "Could not edit that service node at this time. Please try again. " + e.getMessage());
                    request.setAttribute("theJsp", "main_screen.jsp");
                    rd = request.getRequestDispatcher("/recoverable_message.jsp");
                    rd.forward(request, response);
                    return;
                } catch (WTFException e) {
                    logger.error("WTF! Error using loadIndexedServiceNode");
                    rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                    return;
                }

                try {
                    ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
                    if (clusterDao  == null)
                        throw new WTFException("Session timed out. Log back in.");
                    clusterDao.loadClusters(request, "clusterName", "ASC");
                    HostSystemDao hostSystemDao = (HostSystemDao) request.getSession().getAttribute("hostSystemDao");
                    if (hostSystemDao == null)
                        throw new WTFException("Session timed out. Log back in.");
                    hostSystemDao.loadHostSystems(request, "hostname", "ASC");
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

                String next = "/edit_service_node.jsp";
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
