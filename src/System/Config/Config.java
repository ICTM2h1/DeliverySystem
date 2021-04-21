package System.Config;

import System.Error.SystemError;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Provides a class for getting values from the config.
 */
public class Config {

    private static Config config;
    private final HashMap<String, String> items = new HashMap<>();

    /**
     * Creates a new config object.
     *
     * Loads all .env values into the items map.
     */
    private Config() {
        try {
            File file = new File(".env");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String key = scanner.next();
                if (scanner.hasNext()) {
                    scanner.next();
                }

                String value = "";
                if (scanner.hasNext()) {
                    value = scanner.next();
                }

                this.items.put(key, value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file could not be found.");
            SystemError.handle(e, "Configuration file could not be found.");
        }
    }

    /**
     * Creates one instance of this class.
     *
     * @return Either a new config instance or an existing one.
     */
    public static Config getInstance() {
        if (config != null) {
            return config;
        }

        config = new Config();
        return config;
    }

    /**
     * Gets a key from the config.
     *
     * @param key The key.
     *
     * @return The value of the key.
     */
    public String get(String key) {
        String value = this.items.get(key);

        return value.equals("null") ? "" : value;
    }

    /**
     * Gets the config items map.
     *
     * @return The config items.
     */
    public HashMap<String, String> get() {
        return this.items;
    }
}
