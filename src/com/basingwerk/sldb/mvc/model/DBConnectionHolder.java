package com.basingwerk.sldb.mvc.model;

import com.mysql.jdbc.Connection;
import org.apache.log4j.Logger;
import java.sql.*;

/**
 * @desc A singleton database access class for MySQL
 * @author sjones
 */
public final class DBConnectionHolder {

    private static DBConnectionHolder singleConnectionHolder;

    public static DBConnectionHolder getSingleConnectionHolder() {
        return singleConnectionHolder;
    }

    public static void setSingleConnectionHolder(DBConnectionHolder singleConnectionHolder) {
        DBConnectionHolder.singleConnectionHolder = singleConnectionHolder;
    }

    public Connection theConnection;

    final static Logger logger = Logger.getLogger(DBConnectionHolder.class);

    private static String databaseId;
    private static String username;
    private static String password;

    private Statement statement;

    public static void setDatabase(String db) {
        databaseId = db;
    }

    public static void setUsename(String un) {
        username = un;
    }

    public static void setPassword(String pw) {
        password = pw;
    }

    private DBConnectionHolder()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        String url = "jdbc:mysql://localhost:3306/";
        String dbName = databaseId;
        String driver = "com.mysql.jdbc.Driver";
        String userName = username;
        String passWord = password;
        Class.forName(driver).newInstance();
        this.theConnection = (Connection) DriverManager.getConnection(url + dbName, userName, passWord);
        this.theConnection.setAutoCommit(false);
    }

    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized DBConnectionHolder getInitialDbCon() {
        try {
            singleConnectionHolder = new DBConnectionHolder();
        } catch (Exception e) {
            return null;
        }

        ResultSet resultset = null;
        try {
            resultset = singleConnectionHolder.query("select clusterName from cluster");
            while (resultset.next()) {
            }
        } catch (SQLException e) {
            logger.error("Failed when connecting to DB");
            return null;
        }
        logger.info("Connected to DB");

        return singleConnectionHolder;
    }

    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized DBConnectionHolder getDbCon() {
        if (singleConnectionHolder == null) {
            try {
                singleConnectionHolder = new DBConnectionHolder();
            } catch (Exception e) {
                logger.error("Failed when connecting to DB");
                return null;
            }
        }

        ResultSet resultset = null;
        try {
            resultset = singleConnectionHolder.query("select clusterName from cluster");
            while (resultset.next()) {
            }
        } catch (SQLException e) {
            logger.error("Failed when connecting to DB");
            return null;
        }
        logger.info("Connected to DB");
        return singleConnectionHolder;
    }

    /**
     *
     * @param query
     *            String The query to be executed
     * @return a ResultSet object containing the results or null if not
     *         available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException {
        this.theConnection.setAutoCommit(false);
        statement = singleConnectionHolder.theConnection.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
}
