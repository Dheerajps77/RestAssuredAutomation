package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties;

    public ConfigLoader(String environment) {
        properties = new Properties();
        String configFileName = environment + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Could not find config file: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + configFileName, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}