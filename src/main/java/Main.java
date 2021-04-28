import System.Config.Config;
import System.Database.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides the main entry point for the application.
 */
public class Main {

    /**
     * Main entry point of this class.
     *
     * @param args The given arguments.
     */
    public static void main(String[] args) {
        Config config = Config.getInstance();
        System.out.println("Application entry point.");

        new Menu();

    }

}