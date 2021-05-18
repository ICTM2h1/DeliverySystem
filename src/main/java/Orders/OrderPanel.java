package Orders;

import Authenthication.User;
import Crud.Order;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a panel for richard items.
 */
public class OrderPanel extends JPanelListBase {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Orders";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<LinkedHashMap<String, String>> getListItems() {
        Order order = new Order();

        return order.all();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListItemLabel(LinkedHashMap<String, String> listItem) {
        return listItem.get("OrderID");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateListItemPreview(LinkedHashMap<String, String> listItem) {
        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        this.preview.addComponent(new JLabel("Klant:"), true);
        this.preview.addComponent(new JLabel(listItem.get("CustomerID")));

        this.preview.addComponent(new JLabel("Naam:"), true);
        this.preview.addComponent(new JLabel(listItem.get("CustomerName")));

        this.preview.addComponent(new JLabel("Straat:"), true);
        this.preview.addComponent(new JLabel(listItem.get("DeliveryAddressLine1")));

        this.preview.addComponent(new JLabel("Postcode:"), true);
        this.preview.addComponent(new JLabel(listItem.get("DeliveryPostalCode")));

        this.preview.gridBagConstraints.insets = new Insets(5, 25, 275, 25);
        this.preview.addComponent(new JLabel("Plaats:"), true);
        this.preview.addComponent(new JLabel(listItem.get("CityName")));




    }

}