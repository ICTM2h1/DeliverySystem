package Orders;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing stock items.
 */
public class OrderCancelDialog extends JDialogListItemBase {

    private boolean cancelOrder;

    /**
     * Creates a new delete dialog for orders.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The order list item.
     */
    public OrderCancelDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDialogTitle() {
        return "Annuleren bestelling";
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

        this.proceedButton.setText("Annuleer bestelling");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAction() {
        this.cancelOrder = true;
    }

    /**
     * Determines if the order must be canceled.
     *
     * @return Whether the order is going to be canceled or not?
     */
    public boolean cancelOrder() {
        return cancelOrder;
    }
}
