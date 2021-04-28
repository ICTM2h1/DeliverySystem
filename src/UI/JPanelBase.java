package UI;

import DeliveryRoute.DeliveryRoutePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a base for panels.
 */
abstract public class JPanelBase extends JPanel {

    private int extraWidth, extraHeight;
    protected static ArrayList<JPanelBase> panels = new ArrayList<>();

    /**
     * Creates a new panel.
     */
    protected JPanelBase() {
        this(400, 400);
    }

    /**
     * Creates a new panel.
     *
     * @param extraWidth The extra width of the panel.
     * @param extraHeight The extra height of the panel.
     */
    protected JPanelBase(int extraWidth, int extraHeight) {
        this.extraWidth = extraWidth;
        this.extraHeight = extraHeight;
    }

    /**
     * Gets the title of the panel.
     *
     * @return The title.
     */
    public abstract String getTitle();

    /**
     * Registers manually the panels.
     */
    public static void registerPanels() {
        panels.add(new DeliveryRoutePanel());
        panels.add(new TestPanel());
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
