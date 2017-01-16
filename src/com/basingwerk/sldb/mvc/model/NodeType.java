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

    private String nodeTypeName;
    private int cpu;
    private int slot;
    private float hs06PerSlot;
    private float memPerNode;

    public static ArrayList<String> listNodeTypes(HttpServletRequest request) throws ModelException {
        ArrayList<String> nt = new ArrayList<String>();
        try {
            HttpSession session = request.getSession();
            DBConnectionHolder dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");
            NodeType c = null;

            ResultSet r = dbHolder.query("select nodeTypeName from nodeType");
            while (r.next()) {
                nt.add(r.getString("nodeTypeName"));
            }

        } catch (Exception e) {
            throw new ModelException("Cannot read node types.");
        }
        return nt;
    }

    public static ArrayList<NodeType> getNodeTypes(HttpServletRequest request) throws ModelException {
        ArrayList<NodeType> nt = new ArrayList<NodeType>();
        try {
            HttpSession session = request.getSession();
            DBConnectionHolder dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");
            NodeType c = null;

            ResultSet r;
            r = dbHolder.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nt.add(n);
            }

        } catch (Exception e) {
            throw new ModelException("Cannot read node types.");
        }
        return nt;
    }

    // ---

    public static void deleteNodeType(HttpServletRequest request, String nodeType) throws ModelException {
        DBConnectionHolder dbHolder = null;
        try {
            HttpSession session = request.getSession();
            dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");

            String sqlCommand = "delete from nodeType where nodeTypeName = '" + nodeType + "'";

            Statement statement = dbHolder.theConnection.createStatement();
            int result = statement.executeUpdate(sqlCommand);
            dbHolder.theConnection.commit();
        } catch (Exception e) {
            try {
                logger.info ("Could not delete node type, rolling back.");
                dbHolder.theConnection.rollback();

            } catch (SQLException e1) {
                logger.error ("Rollback failed, ",e1);
                throw new ModelException("Cannot delete this node type.");
            }
            // MySQLIntegrityConstraintViolationException
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                throw new ModelException("This node type is still being used in a nodeset.");
            }
            throw new ModelException("Cannot delete this node type.");
        }
    }

    public static void setBaselineNodeType(HttpServletRequest request) throws ModelException {
        try {

            HttpSession session = request.getSession();
            DBConnectionHolder dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");

            ResultSet r;
            r = dbHolder.query( "select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where nodeTypeName='BASELINE'");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                request.setAttribute("baseline", n);

            }
        } catch (Exception e) {
            throw new ModelException("Cannot set baseline node type.");
        }
    }

    public static void refreshListOfNodeTypes(HttpServletRequest request) throws ModelException {
        try {

            HttpSession session = request.getSession();
            DBConnectionHolder dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");
            ArrayList<NodeType> nodeTypeList = new ArrayList<NodeType>();

            ResultSet r;
            r = dbHolder.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType");
            while (r.next()) {
                NodeType n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
                nodeTypeList.add(n);

            }
            request.setAttribute("nodeTypeList", nodeTypeList);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh node type page.");
        }
    }

    public static void readNodeType(HttpServletRequest request, String nodeTypeName) throws ModelException {
        try {

            HttpSession session = request.getSession();
            DBConnectionHolder dbHolder = (DBConnectionHolder) session.getAttribute("DBConnHolder");
            NodeType n = null;
            ResultSet r = dbHolder.query("select nodeTypeName,cpu,slot,hs06PerSlot,memPerNode from nodeType where"
                    + "nodeTypeName = '" + nodeTypeName + "'");
            while (r.next()) {
                n = new NodeType(r.getString("nodeTypeName"), r.getInt("cpu"), r.getInt("slot"),
                        r.getFloat("hs06PerSlot"), r.getFloat("memPerNode"));
            }
            request.setAttribute("Nodetype", n);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh Nodetype page.");
        }
    }

}
