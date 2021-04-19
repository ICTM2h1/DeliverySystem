import javax.swing.*;

/**
 * This class provides the main entry point for the application.
 */
public class Main {

    /**
     * Main entry point of this class.
     *
     * @param args
     *   The given arguments.
     */
    public static void main(String[] args)
    {
        // needed on mac os x
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // the proper way to show a jframe (invokeLater)
        SwingUtilities.invokeLater(new Menu());
    }

}
