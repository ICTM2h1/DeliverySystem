package Stock;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing stock items.
 */
public class StockItemEditDialog extends JDialogListItemBase {

    private int quantityChange;

    private JButton decreaseButton, increaseButton;
    private JTextField quantityField;

    /**
     * Creates a new stock edit dialog.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     */
    public StockItemEditDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem, 4, 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDialogTitle() {
        return "Bewerk productvoorraad";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void instantiate(LinkedHashMap<String, String> listItem) {
        this.add(new JLabel("Product"));
        this.add(new JLabel(listItem.get("StockItemName")));

        this.add(new JLabel("Voorraad aanpassing"));
        this.quantityField = new JTextField(5);
        this.add(this.quantityField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addActionButtons() {
        this.decreaseButton = new JButton("Verlagen");
        this.decreaseButton.addActionListener(this);
        this.add(this.decreaseButton);

        this.increaseButton = new JButton("Verhogen");
        this.increaseButton.addActionListener(this);
        this.add(this.increaseButton);

        this.cancelButton = new JButton("Annuleren");
        this.cancelButton.addActionListener(this);
        this.add(this.cancelButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.clickedButton = (JButton) e.getSource();
        if (this.clickedButton.equals(this.increaseButton) || this.clickedButton.equals(this.decreaseButton)) {
            this.executeAction();
        }

        this.dispose();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAction() {
        String quantityChangeString = "-" + this.quantityField.getText();
        if (this.getClickedButton().equals(this.increaseButton)) {
            quantityChangeString = this.quantityField.getText();
        }

        try {
            this.quantityChange = Integer.parseInt(quantityChangeString);
        } catch (NumberFormatException exception) {
            this.quantityChange = 0;
        }
    }

    /**
     * Gets the quantity change.
     *
     * @return The quantity change.
     */
    public int getQuantityChange() {
        return this.quantityChange;
    }

}
