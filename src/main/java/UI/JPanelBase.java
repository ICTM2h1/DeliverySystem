package UI;

import Authenthication.User;
import DeliveryRoute.DeliveryRoutePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a base for panels.
 */
abstract public class JPanelBase extends JPanel {

    private final int extraWidth, extraHeight;
    protected static ArrayList<JPanelBase> panels = new ArrayList<>();

    protected final User user;

    /**
     * Creates a new panel.
     */
    protected JPanelBase(User user) {
        this(400, 400, user);
    }

    /**
     * Creates a new panel.
     *
     * @param extraWidth The extra width of the panel.
     * @param extraHeight The extra height of the panel.
     */
    protected JPanelBase(int extraWidth, int extraHeight, User user) {
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
     * Instantiates the panel.
     *
     * The developer may add components to the UI within this method.
     */
    public abstract void instantiate();

    /**
     * Registers manually the panels.
     */
    public static void registerPanels(User user) {
        ArrayList<JPanelBase> panelList = new ArrayList<>();
        panelList.add(new DeliveryRoutePanel(user));

        if (user.getRole().isAdmin()) {
            panelList.add(new TestPanel(user));
        }

        for (JPanelBase panel : panelList) {
            panel.instantiate();
            panel.updateUI();
            panels.add(panel);
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
