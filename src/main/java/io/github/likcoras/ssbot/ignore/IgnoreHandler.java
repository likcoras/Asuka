package io.github.likcoras.ssbot.ignore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IgnoreHandler {
	
	private final Set<String> ignored;
	private final File file;
	
	public IgnoreHandler() {
		
		ignored = new HashSet<String>();
		file = new File("ignore.txt");
		
	}
	
	public void loadIgnores() throws IOException {
		
		file.createNewFile();
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		synchronized (ignored) {
			
			ignored.clear();
			
			String line;
			while ((line = in.readLine()) != null)
				ignored.add(line.toLowerCase());
			
		}
		
		in.close();
		
	}
	
	public Set<String> listIgnores() {
		
		return Collections.unmodifiableSet(ignored);
		
	}
	
	public boolean isIgnored(String user) {
		
		return ignored.contains(user.toLowerCase());
		
	}
	
	public void addIgnore(String user) throws IOException {
		
		BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
		out.write(user + "\n");
		out.flush();
		out.close();
		
		loadIgnores();
		
	}
	
	public void delIgnore(String user) throws IOException {
		
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		
		for (String ignore : ignored)
			if (!ignore.equalsIgnoreCase(user))
				out.write(ignore + "\n");
		
		out.flush();
		out.close();
		
		loadIgnores();
		
	}
	
}
