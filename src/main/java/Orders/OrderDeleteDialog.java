package Orders;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing stock items.
 */
public class OrderDeleteDialog extends JDialogListItemBase {

    private boolean deleteOrder;

    /**
     * Creates a new delete dialog for orders.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The order list item.
     */
    public OrderDeleteDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDialogTitle() {
        return "Verwijderen bestelling";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void instantiate(LinkedHashMap<String, String> listItem) {
        this.add(new JLabel("Bestelling"));
        this.add(new JLabel(listItem.get("OrderID")));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void addActionButtons() {
        super.addActionButtons();

        this.proceedButton.setText("Verwijderen");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAction() {
        this.deleteOrder = true;
    }

    /**
     * Determines if the order must be deleted.
     *
     * @return Whether the order is going to be deleted or not?
     */
    public boolean deleteOrder() {
        return deleteOrder;
    }
}
