package UI;

import Authenthication.AuthenticationDialog;
import Authenthication.User;
import Authenthication.UserRole;
import System.Config.Config;
import UI.Panel.JPanelBase;

import java.awt.*;
import javax.swing.*;

/**
 * Provides a class with main window of the application.
 */
public class Window extends JFrame {

    private final User user;

    /**
     * Creates new window object.
     */
    public Window() {
        Config config = Config.getInstance();

        // @todo remove this temporary debug code when the application is no longer in development.
        if (!Boolean.parseBoolean(config.get("debug"))) {
            AuthenticationDialog authentication = new AuthenticationDialog(this, true);
            this.user = authentication.getUser();
            if (!authentication.isAuthenticated()) {
                System.out.println("Inloggen is afgebroken. U heeft geen toegang tot de applicatie.");
                System.exit(0);
            }
        } else {
            this.user = UserRole.ADMIN.createUser("test", "test", UserRole.ADMIN);
        }

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
            tabbedPane.addTab(panel.getTitle(), panel);
        }

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
}
