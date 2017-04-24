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

    public static NodeSet queryOneNodeSet(HttpServletRequest request, String nodeSetName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        NodeSet nodeSet = null;
        ResultSet r;
        String sql = "select  nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet where" + " nodeSetName = '"
                + nodeSetName + "'";
        try {
            r = modelAo.query(sql);

            while (r.next()) {
                nodeSet = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
            }
        } catch (SQLException e) {
            logger.info("Could not read the node set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }

        return nodeSet;

    }

    public static ArrayList<NodeSet> queryNodeSetList(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

        ResultSet r;
        try {
            r = modelAo.query("select nodeSetName ,nodeTypeName, nodeCount, cluster from nodeSet");
            while (r.next()) {
                NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
                nodeSetList.add(n);

            }
        } catch (SQLException e) {
            logger.info("Could not read the node sets, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }

        return nodeSetList;

    }

    public static void addNodeSet(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String clusterName = request.getParameter("clusterList");
        String nodeTypeName = request.getParameter("nodeTypeList");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String sqlCommand = "INSERT INTO nodeSet (nodeSetName ,nodeTypeName, nodeCount, cluster) VALUES" + "('"
                + nodeSetName + "','" + nodeTypeName + "'," + nodeCount + ",'" + clusterName + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        }

        catch (SQLException e) {
            logger.info("Could not add new node set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
    }

    public static void updateNodeSet(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        String sqlCommand = "UPDATE nodeSet SET nodeSetName='" + nodeSetName + "', nodeTypeName='" + nodeTypeName
                + "', nodeCount='" + nodeCount + "', cluster='" + clusterName + "' where nodeSetName='" + nodeSetName
                + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not edit that node set, rollback", ex);
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex2) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex2);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void deleteNodeSet(HttpServletRequest request, String nodeSet) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            String sqlCommand = "delete from nodeSet where nodeSetName = '" + nodeSet + "'";

            Statement statement = modelAo.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (Exception e) {
            logger.info("Could not delete node set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
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

    public static void refreshListOfNodeSets(HttpServletRequest request, String col, String order)
            throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            ArrayList<NodeSet> nodeSetList = new ArrayList<NodeSet>();

            ResultSet r;
            if (modelAo == null) {
                logger.error("Access object is null.");
            }
            r = modelAo.query(
                    "select nodeSetName, nodeTypeName ,nodeCount, cluster from nodeSet order by " + col + " " + order);
            while (r.next()) {
                NodeSet n = new NodeSet(r.getString("nodeSetName"), r.getString("nodeTypeName"), r.getInt("nodeCount"),
                        r.getString("cluster"));
                nodeSetList.add(n);
            }
            request.getSession().setAttribute("nodeSetList", nodeSetList);
        } catch (Exception e) {
            logger.error("Could not refresh the list of node sets, ", e);
            throw new ModelException("Cannot refresh node set page");
        }
    }
}
