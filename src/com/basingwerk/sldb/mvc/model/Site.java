package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Site {
    final static Logger logger = Logger.getLogger(Site.class);
    private String siteName;
    private String description;
    private String location;
    private Float longitude;
    private Float latitude;
    private String admin;

    public String toString() {
        String result;
        result = siteName.toString() + " " + description.toString() + " " + location.toString() + " "
                + longitude.toString() + " " + latitude.toString() + " " + admin.toString();
        return result;
    }

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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public static Site queryOneSite(HttpServletRequest request, String siteName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ResultSet r;
        String sql = "select siteName,description,location,longitude, latitude, admin from site where" + " siteName = '"
                + siteName + "'";
        Site site = null;
        try {
            r = modelAo.query(sql);
            while (r.next()) {
                site = new Site(r.getString("siteName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
            }
        }

        catch (SQLException e) {
            logger.info("Could not read the site, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
        return site;

    }

    public static ArrayList<Site> querySiteList(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<Site> siteList = new ArrayList<Site>();
        ResultSet r;
        try {
            r = modelAo.query("select siteName,description,location,longitude,latitude,admin from site");
            while (r.next()) {
                Site n = new Site(r.getString("siteName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
                siteList.add(n);
            }
        } catch (SQLException e) {
            logger.info("Could not read the sites, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex);
            }
            throw new ModelExceptionRollbackFailed("Rollback worked", null);
        }
        return siteList;

    }

    public Site(String siteName, String description, String location, Float longitude, Float latitude, String admin) {
        super();
        this.siteName = siteName;
        this.description = description;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.admin = admin;
    }

    public static ArrayList<String> listAllSites(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        ArrayList<String> siteNameList = new ArrayList<String>();
        try {

            Site s = null;

            ResultSet r = modelAo.query("select siteName, description ,location ,longitude ,latitude ,admin from site");
            while (r.next()) {
                siteNameList.add(r.getString("siteName"));
            }
        } catch (Exception e) {
            throw new ModelException("Cannot Read sites");
        }
        return siteNameList;
    }

    public static ArrayList<Site> getAllSites(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        ArrayList<Site> siteList = new ArrayList<Site>();
        try {

            ResultSet r;
            r = modelAo.query("select siteName, description ,location ,longitude ,latitude ,admin from site");
            while (r.next()) {
                Site n = new Site(r.getString("siteName"), r.getString("description "), r.getString("location "),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
                siteList.add(n);
            }

        } catch (Exception e) {
            throw new ModelException("Cannot read sites");
        }
        return siteList;
    }

    public static void addSite(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");

        String siteName = request.getParameter("siteName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        String sqlCommand = "INSERT INTO site (siteName,description,location,longitude,latitude,admin) VALUES ('"
                + siteName + "','" + description + "','" + location + "','" + longitude + "','" + longitude + "','"
                + admin + "')";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not add site, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void updateSite(HttpServletRequest request) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        String siteName = request.getParameter("siteName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        String sqlCommand = "UPDATE site set description='" + description + "', location='" + location
                + "', longitude='" + longitude + "', latitude='" + latitude + "', admin='" + admin
                + "' where siteName='" + siteName + "'";

        java.sql.Statement statement;
        int result = -1;

        try {
            statement = modelAo.getTheConnection().createStatement();
            result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not update site, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void deleteSite(HttpServletRequest request, String site) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {
            HttpSession session = request.getSession();

            String sqlCommand = "delete from site where siteName = '" + site + "'";

            Statement statement = modelAo.getTheConnection().createStatement();
            int result = statement.executeUpdate(sqlCommand);
            modelAo.getTheConnection().commit();
        } catch (SQLException ex) {
            logger.info("Could not update site, rolling back.");
            try {
                modelAo.getTheConnection().rollback();
            } catch (SQLException ex1) {
                throw new ModelExceptionRollbackFailed("Rollback failed", ex1);
            }
            throw new ModelExceptionRollbackWorked("Rollback worked", null);
        }
    }

    public static void refreshListOfSites(HttpServletRequest request, String col, String order) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            ArrayList<Site> siteList = new ArrayList<Site>();

            ResultSet r;
            r = modelAo.query("select siteName,description,location,longitude,latitude,admin from site  order by " + col
                    + " " + order);
            while (r.next()) {
                Site s = new Site(r.getString("siteName"), r.getString("description"), r.getString("location"),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
                // logger.error("HAVING A LOOK ..." + s.toString());
                siteList.add(s);
            }
            request.setAttribute("siteList", siteList);
        } catch (Exception e) {
            logger.error("Had an error when trying to refresh site, ", e);
            throw new ModelException("Cannot refresh site page");
        }
    }

    public static void getSingleSite(HttpServletRequest request, String siteName) throws ModelException {
        AccessObject modelAo = (AccessObject) request.getSession().getAttribute("accessObject");
        try {

            Site s = null;
            ResultSet r = modelAo
                    .query("select siteName, description ,location ,longitude ,latitude ,admin from site where"
                            + "siteName = '" + siteName + "'");
            while (r.next()) {
                s = new Site(r.getString("siteName"), r.getString("description "), r.getString("location "),
                        r.getFloat("longitude"), r.getFloat("latitude"), r.getString("admin"));
            }
            request.setAttribute("site", s);
        } catch (Exception e) {
            throw new ModelException("Cannot refresh Site page");
        }
    }
}
