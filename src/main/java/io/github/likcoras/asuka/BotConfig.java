package io.github.likcoras.asuka;

import io.github.likcoras.asuka.exception.ConfigException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import lombok.Cleanup;
import lombok.NonNull;

import com.google.common.base.Splitter;
import com.google.common.io.Resources;

public class BotConfig {

	private final Properties config;

	public BotConfig() {
		config = new Properties();
	}

	public boolean load(@NonNull Path path) throws IOException {
		if (Files.exists(path)) {
			loadFile(path);
			return true;
		} else {
			writeFile(path);
			return false;
		}
	}

	public String getString(@NonNull String key) throws ConfigException {
		String value = config.getProperty(key);
		if (value == null)
			throw new ConfigException(key);
		return value;
	}

	public int getInt(@NonNull String key) throws ConfigException {
		try {
			return Integer.parseInt(getString(key));
		} catch (NumberFormatException e) {
			throw new ConfigException(key, Integer.class);
		}
	}

	public boolean getBoolean(@NonNull String key) throws ConfigException {
		String value = getString(key);
		if (value.equalsIgnoreCase("true"))
			return true;
		else if (value.equalsIgnoreCase("false"))
			return false;
		else
			throw new ConfigException(key, Boolean.class);
	}

	public List<String> getStringList(@NonNull String key) throws ConfigException {
		return Splitter.on(',').omitEmptyStrings().trimResults().splitToList(getString(key));
	}

	public List<Integer> getIntList(@NonNull String key) throws ConfigException {
		try {
			return getStringList(key).stream().mapToInt(s -> Integer.parseInt(s)).boxed().collect(Collectors.toList());
		} catch (NumberFormatException e) {
			throw new ConfigException(key, List.class);
		}
	}

	private void loadFile(Path path) throws IOException {
		@Cleanup
		InputStream in = Files.newInputStream(path);
		config.load(in);
	}

	private void writeFile(Path path) throws IOException {
		@Cleanup
		InputStream in = Resources.getResource("config.txt").openStream();
		Files.copy(in, path);
	}

}
