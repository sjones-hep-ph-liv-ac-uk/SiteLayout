package com.basingwerk.sldb.mvc.model;

import com.mysql.jdbc.Connection;
import java.sql.*;

/**
 * @desc A singleton database access class for MySQL
 * @author sjones
 */
public final class DatabaseConnection {

	private static String database;
	private static String username;
	private static String password;
	
	public Connection conn;
	private Statement statement;
	public static DatabaseConnection db;
	

	public static void setDatabase(String db) {
		database = db;
	}

	public static void setUsename(String un) {
		username = un;
	}

	public static void setPassword(String pw) {
		password = pw;
	}


	private DatabaseConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		String url = "jdbc:mysql://localhost:3306/";
		String dbName = database;
		String driver = "com.mysql.jdbc.Driver";
		String userName = username;
		String passWord = password;
		Class.forName(driver).newInstance();
		this.conn = (Connection) DriverManager.getConnection(url + dbName, userName, passWord);
	}

	/**
	 *
	 * @return MysqlConnect Database connection object
	 */
	public static synchronized DatabaseConnection getInitialDbCon() {
		try {
			db = new DatabaseConnection();
		} catch (Exception e) {
			return null;
		}

		ResultSet resultset = null;
		try {
			resultset = db.query("select clusterName from cluster");
			while (resultset.next()) {
			}
		} catch (SQLException e) {
			return null;
		}

		return db;
	}

	/**
	 *
	 * @return MysqlConnect Database connection object
	 */
	public static synchronized DatabaseConnection getDbCon() {
		if (db == null) {
			try {
				db = new DatabaseConnection();
			} catch (Exception e) {
				return null;
			}
		}

		ResultSet resultset = null;
		try {
			resultset = db.query("select clusterName from cluster");
			while (resultset.next()) {
			}
		} catch (SQLException e) {
			return null;
		}

		return db;
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
		statement = db.conn.createStatement();
		ResultSet res = statement.executeQuery(query);
		return res;
	}

	/**
	 * @desc Method to insert data to a table
	 * @param insertQuery
	 *            String The Insert query
	 * @return boolean
	 * @throws SQLException
	 */
	public int insert(String insertQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(insertQuery);
		return result;

	}

}
