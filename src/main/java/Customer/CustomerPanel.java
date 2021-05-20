package Customer;

import Crud.City;
import Crud.Customer;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a panel for customers.
 */
public class CustomerPanel extends JPanelListBase implements ActionListener {

    private JButton editButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabTitle() {
        return "Klanten";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        if (this.getListItems().size() == 1) {
            return "1 klant";
        }

        return String.format("%s klanten", this.getListItems().size());
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
    protected String getNoResultsText() {
        return "Er zijn geen klanten gevonden.";
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
    protected ArrayList<LinkedHashMap<String, String>> getListItems() {
        Customer customer = new Customer();

        return customer.all();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListItemLabel(LinkedHashMap<String, String> listItem) {
        return String.format("Klant #%s", listItem.get("CustomerID"));
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

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 50, 0);
        this.preview.addComponent(new JLabel("Plaats:"), true);
        this.preview.addComponent(new JLabel(listItem.get("CityName")));

        this.preview.gridBagConstraints.insets = new Insets(5, 25, 275, 25);
        this.editButton = new JButton("Bewerken");
        this.editButton.addActionListener(this);
        this.preview.addFullWidthComponent(this.editButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.editButton) {
            LinkedHashMap<String, String> customerEntity = this.getListItems().get(this.list.getSelectedIndex());
            CustomerEditDialog customerEditDialog = new CustomerEditDialog(new JFrame(), true, customerEntity);
            if (customerEditDialog.getClickedButton() == customerEditDialog.getCancelButton()) {
                return;
            }

            City city = new City();
            LinkedHashMap<String, String> cityEntity = city.getByName(customerEditDialog.getCity());
            if (cityEntity == null) {
                city.addValue("CityName", customerEditDialog.getCity());
                city.addValue("StateProvinceID", "39");
                city.addValue("LastEditedBy", "1");
                city.addValue("ValidFrom", "2013-01-01 00:00:00");
                city.addValue("ValidTo", "2013-01-01 00:00:00");
                city.insert();

                cityEntity = city.getByName(customerEditDialog.getCity());
            }

            Customer customer = new Customer();
            customer.addCondition("CustomerID", customerEntity.get("CustomerID"));
            customer.addValue("DeliveryAddressLine1", customerEditDialog.getStreet());
            customer.addValue("DeliveryPostalCode", customerEditDialog.getPostalCode());
            customer.addValue("DeliveryCityID", cityEntity.get("CityID"));
            customer.update();

            // Get the updated customer and write it back to the item on the selected index and update the preview.
            LinkedHashMap<String, String> updatedCustomer = customer.get(Integer.parseInt(customerEntity.get("CustomerID")));
            this.listItems.set(this.list.getSelectedIndex(), updatedCustomer);
            this.updatePreview(updatedCustomer);
        }
    }

}
