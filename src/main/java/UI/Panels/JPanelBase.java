package UI.Panels;

import Authenthication.LogoutPanel;
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

    public GridBagConstraints gridBagConstraints;
    public JLabel titleLabel;
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
        this.setLayout(this.getLayoutManager());
        this.setBorder(getDefaultBorder());
    }

    /**
     * Gets the title of the panel.
     *
     * @return The title.
     */
    public String getTabTitle() {
        return this.getTitle();
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
     * Adds the title component to the panel.
     */
    protected void addTitleComponent() {
        if (this.getTitle() == null) {
            return;
        }

        Insets defaultInsets = this.gridBagConstraints.insets;
        this.gridBagConstraints.insets = new Insets(5, 0, 15, 0);
        this.titleLabel = new JLabel(this.getTitle(), JLabel.CENTER);
        this.addFullWidthComponent(this.titleLabel);
        this.gridBagConstraints.insets = defaultInsets;
    }

    /**
     * Adds a full width component.
     *
     * @param component The component.
     */
    public void addFullWidthComponent(Component component) {
        int defaultGridWidth = this.gridBagConstraints.gridwidth;

        this.gridBagConstraints.gridwidth = this.getDefaultFullGridWidth();
        this.addComponent(component, true);

        this.gridBagConstraints.gridwidth = defaultGridWidth;
    }

    /**
     * Adds a component.
     *
     * @param component The component.
     */
    public void addComponent(Component component) {
        this.addComponent(component, false);
    }

    /**
     * Adds a component.
     *
     * @param component The component.
     * @param newRow Determines if this must be on a new row.
     */
    public void addComponent(Component component, boolean newRow) {
        this.gridBagConstraints.gridx++;

        if (newRow) {
            this.gridBagConstraints.gridx = 0;
            this.gridBagConstraints.gridy++;
        }

        this.add(component, this.gridBagConstraints);
    }

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
        this.gridBagConstraints.fill = GridBagConstraints.BOTH;
    }

    /**
     * Registers manually the panels.
     */
    public static void registerPanels(User user) {
        panels.clear();
        ArrayList<JPanelBase> panelList = new ArrayList<>();
        panelList.add(new DeliveryRoutePanel(user));

        if (user.getRole().isAdmin()) {
            panelList.add(new TestPanel(user));
        }

        panelList.add(new LogoutPanel(user));

        for (JPanelBase panel : panelList) {
            panel.addTitleComponent();
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
     * Gets the default border of the panels.
     *
     * @return The default border.
     */
    public static Border getDefaultBorder() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10);
    }

    /**
     * Gets the default full grid width value.
     *
     * @return The default full gird width value.
     */
    protected int getDefaultFullGridWidth() {
        return 2;
    }

}
