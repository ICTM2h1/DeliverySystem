package UI.Dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a base for panels.
 */
abstract public class JDialogBase extends JDialog {

    protected final int panelWidth, panelHeight;

    public GridBagConstraints gridBagConstraints;

    /**
     * Creates a new panel.
     */
    public JDialogBase(JFrame frame, boolean modal) {
        this(frame, modal, 700, 500);
    }

    /**
     * Creates a new panel.
     *
     * @param panelWidth The extra width of the panel.
     * @param panelHeight The extra height of the panel.
     */
    public JDialogBase(JFrame frame, boolean modal, int panelWidth, int panelHeight) {
        super(frame, modal);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        this.setDefaultGridBagConstraints();
        this.setLayout(this.getLayoutManager());
        this.setSize(this.panelWidth, this.panelHeight);
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
}
