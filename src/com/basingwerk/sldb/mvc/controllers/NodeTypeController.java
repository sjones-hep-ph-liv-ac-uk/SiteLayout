package com.basingwerk.sldb.mvc.controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.basingwerk.sldb.mvc.model.DatabaseConnection;

/**
 * Servlet implementation class NodetypeController
 */
@WebServlet("/NodeTypeController")

public class NodeTypeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NodeTypeController() {
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
			String value = ((String[]) params.get(key))[0];
			if (key.startsWith("DEL.")) {
				String nodeType = key.substring(4, key.length() - 1);
				try {
					NodeType.deleteNodeType(request, nodeType);
				} catch (ModelException e) {
					request.setAttribute("TheMessage", e.getMessage());
					request.setAttribute("TheJsp", "main_screen.jsp");
					rd = request.getRequestDispatcher("/recoverable_message.jsp");
					rd.forward(request, response);
					return;
				}
				try {
					NodeType.refreshListOfNodeTypes(request);
				} catch (ModelException e) {
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
				String nodeType = key.substring(3, key.length() - 1);

				try {
					HttpSession session = request.getSession();
					DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
					if (dbConn != null) {
						ResultSet r;
						String sql = "select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where"
								+ " nodeTypeName = '" + nodeType + "'";
						System.out.println("SQL:" + sql);
						r = dbConn.query(sql);
						NodeType n = null;
						while (r.next()) {
							n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
									r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
						}
						System.out.println("Node type: " + n);
						request.setAttribute("nodeType", n);
					}
				} catch (SQLException e) {
					rd = request.getRequestDispatcher("/error.jsp");
					rd.forward(request, response);
					return;
				}
				String next = "/edit_nodetype.jsp";
				rd = request.getRequestDispatcher(next);
				rd.forward(request, response);
				return;
			}
		}
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
