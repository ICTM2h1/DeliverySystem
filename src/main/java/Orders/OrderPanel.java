package Orders;

import Crud.Order;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Provides a panel for richard items.
 */
public class OrderPanel extends JPanelListBase implements ActionListener {

    private JButton editButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabTitle() {
        return "Bestellingen";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        ArrayList<LinkedHashMap<String, String>> items = this.getListItems();
        if (items.size() == 1) {
            return "1 bestelling";
        }

        return String.format("%s bestellingen", items.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListPreviewTitle() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<LinkedHashMap<String, String>> getListItems() {
        Order order = new Order();

        return order.allLimited();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListItemLabel(LinkedHashMap<String, String> listItem) {
        return String.format("%s   -   %s", listItem.get("OrderID"), listItem.get("ExpectedDeliveryDate"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasVerticalScrollbar() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateListItemPreview(LinkedHashMap<String, String> listItem) {
        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        this.preview.addComponent(new JLabel("Bestelling:"), true);
        this.preview.addComponent(new JLabel(listItem.get("OrderID")));

        this.preview.addComponent(new JLabel("Adres:"), true);
        this.preview.addComponent(new JLabel(String.format(
                "%s, %s, %s", listItem.get("DeliveryAddressLine1"), listItem.get("DeliveryPostalCode"), listItem.get("CityName")
        )));

        this.preview.addComponent(new JLabel("Geplaatst op:"), true);
        this.preview.addComponent(new JLabel(listItem.get("OrderDate")));

        this.preview.addComponent(new JLabel("Afleverdatum:"), true);
        this.preview.addComponent(new JLabel(listItem.get("ExpectedDeliveryDate")));

        String deliveryDate = listItem.get("ExpectedDeliveryDate");
        String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        if (currentDate.equals(deliveryDate)) {
            return;
        }

        this.preview.gridBagConstraints.insets = new Insets(25, 25, 275, 25);
        this.editButton = new JButton("Verwijderen");
        this.editButton.addActionListener(this);
        this.preview.addFullWidthComponent(this.editButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.editButton) {
            LinkedHashMap<String, String> orderEntity = this.getListItems().get(this.list.getSelectedIndex());
            OrderDeleteDialog orderDeleteDialog = new OrderDeleteDialog(new JFrame(), true, orderEntity);
            if (!orderDeleteDialog.deleteOrder()) {
                return;
            }

            Order order = new Order();
            order.addCondition("OrderID", orderEntity.get("OrderID"));
            order.delete();

            this.removeAll();
            this.addTitleComponent();
            this.instantiate();
            this.updateUI();
        }
    }

}