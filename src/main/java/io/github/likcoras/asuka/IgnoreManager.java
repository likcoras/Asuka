package io.github.likcoras.asuka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.pircbotx.User;

import com.google.common.collect.ImmutableSet;

public class IgnoreManager {

	private Set<String> ignored;

	public IgnoreManager() {
		ignored = Collections.synchronizedSet(new HashSet<>());
	}

	public void addIgnored(User user) {
		addIgnored(user.getNick());
	}

	public void addIgnored(String user) {
		ignored.add(user);
	}

	public void removeIgnored(User user) {
		removeIgnored(user.getNick());
	}

	public void removeIgnored(String user) {
		ignored.remove(user);
	}

	public boolean isIgnored(User user) {
		return user != null && ignored.contains(user.getNick());
	}

	public Set<String> getIgnored() {
		return ImmutableSet.copyOf(ignored);
	}

	public void readFile(Path ignoreFile) throws IOException {
		if (Files.notExists(ignoreFile))
			Files.createFile(ignoreFile);
		synchronized (ignored) {
			Files.lines(ignoreFile).forEach(ignored::add);
		}
	}

	public void writeFile(Path ignoreFile) throws IOException {
		synchronized (ignored) {
			Files.write(ignoreFile, ignored);
		}
	}

}
