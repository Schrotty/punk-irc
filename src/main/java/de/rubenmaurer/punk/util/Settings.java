package de.rubenmaurer.punk.util;

import de.rubenmaurer.punk.Punk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper for accessing properties.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Settings {

    /**
     * The loaded properties
     */
    private Properties properties = new Properties();

    /**
     * The Singleton
     */
    private static Settings self = new Settings();

    /**
     * Get the port specified in properties.
     *
     * @return the port
     */
    public static int port() {
        return Integer.parseInt(getInstance().get("defaultPort"));
    }

    /**
     * Get the singleton.
     *
     * @return the singleton
     */
    public static Settings getInstance() {
        return self;
    }

    /**
     * Constructor for new settings singleton.
     */
    private Settings() {
        try (InputStream input = Punk.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get the value of a given property.
     *
     * @param key the property
     * @return the loaded value
     */
    public String get(String key) {
        return properties.getProperty(key);
    }
}
