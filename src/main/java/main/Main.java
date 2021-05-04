package main;

import UI.Window;

/**
 * This class provides the main entry point for the application.
 */
public class Main {

    /**
     * Holds the current reference to the window.
     */
    private static Window window;

    /**
     * The arguments of the main program.
     */
    private static String[] arguments;

    /**
     * Main entry point of this class.
     *
     * @param args The given arguments.
     */
    public static void main(String[] args) {
        arguments = args;
        window = new Window();
    }

    /**
     * Restarts the main program by disposing the current window and recalling the main method.
     */
    public static void restart() {
        window.dispose();
        main(arguments);
    }

}
