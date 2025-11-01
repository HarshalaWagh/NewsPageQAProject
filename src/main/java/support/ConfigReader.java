package support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	Properties props = new Properties();

	public ConfigReader() {
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("config/config.properties");
			
			if (inputStream == null) {
				throw new IOException("Config file not found: config/config.properties");
			}
			props.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			System.err.println("Failed to load config.properties: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return props.getProperty(key);
	}
}