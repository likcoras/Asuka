package io.github.likcoras.ssbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ConfigParser {
	
	private static final Logger LOG = Logger.getLogger(ConfigParser.class);
	
	private final String[] keys = { "dbhost", "dbport", "dbuser", "#dbpass",
			"dbdatabase", "dbtable", "dbcolumntitle", "dbcolumnid", "xmlfile",
			"muurl", "bturl", "ircnick", "irclogin", "ircrealname", "irchost",
			"ircport", "ircssl", "#ircnickserv", "#ircpass", "ircchannels",
			"adminpass" };
	
	private final Map<String, String> prop;
	private final File conf;
	
	public ConfigParser() {
		
		prop = new HashMap<String, String>();
		conf = new File("config.txt");
		
	}
	
	public void parse() throws IOException {
		
		LOG.info("Loading configuration...");
		
		createDefault();
		load();
		check();
		
		LOG.info("Configuration loaded successfully!");
		
	}
	
	public String getProperty(final String k) {
		
		return prop.get(k);
		
	}
	
	private void createDefault() throws IOException {
		
		if (conf.exists())
			return;
		
		LOG.info("No configuration file found, writing default configuration...");
		
		conf.createNewFile();
		
		final BufferedReader def =
			new BufferedReader(new InputStreamReader(this.getClass()
				.getClassLoader().getResourceAsStream("config.txt")));
		final BufferedWriter out = new BufferedWriter(new FileWriter(conf));
		
		String line;
		while ((line = def.readLine()) != null)
			out.write(line + "\n");
		
		def.close();
		out.flush();
		out.close();
		
		LOG.info("Default configuration written, please edit the config.txt file.");
		
		System.exit(1);
		
	}
	
	private void load() throws IOException {
		
		final BufferedReader in = new BufferedReader(new FileReader(conf));
		
		String line;
		while ((line = in.readLine()) != null) {
			
			if (line.startsWith("#") || !line.contains(":"))
				continue;
			
			final int i = line.indexOf(":");
			final String key = line.substring(0, i);
			final String val = line.substring(i + 1);
			
			prop.put(key, val);
			
		}
		
		in.close();
		
	}
	
	private void check() {
		
		boolean fail = false;
		for (String key : keys) {
			
			boolean notRequired = false;
			if (key.startsWith("#")) {
				
				key = key.substring(1);
				notRequired = true;
				
			}
			
			if (!prop.containsKey(key) || !notRequired
				&& prop.get(key).isEmpty()) {
				
				LOG.error("Required configuration value '" + key
					+ "' was not found!");
				
				fail = true;
				
			}
			
		}
		
		if (fail)
			System.exit(1);
		
	}
	
}
