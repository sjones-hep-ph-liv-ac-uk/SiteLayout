package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.controllers.NodeTypeController;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class NodeSet {
	final static Logger logger = Logger.getLogger(NodeSet.class);

	private String nodeSetName;
	private String nodeTypeName;
	private Integer nodeCount;
	private String cluster;

	public NodeSet(String nodeSetName, String nodeTypeName, Integer nodeCount, String cluster) {
		super();
		this.nodeSetName = nodeSetName;
		this.nodeTypeName = nodeTypeName;
		this.nodeCount = nodeCount;
		this.cluster = cluster;
	}

	public String getNodeSetName() {
		return nodeSetName;
	}

	public void setNodeSetName(String nodeSetName) {
		this.nodeSetName = nodeSetName;
	}

	public String getNodeTypeName() {
		return nodeTypeName;
	}

	public void setNodeTypeName(String nodeTypeName) {
		this.nodeTypeName = nodeTypeName;
	}

	public Integer getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public static void deleteNodeSet(HttpServletRequest request, String nodeSet) throws ModelException {
		try {
			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");

			String sqlCommand = "delete from nodeSet where nodeSetName = '" + nodeSet + "'";

			Statement statement = dbConn.conn.createStatement();
			int result = statement.executeUpdate(sqlCommand);
		} catch (Exception e) {
			// MySQLIntegrityConstraintViolationException
			if (e instanceof MySQLIntegrityConstraintViolationException) {
				throw new ModelException("This node set is still being used by a cluster and/or a node type.");
			}
			throw new ModelException("Cannot delete this node set.");
		}
	}

	public static void refreshListOfNodeSets(HttpServletRequest request) throws ModelException {
		try {

			HttpSession session = request.getSession();
			DatabaseConnection dbConn = (DatabaseConnection) session.getAttribute("TheDatabaseConnection");
			ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

			ResultSet r;
			r = dbConn.query("select nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet");
			while (r.next()) {
				NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
						r.getString("cluster"));
				nodeSetList.add(n);

			}
			request.setAttribute("nodeSetList", nodeSetList);
		} catch (Exception e) {
			throw new ModelException("Cannot refresh node set page.");
		}
	}

}
