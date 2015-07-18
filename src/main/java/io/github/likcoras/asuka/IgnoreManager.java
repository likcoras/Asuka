package io.github.likcoras.asuka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.pircbotx.User;
import lombok.Cleanup;

public class IgnoreManager {
	
	private Set<String> ignored;
	
	public IgnoreManager() {
		ignored = Collections.synchronizedSet(new HashSet<String>());
	}
	
	public void addIgnored(User user) {
		addIgnored(BotUtil.getId(user));
	}
	
	public void addIgnored(String user) {
		ignored.add(user);
	}
	
	public void removeIgnored(User user) {
		removeIgnored(BotUtil.getId(user));
	}
	
	public void removeIgnored(String user) {
		ignored.remove(user);
	}
	
	public boolean isIgnored(User user) {
		return ignored.contains(BotUtil.getId(user));
	}
	
	public void readFile(Path ignoreFile) throws IOException {
		@Cleanup
		BufferedReader read = Files.newBufferedReader(ignoreFile);
		synchronized (ignored) {
			String line;
			while ((line = read.readLine()) != null)
				ignored.add(line);
		}
	}
	
	public void writeFile(Path ignoreFile) throws IOException {
		@Cleanup
		BufferedWriter write = Files.newBufferedWriter(ignoreFile);
		synchronized (ignored) {
			for (String user : ignored)
				write.write(user + "\n");
		}
		write.flush();
	}
	
}
