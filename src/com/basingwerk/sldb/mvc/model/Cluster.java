package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.controllers.ClusterController;
import com.basingwerk.sldb.mvc.controllers.NodeTypeController;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Cluster {

    final static Logger logger = Logger.getLogger(Cluster.class);

    private String clusterName;
    private String descr;
    private String clusterSetName;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getclusterSetName() {
        return clusterSetName;
    }

    public void setclusterSetName(String clusterSetName) {
        this.clusterSetName = clusterSetName;
    }

    public Cluster(String clusterName, String descr, String clusterSetName) {
        super();
        this.clusterName = clusterName;
        this.descr = descr;
        this.clusterSetName = clusterSetName;
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
    
    public static ArrayList<Cluster> queryCluster (HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
        try {
            ResultSet r = modelAo.query("select clusterName,descr, clusterSetName from cluster");
            while (r.next()) {
                Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
                clusterList.add(c);
            }
        } 
        
        catch (SQLException e) {
            logger.info("Could not read the node sets, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
        return clusterList;
    }
    

    public static void refreshListOfAllClusters(HttpServletRequest request, String col, String order)
            throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
            ResultSet r;
            r = modelAo.query("select clusterName,descr,clusterSetName from cluster order by " + col + " " + order);
            while (r.next()) {
                Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
                clusterList.add(c);
            }
            request.setAttribute("clusterList", clusterList);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh cluster page");
        }
    }
    
    public static void refreshClusterSet(HttpServletRequest request, String clusterSetName, String col, String order)
            throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
            ResultSet r;
            
            
            r = modelAo.query("select clusterName,descr,clusterSetName from cluster where clusterSetName='" + clusterSetName + "' order by " + col + " " + order);
            while (r.next()) {
                Cluster c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
                clusterList.add(c);
            }
            request.setAttribute("clusterList", clusterList);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh cluster page");
        }
    }

    public static void addCluster(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");

        String sqlCommand = "INSERT INTO cluster (clusterName, Descr, clusterSetName) VALUES " + "('" + clusterName + "','"
                + descr + "','" + clusterSetName + "')";
        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException e) {
            logger.info("Could not add new cluster, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);

        }
    }

    public static void updateSingleCluster(HttpServletRequest request, Cluster cluster) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        // AccessObject modelAo = null;
        try {
            RequestDispatcher rd = null;
            HttpSession session = request.getSession();

            String clusterName = cluster.getCluster();
            String descr = cluster.getDescr();
            String clusterSetName = cluster.getclusterSetName();

            String sqlCommand = "UPDATE cluster SET descr='" + descr + "', clusterSetName='" + clusterSetName
                    + "' WHERE clusterName = '" + clusterName + "'";

            java.sql.Statement statement;
            int result = -1;

            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException e) {
            logger.info("Problem Updating cluster, rolling back ", e);
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed, ", ex);
                throw new ModelException("Failed to update the cluster");
            }
            throw new ModelException("Problem updating the cluster. Please try again.");
        }
    }

    public static void setSingleCluster(HttpServletRequest request, String cluster) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            ResultSet r;
            r = modelAo.query("select clusterName,descr,clusterSetName from cluster where clusterName = '" + cluster + "'");
            Cluster c = null;
            while (r.next()) {
                c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
            }
            request.setAttribute("cluster", c);
            modelAo.getTheConnection().commit();
        } catch (Exception e) {
            logger.info("Could not set cluster, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed, ", ex);
                throw new ModelException("Cannot refresh single cluster page.");
            }
            throw new ModelException("Cannot refresh single cluster page.");
        }
        return;
    }

    public static void deleteCluster(HttpServletRequest request, String cluster) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
//            HttpSession session = request.getSession();

            String sqlCommand = "delete from cluster where clusterName = '" + cluster + "'";

            Statement statement = modelAo.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not delete cluster, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

//    public static void getOneCluster(AccessObject modelAo, HttpServletRequest request, String clusterName)
//            throws ModelException {
//        try {
//
//
//            Cluster c = null;
//            ResultSet r = modelAo
//                    .query("select clusterName,descr,clusterSetName from cluster where clusterName = '" + clusterName + "'");
//            while (r.next()) {
//                c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
//            }
//            request.setAttribute("cluster", c);
//        } catch (Exception e) {
//            throw new ModelException("Cannot refresh cluster page.");
//        }
//    }

//    public static ArrayList<Cluster> getAllClusters(AccessObject modelAo, HttpServletRequest request)
//            throws ModelException {
//        ArrayList<Cluster> cl = new ArrayList<Cluster>();
//        try {
//
//            Cluster c = null;
//            ResultSet r = modelAo.query("select clusterName,descr,clusterSetName from cluster");
//            while (r.next()) {
//                c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
//                cl.add(c);
//            }
//
//        } catch (Exception e) {
//            throw new ModelException("Cannot get clusters.");
//        }
//        return cl;
//    }
    
    public static ArrayList<String> listClusterSetNames(HttpServletRequest request, String clusterSetName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<String> cl = new ArrayList<String>();
        try {

            Cluster c = null;

            ResultSet r = modelAo.query("select clusterName,descr,clusterSetName from cluster where clusterSetName='" + clusterSetName + "'");
            while (r.next()) {
                c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
                cl.add(c.getCluster());
            }
        } catch (Exception e) {
            throw new ModelException("Cannot list clusters.");
        }
        return cl;
    }

    public static ArrayList<String> listAllClusterNames(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<String> cl = new ArrayList<String>();
        try {

            Cluster c = null;

            ResultSet r = modelAo.query("select clusterName,descr,clusterSetName from cluster");
            while (r.next()) {
                c = new Cluster(r.getString("clusterName"), r.getString("descr"), r.getString("clusterSetName"));
                cl.add(c.getCluster());
            }
        } catch (Exception e) {
            throw new ModelException("Cannot list clusters.");
        }
        return cl;
    }

    public String toString() {
        return clusterName + " " + descr + " " + clusterSetName;
    }

}
