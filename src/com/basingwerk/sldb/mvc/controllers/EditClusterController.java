package com.basingwerk.sldb.mvc.controllers;

import org.apache.log4j.Logger;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.DatabaseConnection;
import com.basingwerk.sldb.mvc.model.ModelException;

/**
 * Servlet implementation class EditClusterController
 */
@WebServlet("/EditClusterController")
public class EditClusterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(EditClusterController.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditClusterController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DatabaseConnection dbConn = null;
		RequestDispatcher rd = null;
		HttpSession session = request.getSession();
		dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
		if (dbConn == null) {
			logger.error("Error connecting to the database.");
			rd = request.getRequestDispatcher("/error.jsp");
			rd.forward(request, response);
			return;
		}

		String clusterName = request.getParameter("clusterName");
		String descr = request.getParameter("descr");

		try {
			Cluster.updateSingleCluster(request, new Cluster(clusterName, descr));
		} catch (ModelException e) {
			request.setAttribute("TheMessage", e.getMessage());
			rd = request.getRequestDispatcher("/recoverable_message.jsp");
			rd.forward(request, response);
			return;
		}
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
