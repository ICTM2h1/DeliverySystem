package UI;

import Authenthication.AuthenticationDialog;
import Authenthication.User;
import System.Config.Config;
import UI.Panels.JPanelBase;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a class with main window of the application.
 */
public class Window extends JFrame {

    private final User user;

    /**
     * Creates new window object.
     */
    public Window() {
                AuthenticationDialog authentication = new AuthenticationDialog(this, true);
        this.user = authentication.getUser();
        if (!authentication.isAuthenticated()) {
            System.out.println("Inloggen is afgebroken. U heeft geen toegang tot de applicatie.");
            System.exit(0);
        }

        Config config = Config.getInstance();
        this.setTitle(String.format("%s", config.get("app_title")).replace("+", " "));

        this.setSize(700, 700);
        this.addComponentToPane(this.getContentPane());

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Builds the window content.
     *
     * @param pane Frame container.
     */
    private void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanelBase.registerPanels(this.user);
        for (JPanelBase panel : JPanelBase.getPanels()) {
            tabbedPane.addTab(panel.getTabTitle(), panel);
        }

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
}
