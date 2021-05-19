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

    public OrderPanel(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String getTabTitle() {
        return "Orders";
    }

    @Override
    public String getTitle() {
        if (this.getListItems().size() == 1) {
            return "1 Order";
        }

        return String.format("%s Orders", this.getListItems().size());
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
    protected void addTitleComponent() {
        if (this.getTitle() == null) {
            return;
        }

        JPanel titlePanel = new JPanel();
        titlePanel.setSize(this.getSize());
        titlePanel.setLayout(new BorderLayout());

        this.titleLabel = new JLabel(this.getTitle());
        Font labelFont = this.titleLabel.getFont();
        this.titleLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 20));
        titlePanel.add(this.titleLabel, BorderLayout.WEST);
    }

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

        this.preview.addComponent(new JLabel("Plaats:"), true);
        this.preview.addComponent(new JLabel(listItem.get("CityName")));




    }

}