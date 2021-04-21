package System.Error;

import System.Config.Config;

import java.util.Arrays;

/**
 * Provides a class for handling errors.
 */
public class SystemError {

    private static final Config config = Config.getInstance();

    /**
     * Handles an exception, either shows the message or stops running the program.
     *
     * @param exception The exception.
     */
    public static void handle(Exception exception) {
        if (Boolean.parseBoolean(config.get("debug"))) {
            System.out.println(exception.getMessage());
            System.out.println(Arrays.toString(exception.getStackTrace()));
            System.exit(-1);
        }

        System.out.println("Something went wrong while running this code, please try again or contact the adminstrators.");
        System.exit(-1);
    }

    /**
     * Handles an exception, either shows the message or stops running the program.
     *
     * @param exception The exception.
     */
    public static void handle(Exception exception, String message) {
        if (Boolean.parseBoolean(config.get("debug"))) {
            System.out.println(message);
            System.out.println(exception.getMessage());
            System.out.println(Arrays.toString(exception.getStackTrace()));
            System.exit(-1);
        }

        System.out.println("Something went wrong while running this code, please try again or contact the adminstrators.");
        System.exit(-1);
    }

}
