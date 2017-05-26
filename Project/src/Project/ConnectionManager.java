package Project;

import java.sql.*;



public class ConnectionManager {
	private static Connection con;
	private static String url = "jdbc:mysql://cse.unl.edu:3306/";

	public static Connection getConnection(String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			try {
				con = DriverManager.getConnection(url + user, user, pass);
			} catch (SQLException e) {
				e.printStackTrace(); 
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return con;
	}

}
