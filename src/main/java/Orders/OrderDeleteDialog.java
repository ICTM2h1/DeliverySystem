package Orders;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing stock items.
 */
public class OrderDeleteDialog extends JDialogListItemBase {

    public OrderDeleteDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDialogTitle() {
        return "Bewerk productvoorraad";
    }

    @Override
    protected void instantiate(LinkedHashMap<String, String> listItem) {

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




    }
}
