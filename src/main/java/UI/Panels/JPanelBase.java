package UI.Panels;

import Authenthication.User;
import DeliveryRoute.DeliveryRoutePanel;
import UI.TestPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a base for panels.
 */
abstract public class JPanelBase extends JPanel {

    protected final int panelWidth, panelHeight;

    protected GridBagConstraints gridBagConstraints;
    protected JLabel titleLabel;
    protected static ArrayList<JPanelBase> panels = new ArrayList<>();

    protected final User user;

    /**
     * Creates a new panel.
     */
    protected JPanelBase(User user) {
        this(700, 500, user);
    }

    /**
     * Creates a new panel.
     *
     * @param panelWidth The extra width of the panel.
     * @param panelHeight The extra height of the panel.
     */
    protected JPanelBase(int panelWidth, int panelHeight, User user) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.user = user;

        this.setDefaultGridBagConstraints();
    }

    /**
     * Gets the title of the panel.
     *
     * @return The title.
     */
    public abstract String getTabTitle();

    /**
     * Instantiates the panel.
     *
     * The developer may add components to the UI within this method.
     */
    public abstract void instantiate();

    /**
     * Gets the layout manager for the panel.
     *
     * @return The layout manager.
     */
    protected LayoutManager getLayoutManager() {
        return new GridBagLayout();
    }

    /**
     * Sets the default grid bag constraints.
     */
    protected void setDefaultGridBagConstraints() {
        this.gridBagConstraints = new GridBagConstraints();
        this.gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    }

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
            panel.setLayout(panel.getLayoutManager());
            panel.instantiate();
            panel.setBorder(getDefaultBorder());
            panel.setPreferredSize(panel.getPreferredSize());
            panel.setSize(panel.getPreferredSize());
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
     * Gets the default border of the panels.
     *
     * @return The default border.
     */
    public static Border getDefaultBorder() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10);
    }

    /**
     * {@inheritDoc}
     */
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        size.width = this.panelWidth;
        size.height = this.panelHeight;

        return size;
    }

}
