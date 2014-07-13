package com.mackthehobbit.mbedit.world;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WorldFile {
	
	Connection connection;
	
	public WorldFile(String path) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + path);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
