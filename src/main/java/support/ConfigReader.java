package support;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    Properties props = new Properties();

    public ConfigReader() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties");
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
