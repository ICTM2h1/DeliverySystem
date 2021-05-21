package UI.Panels;

import Authenthication.LogoutPanel;
import Authenthication.User;
import Customer.CustomerPanel;
import DeliveryRoute.DeliveryRoutePanel;
import Orders.OrderPanel;
import Stock.StockPanel;

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
    protected static final ArrayList<JPanelBase> menuPanels = new ArrayList<>();

    /**
     * Creates a new panel.
     */
    public JPanelBase() {
        this(700, 500);
    }

    /**
     * Creates a new panel.
     *
     * @param panelWidth The extra width of the panel.
     * @param panelHeight The extra height of the panel.
     */
    public JPanelBase(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

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
        Font labelFont = this.titleLabel.getFont();
        this.titleLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 20));
        this.addFullWidthComponent(this.titleLabel);
        this.gridBagConstraints.insets = defaultInsets;
    }

    /**
     * Adds a full width component.
     *
     * @param component The component.
     */
    public void addFullWidthComponent(Component component) {
        this.addFullWidthComponent(component, 2);
    }

    /**
     * Adds a full width component.
     *
     * @param component The component.
     */
    public void addFullWidthComponent(Component component, int gridWidth) {
        int defaultGridWidth = this.gridBagConstraints.gridwidth;

        this.gridBagConstraints.gridwidth = gridWidth;
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
     * Registers manually the menu panels.
     */
    public static void registerMenuPanels(User user) {
        menuPanels.clear();
        ArrayList<JPanelBase> panelList = new ArrayList<>();
        panelList.add(new DeliveryRoutePanel());

        if (user.getRole().isAdmin()) {
            panelList.add(new CustomerPanel());
            panelList.add(new StockPanel());
            panelList.add(new OrderPanel());
        }

        panelList.add(new LogoutPanel());

        for (JPanelBase panel : panelList) {
            panel.addTitleComponent();
            panel.instantiate();
            panel.updateUI();
            menuPanels.add(panel);
        }
    }

    /**
     * Gets the registered menu panels.
     *
     * @return The panels.
     */
    public static ArrayList<JPanelBase> getMenuPanels() {
        if (menuPanels.isEmpty()) {
            throw new RuntimeException("Panels cannot be empty in order to run this application.");
        }

        return menuPanels;
    }

    /**
     * Gets the default border of the panels.
     *
     * @return The default border.
     */
    public static Border getDefaultBorder() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10);
    }

}
