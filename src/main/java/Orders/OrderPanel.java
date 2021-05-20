package Orders;

import Authenthication.User;
import Crud.Order;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

        this.preview.gridBagConstraints.insets = new Insets(25, 25, 275, 25);
        this.editButton = new JButton("Verwijderen");
        this.editButton.addActionListener(this);
        this.preview.addFullWidthComponent(this.editButton);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.editButton) {

        }
    }


}