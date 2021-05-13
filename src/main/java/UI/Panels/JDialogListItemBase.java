package UI.Panels;

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

    protected JButton okButton, cancelButton, clickedButton;

    /**
     * Creates a new dialog for list items.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     */
    public JDialogListItemBase(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal);
        this.listItem = listItem;

        this.setLayout(new GridLayout(3, 2));
        this.setTitle(this.getDialogTitle());
        this.setSize(400, 200);
        this.instantiate(listItem);

        this.cancelButton = new JButton("Annuleren");
        this.cancelButton.addActionListener(this);
        this.add(this.cancelButton);

        this.okButton = new JButton("Opslaan");
        this.okButton.addActionListener(this);
        this.add(this.okButton);

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
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.clickedButton = (JButton) e.getSource();
        if (this.clickedButton == this.getOkButton()) {
            this.executionAction();
        }

        this.dispose();
        this.repaint();
    }

    /**
     * Executes the action.
     */
    protected abstract void executionAction();

    /**
     * Gets the ok button.
     *
     * @return The ok button.
     */
    public JButton getOkButton() {
        return this.okButton;
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
