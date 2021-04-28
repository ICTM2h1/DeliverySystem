package UI;

import System.Config.Config;

import java.awt.*;
import javax.swing.*;

/**
 * Provides a class with main window of the application.
 */
public class Window extends JFrame {

    /**
     * Creates new window object.
     */
    public Window() {
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

        JPanelBase.registerPanels();
        for (JPanelBase panel : JPanelBase.getPanels()) {
            tabbedPane.addTab(panel.getTitle(), panel);
        }

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
}
