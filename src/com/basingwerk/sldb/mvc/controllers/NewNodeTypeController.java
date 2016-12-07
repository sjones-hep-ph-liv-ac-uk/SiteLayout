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
import com.basingwerk.sldb.mvc.model.NodeType;

/**
 * Servlet implementation class NewNodeTypeController
 */
@WebServlet("/NewNodeTypeController")
public class NewNodeTypeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewNodeTypeController() {
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

		String nodeTypeName = request.getParameter("nodeTypeName");
		String cpu = request.getParameter("cpu");
		String slot = request.getParameter("slot");
		String hs06PerSlot = request.getParameter("hs06PerSlot");
		String memPerSlot = request.getParameter("memPerSlot");

		String sqlCommand = "INSERT INTO nodeType (nodeTypeName,cpu,slot,hs06PerSlot,memPerSlot) VALUES ('"
				+ nodeTypeName + "','" + cpu + "','" + slot + "','" + hs06PerSlot + "','" + memPerSlot + "')";
		

		java.sql.Statement statement;
		int result = -1;

		try {
			statement = dbConn.conn.createStatement();
			result = statement.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("TheMessage", "It did not work. Perhaps that node type exists already?");
			rd = request.getRequestDispatcher("/recoverable_message.jsp");
			rd.forward(request, response);
			return;
		}
		try {
			ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();

			ResultSet r = dbConn.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerSlot from nodeType");
			while (r.next()) {
				NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
						r.getFloat("hs06PerSlot"), r.getFloat("memPerSlot"));
				nodeTypeList.add(n);
				
			}
			request.setAttribute("nodeTypeList", nodeTypeList);
			String next = "/nodetype.jsp";
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
