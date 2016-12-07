package com.basingwerk.sldb.mvc.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.model.DatabaseConnection;
import com.basingwerk.sldb.mvc.model.User;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")

public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String database = request.getParameter("database");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		RequestDispatcher rd = null;

		DatabaseConnection.setDatabase(database);
		DatabaseConnection.setUsename(username);
		DatabaseConnection.setPassword(password);

		DatabaseConnection c = DatabaseConnection.getInitialDbCon();

		if (c != null) {
			HttpSession session = request.getSession();
			session.setAttribute("TheDatabaseConnection", c);
			rd = request.getRequestDispatcher("/main_screen.jsp");
			User user = new User(username, password);
			request.setAttribute("user", user);
		} else {
			rd = request.getRequestDispatcher("/error.jsp");
		}
		rd.forward(request, response);
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
