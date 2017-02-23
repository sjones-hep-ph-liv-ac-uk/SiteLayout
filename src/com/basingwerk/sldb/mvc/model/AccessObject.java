package com.basingwerk.sldb.mvc.model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import com.mysql.jdbc.Connection;

public class AccessObject {

    final static Logger logger = Logger.getLogger(AccessObject.class);
    private Connection conn;
    private String db;
    private String pw;
    private String user;

    public AccessObject(String db, String user, String pw) throws AccessObjectException {
        super();
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/";
        this.db = db;
        this.user = user;
        this.pw = pw;

        try {
            java.lang.Class.forName(driver).newInstance();
            this.conn = (Connection) DriverManager.getConnection(url + this.db, this.user, this.pw);
            this.conn.setAutoCommit(false);
            java.sql.Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("select clusterName from cluster");
            this.conn.commit();

        } catch (Exception ex) {
            logger.error("Failed to connect ", ex);
            throw new AccessObjectException("Failed to connect");
        }
    }

    public Connection getTheConnection() {
        return conn;
    }

    public ResultSet query(String query) throws SQLException {
        java.sql.Statement statement = conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
}
