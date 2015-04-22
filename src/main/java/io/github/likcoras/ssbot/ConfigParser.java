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

public class ConfigParser {
	
	private final String[] keys = { "dbhost", "dbport", "dbuser", "#dbpass",
			"dbdatabase", "dbtable", "dbcolumntitle", "dbcolumnid", "xmlfile",
			"muurl", "bturl", "ircnick", "irclogin", "ircrealname", "irchost",
			"ircport", "ircssl", "#ircnickserv", "#ircpass", "ircchannels" };
	
	private final Map<String, String> prop;
	private final File conf;
	
	public ConfigParser() {
		
		prop = new HashMap<String, String>();
		conf = new File("config.txt");
		
		createDefault();
		load();
		check();
		
		System.out.println("Configuration loaded with no errors.");
		
	}
	
	private void check() {
		
		for (String s : keys) {
			
			boolean b = false;
			if (s.startsWith("#")) {
				
				s = s.substring(1);
				b = true;
				
			}
			
			if (!prop.containsKey(s) || (!b && prop.get(s).isEmpty())) {
				
				System.out
						.println("Error: Missing configuration on " + s + "!");
				System.out
						.println("Remember, every single field must be filled, except passwords.");
				System.exit(1);
				
			}
			
		}
		
	}
	
	private void createDefault() {
		
		if (conf.exists()) {
			
			return;
			
		}
		
		System.out.println("Writing default configuration...");
		
		try {
			
			conf.createNewFile();
			
			final BufferedReader def =
					new BufferedReader(
							new InputStreamReader(this.getClass()
									.getClassLoader()
									.getResourceAsStream("config.txt")));
			final BufferedWriter out = new BufferedWriter(new FileWriter(conf));
			
			String line;
			while ((line = def.readLine()) != null) {
				
				out.write(line + "\n");
				
			}
			
			def.close();
			out.flush();
			out.close();
			
			System.out
					.println("Wrote default configuration! Please configure and re-run the application!");
			
		} catch (final IOException e) {
			
			System.out.println("Error: Exception while writing configuration!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			
		}
		
		System.exit(1);
		
	}
	
	public String getString(final String k) {
		
		return prop.get(k);
		
	}
	
	private void load() {
		
		System.out.println("Loading configuration...");
		
		try (BufferedReader in = new BufferedReader(new FileReader(conf))) {
			
			String line;
			while ((line = in.readLine()) != null) {
				
				if (line.startsWith("#") || !line.contains(":")) {
					
					continue;
					
				}
				
				final int i = line.indexOf(":");
				final String key = line.substring(0, i);
				final String val = line.substring(i + 1);
				
				prop.put(key, val);
				
			}
			
		} catch (final IOException e) {
			
			System.out.println("Error: Exception while reading configuration!");
			System.out
					.println("Please send the following error message to the author:");
			e.printStackTrace();
			System.exit(1);
			
		}
		
	}
	
}
