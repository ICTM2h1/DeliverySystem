package UI.Panel;

import Authenthication.User;
import DeliveryRoute.DeliveryRoutePanel;
import UI.TestPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a base for panels.
 */
public abstract class JPanelBase extends JPanel {

    private int extraWidth, extraHeight;
    protected static ArrayList<JPanelBase> panels = new ArrayList<>();

    protected final User user;

    /**
     * Creates a new panel.
     */
    public JPanelBase(User user) {
        this(400, 400, user);
    }

    /**
     * Creates a new panel.
     *
     * @param extraWidth The extra width of the panel.
     * @param extraHeight The extra height of the panel.
     */
    public JPanelBase(int extraWidth, int extraHeight, User user) {
        this.extraWidth = extraWidth;
        this.extraHeight = extraHeight;
        this.user = user;
    }

    /**
     * Gets the title of the panel.
     *
     * @return The title.
     */
    public abstract String getTitle();

    /**
     * Instantiates the GUI of the panel.
     *
     * The developer may add components to the panel within this method.
     */
    public abstract void instantiate();

    /**
     * Registers manually the panels.
     */
    public static void registerPanels(User user) {
        ArrayList<JPanelBase> delivererPanels = new ArrayList<>();
        delivererPanels.add(new DeliveryRoutePanel(user));

        for (JPanelBase panel : delivererPanels) {
            panel.instantiate();
            panel.updateUI();
            panels.add(panel);
        }

        if (user.getRole().isAdmin()) {
            ArrayList<JPanelBase> adminPanels = new ArrayList<>();
            adminPanels.add(new TestPanel(user));

            for (JPanelBase panel : adminPanels) {
                panel.instantiate();
                panel.updateUI();
                panels.add(panel);
            }
        }
    }

    /**
     * Gets the registered panels.
     *
     * @return The panels.
     */
    public static ArrayList<JPanelBase> getPanels() {
        if (panels.isEmpty()) {
            throw new RuntimeException("Panels cannot be empty in order to run this application.");
        }

        return panels;
    }

    /**
     * {@inheritDoc}
     */
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        size.width += this.extraWidth;
        size.height += this.extraHeight;

        return size;
    }

}
