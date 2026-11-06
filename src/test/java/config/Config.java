package config;



import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input =
                     Config.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "config.properties not found");
            }

            PROPERTIES.load(input);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to load configuration", e);
        }
    }

    private Config() {}

    public static String getBaseUrl() {
        return PROPERTIES.getProperty("base.url");
    }

    public static String getUsername() {
        return PROPERTIES.getProperty("username");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("password");
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(
                PROPERTIES.getProperty("connection.timeout"));
    }

    public static int getSocketTimeout() {
        return Integer.parseInt(
                PROPERTIES.getProperty("socket.timeout"));
    }

    public static int getHealthMaxAttempts() {
        return Integer.parseInt(
                PROPERTIES.getProperty("health.max.attempts"));
    }

    public static int getHealthRetryDelay() {
        return Integer.parseInt(
                PROPERTIES.getProperty("health.retry.delay"));
    }
}
