import System.Config.Config;
import System.Error.SystemError;

import javax.swing.*;

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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Menu();
            }
        });
    }

}
