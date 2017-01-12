package com.basingwerk.sldb.mvc.controllers;

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

import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.DatabaseConnection;

/**
 * Servlet implementation class NewClusterController
 */
@WebServlet("/NewClusterController")
public class NewClusterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewClusterController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dbConn = null;
		RequestDispatcher rd = null;
		HttpSession session = request.getSession();
		dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
		if (dbConn == null) {
			rd = request.getRequestDispatcher("/error.jsp");
			rd.forward(request, response);
			return;
		}
		String clusterName = request.getParameter("clusterName");
		String descr = request.getParameter("descr");
		String sqlCommand = "INSERT INTO cluster (clusterName, Descr) VALUES ('" + clusterName + "','" + descr + "')";

		java.sql.Statement statement;
		int result = -1;

		try {
			statement = dbConn.conn.createStatement();
			result = statement.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("TheMessage", "It did not work. Perhaps that cluster exists already?");
			rd = request.getRequestDispatcher("/recoverable_message.jsp");
			rd.forward(request, response);
			return;
		}
		try {
			ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
			ResultSet r = dbConn.query("select clusterName,descr from cluster");
			while (r.next()) {
				Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"));
				clusterList.add(c);
			}
			request.setAttribute("clusterList", clusterList);
			String next = "/cluster.jsp";

			rd = request.getRequestDispatcher(next);
			rd.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
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
