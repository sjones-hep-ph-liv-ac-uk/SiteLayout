package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        AccessObject ao = null;
        try {
            HttpSession session = request.getSession();
            ao = (AccessObject) session.getAttribute("AccessObject");
            String sqlCommand = "delete from nodeSet where nodeSetName = '" + nodeSet + "'";

            Statement statement = ao.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            ao.getTheConnection().commit();
        } catch (Exception e) {
            logger.info("Could not delete node set, rolling back.");
            try {
                ao.getTheConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed, ", ex);
                throw new ModelException("Cannot delete this node set");
            }
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                throw new ModelException("This node set is still being used by a cluster and/or a node type");
            }
            throw new ModelException("Cannot delete this node set");
        }
    }

    public static void refreshListOfNodeSets(HttpServletRequest request) throws ModelException {
        try {
            HttpSession session = request.getSession();
            AccessObject ao = (AccessObject) session.getAttribute("AccessObject");
            ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

            ResultSet r;
            if (ao == null) {
                logger.error("Access object is null.");
            }
            r = ao.query("select nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet");
            while (r.next()) {
                NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
                nodeSetList.add(n);

            }
            session.setAttribute("nodeSetList", nodeSetList);
        } catch (Exception e) {
            logger.error("Could not refresh the list of node sets, ", e);
            throw new ModelException("Cannot refresh node set page");
        }
    }
}
