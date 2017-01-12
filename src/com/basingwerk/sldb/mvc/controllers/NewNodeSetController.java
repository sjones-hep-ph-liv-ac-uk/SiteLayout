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

import com.basingwerk.sldb.mvc.model.DatabaseConnection;
import com.basingwerk.sldb.mvc.model.NodeSet;

/**
 * Servlet implementation class NewNodeSetController
 */
@WebServlet("/NewNodeSetController")
public class NewNodeSetController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewNodeSetController() {
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
		String clusterName = request.getParameter("clusterList");
		String nodeTypeName = request.getParameter("nodeTypeList");

		String nodeSetName = request.getParameter("nodeSetName");
		String nodeCount = request.getParameter("nodeCount");
		String sqlCommand = "INSERT INTO nodeSet (nodeSetName ,nodeTypeName, nodeCount, cluster) VALUES" + "('"
				+ nodeSetName + "','" + nodeTypeName + "'," + nodeCount + ",'" + clusterName + "')";

		java.sql.Statement statement;
		int result = -1;

		try {
			statement = dbConn.conn.createStatement();
			result = statement.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("TheMessage", "It did not work. Perhaps that node set5 exists already?");
			rd = request.getRequestDispatcher("/recoverable_message.jsp");
			rd.forward(request, response);
			return;
		}
		try {
			ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

			ResultSet r = dbConn.query("select nodeSetName ,nodeTypeName, nodeCount, cluster from nodeSet");
			while (r.next()) {
				NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
						r.getString("cluster"));
				nodeSetList.add(n);

			}
			request.setAttribute("nodeSetList", nodeSetList);
			String next = "/nodeset.jsp";
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
