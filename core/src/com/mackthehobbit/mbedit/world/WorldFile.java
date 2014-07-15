package com.mackthehobbit.mbedit.world;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

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
	
	public boolean isChunkPresent(int x, int y, int z) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM minebuilder WHERE minebuilder.blockname = \"%d_%d_%d\"", x, y, z));
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public DataInputStream loadChunk(int x, int y, int z) {
		try {
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(String.format("SELECT * FROM minebuilder WHERE minebuilder.blockname = \"%d_%d_%d\"", x, y, z));
			if(set.next()) {
				byte[] compressedBytes = set.getBytes("blockdata");
				DataInputStream compressedBuf = new DataInputStream(new ByteArrayInputStream(compressedBytes));
				
				int orig_size = compressedBuf.readInt();
				int comp_size = compressedBuf.readInt();
				byte[] inflatedBytes = new byte[orig_size];
				
				Inflater inflater = new Inflater();
				inflater.setInput(compressedBytes, 8, comp_size);
				inflater.inflate(inflatedBytes, 0, orig_size);
				
				return new DataInputStream(new ByteArrayInputStream(inflatedBytes));
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (DataFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
