package UI.Dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * Provides a base for dialogs.
 */
public abstract class JDialogListItemBase extends JDialog implements ActionListener {

    protected LinkedHashMap<String, String> listItem;

    protected JButton proceedButton, cancelButton, clickedButton;

    /**
     * Creates a new dialog for list items.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     */
    public JDialogListItemBase(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        this(frame, modal, listItem, 3, 2);
    }

    /**
     * Creates a new dialog for list items.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     * @param rows The number of rows.
     * @param cols The number of cols.
     */
    public JDialogListItemBase(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem, int rows, int cols) {
        super(frame, modal);
        this.listItem = listItem;

        this.setLayout(new GridLayout(rows, cols));
        this.setTitle(this.getDialogTitle());
        this.setSize(400, 200);
        this.instantiate(listItem);
        this.addActionButtons();

        this.setVisible(true);
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    protected abstract String getDialogTitle();

    /**
     * Instantiates the dialog.
     *
     * The developer may add components to the UI within this method.
     *
     * @param listItem The list item.
     */
    protected abstract void instantiate(LinkedHashMap<String, String> listItem);

    /**
     * Adds the action buttons.
     */
    protected void addActionButtons() {
        this.cancelButton = new JButton("Annuleren");
        this.cancelButton.addActionListener(this);
        this.add(this.cancelButton);

        this.proceedButton = new JButton("Opslaan");
        this.proceedButton.addActionListener(this);
        this.add(this.proceedButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.clickedButton = (JButton) e.getSource();
        if (this.clickedButton == this.getProceedButton()) {
            if (!this.validateAction()) {
                return;
            }

            this.executeAction();
        }

        this.dispose();
        this.repaint();
    }

    /**
     * Validates the action.
     *
     * @return Whether the validation was successful or not.
     */
    protected boolean validateAction() {
        return true;
    }

    /**
     * Executes the action.
     */
    protected abstract void executeAction();

    /**
     * Gets the ok button.
     *
     * @return The ok button.
     */
    public JButton getProceedButton() {
        return this.proceedButton;
    }

    /**
     * Gets the cancel button.
     *
     * @return The cancel button.
     */
    public JButton getCancelButton() {
        return this.cancelButton;
    }

    /**
     * Gets the clicked button.
     *
     * @return The clicked button.
     */
    public JButton getClickedButton() {
        return this.clickedButton;
    }

}
