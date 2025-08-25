package api.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager is a utility class to read configuration from config.properties.
 */
public class ConfigManager {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file.");
        }
    }

    // Generic getter for any property
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Specific getters for common properties
    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static String getProjectName() {
        return getProperty("projectName");
    }

    public static String getTesterName() {
        return getProperty("testerName");
    }

    public static String getEnv() {
        return getProperty("env");
    }

    // Endpoint getters
    public static String getCreatePetEndpoint() {
        return getProperty("createPetEndpoint");
    }

    public static String getGetPetEndpoint() {
        return getProperty("getPetEndpoint");
    }

    public static String getUpdatePetEndpoint() {
        return getProperty("updatePetEndpoint");
    }

    public static String getDeletePetEndpoint() {
        return getProperty("deletePetEndpoint");
    }
}
