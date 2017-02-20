package com.basingwerk.sldb.mvc.model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import com.mysql.jdbc.Connection;

public class AccessObject {

    public AccessObject(String db, String user, String pw ) throws AccessObjectException{
        super();
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/";
        this.db = db;
        this.user = user;
        this.pw = pw;
        final Logger logger = Logger.getLogger(AccessObject.class);
        try {
            java.lang.Class.forName(driver).newInstance();
            this.theConnection = (Connection) DriverManager.getConnection(url + this.db, this.user, this.pw);
            this.theConnection.setAutoCommit(false);

            this.theConnection.commit();        
            java.sql.Statement statement = theConnection.createStatement();
            ResultSet res = statement.executeQuery("select clusterName from cluster");

        } catch (Exception e) {
            logger.error("Failed to connect", e);
            throw new AccessObjectException("Connection problem");
        }
    }

    private String db;
    private String pw;
    private String user;
    public Connection getTheConnection() {
        return theConnection;
    }

    private Connection theConnection;

    public ResultSet query(String query) throws SQLException { 
        this.theConnection.commit();        
        //this.theConnection.setAutoCommit(false);
        java.sql.Statement statement = theConnection.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
}
