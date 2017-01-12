package com.basingwerk.sldb.mvc.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.basingwerk.sldb.mvc.model.Cluster;

import com.basingwerk.sldb.mvc.model.ModelException;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

/**
 * Servlet implementation class MainScreenController
 */
@WebServlet("/MainScreenController")

public class MainScreenController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainScreenController() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = null;
		try {

			String act = request.getParameter("SomeButton");
			if (act == null) {
				rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
				return;
			}
			String next = "";
			if (act.equals("Edit node types")) {

				NodeType.refreshListOfNodeTypes(request);

				next = "/nodetype.jsp";
				rd = request.getRequestDispatcher(next);
				rd.forward(request, response);
				return;
			}

			if (act.equals("Edit clusters")) {
				Cluster.setListOfClusters(request);
				next = "/cluster.jsp";

				rd = request.getRequestDispatcher(next);
				rd.forward(request, response);
				return;
			}
			if (act.equals("Edit node sets")) {

                                NodeSet.refreshListOfNodeSets(request);

				next = "/nodeset.jsp";
				rd = request.getRequestDispatcher(next);
				rd.forward(request, response);
				return;

			}
			if (act.equals("Reports")) {
				NodeType.refreshListOfNodeTypes(request);
				Cluster.setListOfClusters(request);
				NodeType.setBaselineNodeType(request);
				ArrayList<String> clusters = Cluster.listClusters(request);
				java.util.HashMap<String,ArrayList> joinMap = new java.util.HashMap<String,ArrayList>() ; 
				Iterator<String> c = clusters.iterator();
				while (c.hasNext()) {
                                        String cluster = c.next();					
					ArrayList<NodeSetNodeTypeJoin> nsntj = NodeSetNodeTypeJoin.getJoinForCluster(request, cluster);
					joinMap.put(cluster, nsntj );
				}
				request.setAttribute("joinMap", joinMap);
		 
				next = "/reports.jsp";
				rd = request.getRequestDispatcher(next);
				rd.forward(request, response);
				return;
			}
			rd = request.getRequestDispatcher("/error.jsp");
			rd.forward(request, response);
			return;

		} catch (ModelException e) {
                        e.printStackTrace(System.out);
			rd = request.getRequestDispatcher("/error.jsp");
			rd.forward(request, response);
			return;
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
