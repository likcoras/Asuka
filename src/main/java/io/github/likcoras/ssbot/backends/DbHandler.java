package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.IllegalFormatException;

public class DbHandler {
	
	private final String host;
	private final int port;
	
	private final String user;
	private final String pass;
	
	private final String database;
	private final String table;
	private final String titles;
	private final String id;
	
	public DbHandler(final ConfigParser cfg) {
		
		host = cfg.getString("dbhost");
		port = Integer.parseInt(cfg.getString("dbport"));
		
		user = cfg.getString("dbuser");
		pass = cfg.getString("dbpass");
		
		database = cfg.getString("dbdatabase");
		table = cfg.getString("dbtable");
		titles = cfg.getString("dbcolumntitle");
		id = cfg.getString("dbcolumnid");
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (final ClassNotFoundException e) {
			
			System.out.println("Error: Unable to load mysql driver!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
	}
	
	public int getId(final String... s) {
		
		int i = -1;
		String qr = "";
		
		for (final String k : s) {
			
			qr += String.format("%s LIKE '%%%s%%' AND ", titles, k);
			
		}
		
		qr =
				String.format("SELECT * FROM %s.%s WHERE %s LIMIT 0,1",
						database, table, qr.substring(0, qr.length() - 5));
		
		try {
			
			final Connection con = makeCon();
			
			final Statement st = con.createStatement();
			final ResultSet res = st.executeQuery(qr);
			
			if (res.next()) {
				
				i = res.getInt(id);
				
			}
			
			con.close();
			
		} catch (final SQLException e) {
			
			System.out
					.println("Error: Exception while querying the database!!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			i = -2;
			
		}
		
		return i;
		
	}
	
	private Connection makeCon() {
		
		try {
			
			return DriverManager.getConnection(String.format(
					"jdbc:mysql://%s:%d/%s", host, port, database), user, pass);
			
		} catch (final SQLException e) {
			
			System.out
					.println("Error: Unable to establish connection to server!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		} catch (final IllegalFormatException e) {
			
			System.out.println("Error: Wrong format on db configuration!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
		return null;
		
	}
	
}
