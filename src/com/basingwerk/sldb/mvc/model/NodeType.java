package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class NodeType {
    final static Logger logger = Logger.getLogger(NodeType.class);
    private String nodeTypeName;
    private int cpu;
    private int slot;
    private float hs06PerSlot;
    private float memPerNode;

    public String toString() {
        String result;
        result = nodeTypeName + Integer.toString(cpu) + Integer.toString(slot) + Float.toString(hs06PerSlot)
                + Float.toString(memPerNode);
        return result;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public float getHs06PerSlot() {
        return hs06PerSlot;
    }

    public void setHs06PerSlot(float hs06PerSlot) {
        this.hs06PerSlot = hs06PerSlot;
    }

    public float getMemPerNode() {
        return memPerNode;
    }

    public void setMemPerNode(float memPerNode) {
        this.memPerNode = memPerNode;
    }

    public NodeType(String nodeTypeName, int cpu, int slot, float hs06PerSlot, float memPerNode) {
        super();
        this.nodeTypeName = nodeTypeName;
        this.cpu = cpu;
        this.slot = slot;
        this.hs06PerSlot = hs06PerSlot;
        this.memPerNode = memPerNode;
    }

    public static ArrayList<String> listAllNodeTypes(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<String> nt = new ArrayList<String>();
        try {

            ResultSet r = modelAo.query("select nodeTypeName from nodeType");
            while (r.next()) {
                nt.add(r.getString("nodeTypeName"));
            }

        } catch (Exception e) {
            throw new ModelException("Cannot read node types");
        }
        return nt;
    }

    public static ArrayList<NodeType> getAllNodeTypes(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<NodeType> nt = new ArrayList<NodeType>();
        try {

            NodeType c = null;

            ResultSet r;
            r = modelAo.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nt.add(n);
            }

        } catch (Exception e) {
            throw new ModelException("Cannot read node types");
        }
        return nt;
    }

    public static NodeType queryOneNodeType(HttpServletRequest request, String nodeTypeName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        NodeType n = null;
        ResultSet r;
        String sql = "select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where" + " nodeTypeName = '"
                + nodeTypeName + "'";
        NodeType nt = null;
        try {
            r = modelAo.query(sql);
            while (r.next()) {
                nt = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
            }

        } catch (SQLException e) {
            logger.info("Could not read the node type, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }

        return n;
    }

    public static ArrayList<NodeType> queryNodeTypeList(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();
        ResultSet r;
        try {
            r = modelAo.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nodeTypeList.add(n);

            }
        } catch (SQLException e) {
            logger.info("Could not read the node types, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }

        return nodeTypeList;
    }

    public static void addNodeType(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        String sqlCommand = "INSERT INTO nodeType (nodeTypeName,cpu,slot,hs06PerSlot,memPerNode) VALUES ('"
                + nodeTypeName + "','" + cpu + "','" + slot + "','" + hs06PerSlot + "','" + memPerNode + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();

        } catch (SQLException e) {
            logger.info("Could not add new node set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
    }

    public static void updateNodeType(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        String sqlCommand = "UPDATE nodeType set cpu='" + cpu + "', slot='" + slot + "', hs06PerSlot='" + hs06PerSlot
                + "', memPerNode='" + memPerNode + "' where nodeTypeName='" + nodeTypeName + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not update node type, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void deleteNodeType(HttpServletRequest request, String nodeType) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            String sqlCommand = "delete from nodeType where nodeTypeName = '" + nodeType + "'";

            Statement statement = modelAo.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (Exception e) {
            logger.info("Could not delete node type, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed, ", ex);
                throw new ModelException("Cannot delete this node type");
            }
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                throw new ModelException("This node type is still being used in a nodeset");
            }
            throw new ModelException("Cannot delete this node type");
        }
    }

    public static void setBaselineNodeType(HttpServletRequest request) throws ModelException {
        try {

            AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
            if (modelAo == null) {
                throw new ModelException("No access to database");
            }

            ResultSet r;
            r = modelAo.query(
                    "select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where nodeTypeName='BASELINE'");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                request.setAttribute("baseline", n);

            }
        } catch (Exception e) {
            throw new ModelException("Cannot set baseline node type");
        }
    }

    public static void refreshListOfNodeTypes(HttpServletRequest request, String col, String order)
            throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            if (modelAo == null) {
                throw new ModelException("No access to database");
            }

            ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();

            ResultSet r;
            r = modelAo.query(
                    "select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType order by " + col + " " + order);
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nodeTypeList.add(n);

            }
            request.setAttribute("nodeTypeList", nodeTypeList);
        } catch (Exception e) {
            logger.error("Had an error when trying to refresh node type, ", e);
            throw new ModelException("Cannot refresh node type page");
        }
    }

    public static void getSingleNodeType(HttpServletRequest request, String nodeTypeName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            if (modelAo == null) {
                throw new ModelException("No access to database");
            }

            NodeType n = null;
            ResultSet r = modelAo.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where"
                    + "nodeTypeName = '" + nodeTypeName + "'");
            while (r.next()) {
                n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
            }
            request.setAttribute("nodetype", n);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh Nodetype page");
        }
    }
}
