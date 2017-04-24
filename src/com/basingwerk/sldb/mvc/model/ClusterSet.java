package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class ClusterSet {
    final static Logger logger = Logger.getLogger(ClusterSet.class);
    private String clusterSetName;
    private String description;
    private String location;
    private Float longitude;
    private Float latitude;
    private String admin;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getClusterSetName() {
        return clusterSetName;
    }

    public void setclusterSetName(String clusterSetName) {
        this.clusterSetName = clusterSetName;
    }

    public static ClusterSet queryOneClusterSet(HttpServletRequest request, String clusterSetName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ResultSet r;
        String sql = "select clusterSetName,description,location,longitude, latitude, admin from clusterSet where" + " clusterSetName = '"
                + clusterSetName + "'";
        ClusterSet clusterSet = null;
        try {
            r = modelAo.query(sql);
            while (r.next()) {
                clusterSet = new ClusterSet(r.getString("clusterSetName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
            }
        }

        catch (SQLException e) {
            logger.info("Could not read the cluster set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
        return clusterSet;

    }

    public static ArrayList<ClusterSet> queryClusterSetList(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<ClusterSet> clusterSetList = new ArrayList<ClusterSet>();
        ResultSet r;
        try {
            r = modelAo.query("select clusterSetName,description,location,longitude,latitude,admin from clusterSet");
            while (r.next()) {
                ClusterSet n = new ClusterSet(r.getString("clusterSetName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
                clusterSetList.add(n);
            }
        } catch (SQLException e) {
            logger.info("Could not read the cluster sets, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
        return clusterSetList;

    }

    public ClusterSet(String clusterSetName, String description, String location, Float longitude, Float latitude, String admin) {
        super();
        this.clusterSetName = clusterSetName;
        this.description = description;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.admin = admin;
    }

    public static ArrayList<String> listAllClusterSets(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        ArrayList<String> clusterSetNameList = new ArrayList<String>();
        try {

            ClusterSet s = null;

            ResultSet r = modelAo.query("select clusterSetName, description ,location ,longitude ,latitude ,admin from clusterSet");
            while (r.next()) {
                clusterSetNameList.add(r.getString("clusterSetName"));
            }
        } catch (Exception e) {
            throw new ModelException("Cannot read cluster sets");
        }
        return clusterSetNameList;
    }

//    public static ArrayList<ClusterSet> getAllClusterSets(HttpServletRequest request) throws ModelException {
//        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
//        ArrayList<ClusterSet> clusterSetList = new ArrayList<ClusterSet>();
//        try {
//
//            ResultSet r;
//            r = modelAo.query("select clusterSetName, description ,location ,longitude ,latitude ,admin from clusterSet");
//            while (r.next()) {
//                ClusterSet n = new ClusterSet(r.getString("clusterSetName"), r.getString("description "), r.getString("location "),
//                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
//                clusterSetList.add(n);
//            }
//
//        } catch (Exception e) {
//            throw new ModelException("Cannot read cluster sets");
//        }
//        return clusterSetList;
//    }

    public static void addClusterSet(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        String sqlCommand = "INSERT INTO clusterSet (clusterSetName,description,location,longitude,latitude,admin) VALUES ('"
                + clusterSetName + "','" + description + "','" + location + "','" + longitude + "','" + longitude + "','"
                + admin + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not add cluster set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void updateClusterSet(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        String sqlCommand = "UPDATE clusterSet set description='" + description + "', location='" + location
                + "', longitude='" + longitude + "', latitude='" + latitude + "', admin='" + admin
                + "' where clusterSetName='" + clusterSetName + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.error("SJDEBUG: " + sqlCommand);
            logger.info("Could not update cluster set, rolling back, ", ex);
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void deleteClusterSet(HttpServletRequest request, String clusterSet) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            HttpSession session = request.getSession();

            String sqlCommand = "delete from clusterSet where clusterSetName = '" + clusterSet + "'";

            Statement statement = modelAo.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not update cluster set, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void refreshClusterSets(HttpServletRequest request, String col, String order) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            ArrayList<ClusterSet> clusterSetList = new ArrayList<ClusterSet>();

            ResultSet r;
            r = modelAo.query("select clusterSetName,description,location,longitude,latitude,admin from clusterSet  order by " + col
                    + " " + order);
            while (r.next()) {
                ClusterSet s = new ClusterSet(r.getString("clusterSetName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
                clusterSetList.add(s);
            }
            request.setAttribute("clusterSetList", clusterSetList);
        } catch (Exception e) {
            logger.error("Had an error when trying to refresh cluster set, ", e);
            throw new ModelException("Cannot refresh ClusterSet page");
        }
    }

//    public static void getSingleClusterSet(HttpServletRequest request, String clusterSetName) throws ModelException {
//        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
//        try {
//
//            ClusterSet s = null;
//            ResultSet r = modelAo
//                    .query("select clusterSetName, description ,location ,longitude ,latitude ,admin from clusterSet where"
//                            + "clusterSetName = '" + clusterSetName + "'");
//            while (r.next()) {
//                s = new ClusterSet(r.getString("clusterSetName"), r.getString("description "), r.getString("location "),
//                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
//            }
//            request.setAttribute("clusterSet", s);
//        } catch (Exception e) {
//            throw new ModelException("Cannot refresh ClusterSet page");
//        }
//    }
    
    public String toString() {
        String result;
        result = clusterSetName.toString() + " " + description.toString() + " " + location.toString() + " "
                + longitude.toString() + " " + latitude.toString() + " " + admin.toString();
        return result;
    }
    
}
