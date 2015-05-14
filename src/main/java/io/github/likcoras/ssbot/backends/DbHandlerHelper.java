package io.github.likcoras.ssbot.backends;

import io.github.likcoras.ssbot.ConfigParser;
import io.github.likcoras.ssbot.backends.exceptions.NoResultsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DbHandlerHelper {
	
	private static final Logger HANDLE = Logger.getLogger("Handler");
	
	private final String queryFormat;
	private final String keywordFormat;
	
	private final String host;
	private final int port;
	
	private final String user;
	private final String pass;
	
	private final String database;
	private final String table;
	private final String titles;
	private final String id;
	
	private Connection connection;
	
	public DbHandlerHelper(final ConfigParser cfg)
		throws ClassNotFoundException {
		
		host = cfg.getProperty("dbhost");
		port = Integer.parseInt(cfg.getProperty("dbport"));
		
		user = cfg.getProperty("dbuser");
		pass = cfg.getProperty("dbpass");
		
		database = cfg.getProperty("dbdatabase");
		table = cfg.getProperty("dbtable");
		titles = cfg.getProperty("dbcolumntitle");
		id = cfg.getProperty("dbcolumnid");
		
		Class.forName("com.mysql.jdbc.Driver");
		
		queryFormat =
			String.format("SELECT * FROM %s.%s WHERE {} LIMIT 0,1", database,
				table);
		keywordFormat = String.format("%s LIKE '%%{}%%' AND ", titles);
		
	}
	
	public int getId(final String[] keywords) throws SQLException,
		NoResultsException {
		
		final String query = getQuery(keywords);
		final ResultSet result = makeQuery(query);
		
		if (!result.next())
			throw new NoResultsException(keywords);
		
		HANDLE.info("Database query '" + query + "' returned data: "
			+ result.getInt(id));
		
		return result.getInt(id);
		
	}
	
	private String getQuery(final String[] keywords) {
		
		final StringBuilder queryBuild = new StringBuilder();;
		
		for (final String keyword : keywords)
			if (keyword != null && !keyword.isEmpty())
				queryBuild.append(keywordFormat.replace("{}", keyword));
		
		final String query = queryBuild.toString();
		
		if (query.isEmpty())
			throw new IllegalArgumentException(
				"There must be at least one valid keyword");
		
		return queryFormat
			.replace("{}", query.substring(0, query.length() - 5));
		
	}
	
	private ResultSet makeQuery(final String query) throws SQLException {
		
		makeCon();
		final Statement state = connection.createStatement();
		
		return state.executeQuery(query);
		
	}
	
	private void makeCon() throws SQLException {
		
		if (connection == null || connection.isClosed())
			connection =
				DriverManager.getConnection(String.format(
					"jdbc:mysql://%s:%d/%s", host, port, database), user, pass);
		
	}
	
}
