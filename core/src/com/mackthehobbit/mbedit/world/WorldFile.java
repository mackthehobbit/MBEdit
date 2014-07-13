package com.mackthehobbit.mbedit.world;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mackthehobbit.mbedit.util.Point;

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
	
	public boolean isChunkPresent(Point point) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM minebuilder WHERE minebuilder.blockname = \"%d_%d_%d\"", point.x, point.y, point.z));
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
