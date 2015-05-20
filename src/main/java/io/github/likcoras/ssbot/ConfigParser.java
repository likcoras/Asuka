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
		"dbdatabase", "dbtable", "dbcolumntitle", "dbcolumnid",
		"xmlupdatefile", "xmlfile", "latestfile", "muurl", "bturl",
		"ircnick", "irclogin", "ircrealname", "irchost", "ircport",
		"ircssl", "#ircnickserv", "#ircpass", "ircchannels", "quitmsg" };
	
	private Map<String, String> properties;
	
	public ConfigParser() {
		
		properties = new HashMap<String, String>();
		
	}
	
	public boolean parse() throws IOException {
		
		return parse(new File("config.txt"));
		
	}
	
	public boolean parse(File file) throws IOException {
		
		LOG.info("Loading configuration...");
		
		createDefault(file);
		properties = load(file);
		return check(properties);
		
	}
	
	public String getProperty(final String key) {
		
		return properties.get(key);
		
	}
	
	private void createDefault(File file) throws IOException {
		
		if (!file.createNewFile())
			return;
		
		final BufferedReader def =
			new BufferedReader(new InputStreamReader(this.getClass()
				.getClassLoader().getResourceAsStream("config.txt")));
		final BufferedWriter out = new BufferedWriter(new FileWriter(file));
		
		String line;
		while ((line = def.readLine()) != null)
			out.write(line + "\n");
		
		def.close();
		out.flush();
		out.close();
		
		LOG.info("Default configuration written, please edit the config.txt file.");
		
		System.exit(1);
		
	}
	
	private Map<String, String> load(File file) throws IOException {
		
		Map<String,String> properties = new HashMap<String, String>();
		
		final BufferedReader in = new BufferedReader(new FileReader(file));
		
		String line;
		while ((line = in.readLine()) != null) {
			
			if (line.startsWith("#") || !line.contains(":"))
				continue;
			
			final int i = line.indexOf(":");
			final String key = line.substring(0, i);
			final String val = line.substring(i + 1);
			
			properties.put(key, val);
			
		}
		
		in.close();
		
		return properties;
		
	}
	
	private boolean check(Map<String, String> properties) {
		
		boolean passed = true;
		for (String key : keys) {
			
			boolean required = true;
			if (key.startsWith("#")) {
				
				key = key.substring(1);
				required = false;
				
			}
			
			if (required && (!properties.containsKey(key) || properties.get(key).isEmpty())) {
				
				LOG.error("Required configuration value '" + key
					+ "' was not found!");
				
				passed = false;
				
			}
			
		}
		
		return passed;
		
	}
	
}
