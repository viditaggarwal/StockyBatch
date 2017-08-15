package com.stocky.batch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	private static final String PASSWORD = "password";
	private static final String USERNAME = "remoteu";

	private static final String DATABASE_URL = "ec2-52-41-67-9.us-west-2.compute.amazonaws.com";

	private static final int PORT = 3306;

	public static Connection getConnection(String databaseName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + DATABASE_URL + ":" + Integer.toString(PORT) + "/" + databaseName, USERNAME,
					PASSWORD);
			return con;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
