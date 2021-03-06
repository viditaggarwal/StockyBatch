package com.stocky.batch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionUtil {

	private static final String PASSWORD = "9111@Ykcots";
	private static final String USERNAME = "remoteu";

	private static final String DATABASE_URL = "ec2-34-216-62-227.us-west-2.compute.amazonaws.com";

	private static final int PORT = 3306;
	
//	private static ConnectionUtil m_instance;
//	
//	public static ConnectionUtil getInstance(){
//		if(m_instance == null){
//			synchronized (ConnectionUtil.class) {
//				m_instance = new ConnectionUtil();	
//			}
//		}
//		return m_instance;
//	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + DATABASE_URL + ":" 
					+ Integer.toString(PORT) + "/" + Constants.STOCKYDATABASE, USERNAME,
					PASSWORD);
			con.setAutoCommit(false);
			String query = "SET SQL_SAFE_UPDATES=0";
			PreparedStatement preparedSt = con.prepareStatement(query);
			preparedSt.execute();
			con.commit();
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
