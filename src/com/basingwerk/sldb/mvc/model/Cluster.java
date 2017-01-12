package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.controllers.NodeTypeController;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Cluster {
	final static Logger logger = Logger.getLogger(NodeTypeController.class);

	private String clusterName;
	private String descr;

	public Cluster(String clusterName, String descr) {
		super();
		this.clusterName = clusterName;
		this.descr = descr;
	}

	public String toString() {
		return clusterName + " " + descr;
	}

	public String getCluster() {
		return clusterName;
	}

	public void setCluster(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public static void setListOfClusters(HttpServletRequest request) throws ModelException {
		try {

			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
			ResultSet r;
			r = dbConn.query("select clusterName,descr from cluster");
			while (r.next()) {
				Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"));
				clusterList.add(c);
			}
			request.setAttribute("clusterList", clusterList);
		} catch (Exception e) {
			throw new ModelException("Cannot refresh cluster page.");
		}
	}

	public static void updateSingleCluster(HttpServletRequest request, Cluster cluster) throws ModelException {
		try {

			DatabaseConnection dbConn = null;
			RequestDispatcher rd = null;
			HttpSession session = request.getSession();
			dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");

			String clusterName = cluster.getCluster();
			String descr = cluster.getDescr();

			String sqlCommand = "UPDATE cluster SET descr='" + descr + "' WHERE clusterName = '" + clusterName + "'";

			java.sql.Statement statement;
			int result = -1;

			statement = dbConn.conn.createStatement();
			result = statement.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			throw new ModelException("Failed to update the cluster");
		}

	}

	public static void setSingleCluster(HttpServletRequest request, String cluster) throws ModelException {
		try {
			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			if (dbConn != null) {
				ResultSet r;
				r = dbConn.query("select clusterName,descr from cluster where clusterName = '" + cluster + "'");
				Cluster c = null;
				while (r.next()) {
					c = new Cluster(r.getString("clusterName"), r.getString("descr"));
				}
				request.setAttribute("cluster", c);
			}
		} catch (Exception e) {
			throw new ModelException("Cannot refresh single cluster page.");
		}
		return;
	}

	public static void deleteCluster(HttpServletRequest request, String cluster) throws ModelException {
		try {
			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");

			String sqlCommand = "delete from cluster where clusterName = '" + cluster + "'";

			Statement statement = dbConn.conn.createStatement();
			int result = statement.executeUpdate(sqlCommand);
		} catch (Exception e) {
			if (e instanceof MySQLIntegrityConstraintViolationException) {
				throw new ModelException("This cluster still has nodes.");
			}
			throw new ModelException("Cannot delete this cluster.");
		}
	}

	public static void readCluster(HttpServletRequest request, String clusterName) throws ModelException {
		try {

			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			Cluster c = null;
			ResultSet r = dbConn
					.query("select clusterName,descr from cluster where clusterName = '" + clusterName + "'");
			while (r.next()) {
				c = new Cluster(r.getString("clusterName"), r.getString("descr"));
			}
			request.setAttribute("cluster", c);
		} catch (Exception e) {
			throw new ModelException("Cannot refresh cluster page.");
		}
	}

	public static ArrayList<Cluster> getClusters(HttpServletRequest request) throws ModelException {
		ArrayList<Cluster> cl = new ArrayList<Cluster>();
		try {
			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			Cluster c = null;
			ResultSet r = dbConn.query("select clusterName,descr from cluster");
			while (r.next()) {
				c = new Cluster(r.getString("clusterName"), r.getString("descr"));
				cl.add(c);
			}

		} catch (Exception e) {
			throw new ModelException("Cannot get clusters.");
		}
		return cl;
	}

	public static ArrayList<String> listClusters(HttpServletRequest request) throws ModelException {
		ArrayList<String> cl = new ArrayList<String>();
		try {
			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			Cluster c = null;

			ResultSet r = dbConn.query("select clusterName,descr from cluster");
			while (r.next()) {
				c = new Cluster(r.getString("clusterName"), r.getString("descr"));
				cl.add(c.getCluster());
			}
		} catch (Exception e) {
			throw new ModelException("Cannot list clusters.");
		}
		return cl;
	}
}
