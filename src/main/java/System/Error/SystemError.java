package System.Error;

import System.Config.Config;

import java.util.Arrays;

/**
 * Provides a class for handling errors.
 */
public class SystemError {

    private static final Config config = Config.getInstance();

    /**
     * Handles an exception, either shows the error or a user friendly error message and stops running the program.
     *
     * @param exception The exception.
     */
    public static void handle(Exception exception) {
        handle(exception, null);
    }

    /**
     * Handles an exception, either shows the error or a user friendly error message and stops running the program.
     *
     * @param exception The exception.
     * @param message The message.
     */
    public static void handle(Exception exception, String message) {
        if (Boolean.parseBoolean(config.get("debug"))) {
            if (message != null && !message.isEmpty()) {
                System.out.println(message);
            }

            System.out.println(exception.getMessage());
            System.out.println(Arrays.toString(exception.getStackTrace()));
            System.exit(-1);
        }

        System.out.println("Something went wrong while running this application, please try again or contact the administrators.");
        System.exit(-1);
    }

}
