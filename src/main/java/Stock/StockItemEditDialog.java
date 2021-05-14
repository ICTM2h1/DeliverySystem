package Stock;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing stock items.
 */
public class StockItemEditDialog extends JDialogListItemBase {

    private int quantityChange;

    private JTextField quantityField;

    /**
     * Creates a new stock edit dialog.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     */
    public StockItemEditDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem);
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

        this.add(new JLabel("Voorraad verhoging"));
        this.quantityField = new JTextField(5);
        this.add(this.quantityField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAction() {
        try {
            this.quantityChange = Integer.parseInt(this.quantityField.getText());
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
