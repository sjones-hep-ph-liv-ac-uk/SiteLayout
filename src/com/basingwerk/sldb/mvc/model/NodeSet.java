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
        AccessObject dbHolder = null;
        try {
            HttpSession session = request.getSession();
            dbHolder = (AccessObject) session.getAttribute("AccessObject");
            if (dbHolder == null) {
//                logger.error("Access object is null.");
            }
            logger.debug("AccessObject is not null.");
            String sqlCommand = "delete from nodeSet where nodeSetName = '" + nodeSet + "'";

            Statement statement = dbHolder.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            dbHolder.getTheConnection().commit();
        } catch (Exception e) {
            try {
                logger.info ("Could not delete node set, rolling back.");
                dbHolder.getTheConnection().rollback();
            } catch (SQLException e1) {
                logger.error("Rollback failed, " , e1);
                throw new ModelException("Cannot delete this node set.");
            }
            logger.error("Could not delete node set, " , e);
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                throw new ModelException("This node set is still being used by a cluster and/or a node type.");
            }
            throw new ModelException("Cannot delete this node set.");
        }
    }

    public static void refreshListOfNodeSets(HttpServletRequest request) throws ModelException {
        try {
            HttpSession session = request.getSession();
            AccessObject dbHolder = (AccessObject) session.getAttribute("AccessObject");
            ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

            ResultSet r;
            if (dbHolder == null) {
                logger.error("Access object is null.");
            }
            r = dbHolder.query("select nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet");
            while (r.next()) {
                NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
                nodeSetList.add(n);

            }
            logger.info("Suppose did loop...");
            session.setAttribute("nodeSetList", nodeSetList);
            logger.info("Hm...");
        } catch (Exception e) {
            logger.error("Could not refresh the list of node sets, " , e);
            throw new ModelException("Cannot refresh node set page.");
        }
    }
}
