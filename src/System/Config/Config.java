package System.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

    private static Config config;
    private Map<String, String> items = new HashMap<>();

    private Config() {
        try {
            File file = new File(".env");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String key = scanner.next();
                scanner.next();
                String value = scanner.next();
                this.items.put(key, value);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Configuratiebestand kon niet worden gevonden: %s%n", e.getMessage());
        }
    }

    public static Config getInstance() {
        if (config != null) {
            return config;
        }

        config = new Config();
        return config;
    }

    public String get(String key) {
        return this.items.get(key);
    }

    public Map<String, String> get() {
        return this.items;
    }
}
