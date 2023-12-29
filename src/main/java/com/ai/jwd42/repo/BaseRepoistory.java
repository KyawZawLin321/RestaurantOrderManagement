package com.ai.jwd42.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseRepoistory {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant_order_management";
	private static final String DB_UserName = "root";
	private static final String DB_UserPassword = "ggwpnoob321";

	private static Connection con;

	public Connection getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (con == null) {
				con = DriverManager.getConnection(DB_URL, DB_UserName, DB_UserPassword);
			} else {
				return con;
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Driver class not found");
		} catch (SQLException e) {
			System.out.println("Database Connection not found");
		}
		return con;
	}

}
