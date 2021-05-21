package Customer;

import UI.Dialogs.JDialogListItemBase;

import javax.swing.*;
import java.util.LinkedHashMap;

/**
 * Provides a dialog for editing customers.
 */
public class CustomerEditDialog extends JDialogListItemBase {

    private String street, postalCode, city;

    private JTextField streetField, postalCodeField, cityField;

    /**
     * Creates a new stock edit dialog.
     *
     * @param frame The frame.
     * @param modal Is it a modal?
     * @param listItem The list item.
     */
    public CustomerEditDialog(JFrame frame, boolean modal, LinkedHashMap<String, String> listItem) {
        super(frame, modal, listItem, 5, 2);
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
        this.add(new JLabel("Naam: "));
        this.add(new JLabel(listItem.get("CustomerName")));

        this.add(new JLabel("Straat: "));
        this.streetField = new JTextField(5);
        this.streetField.setText(listItem.get("DeliveryAddressLine1"));
        this.add(this.streetField);

        this.add(new JLabel("Postcode: "));
        this.postalCodeField = new JTextField(5);
        this.postalCodeField.setText(listItem.get("DeliveryPostalCode"));
        this.add(this.postalCodeField);

        this.add(new JLabel("Straat: "));
        this.cityField = new JTextField(5);
        this.cityField.setText(listItem.get("CityName"));
        this.add(this.cityField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean validateAction() {
        String regex = "[1-9][0-9]{3}[a-zA-Z]{2}";

        boolean valid = this.postalCodeField.getText().matches(regex);
        if (!valid) {
            JOptionPane.showMessageDialog(this, "Ongeldige postcode opgegeven.");
        }

        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAction() {
        this.street = this.streetField.getText();
        this.postalCode = this.postalCodeField.getText();
        this.city = this.cityField.getText();
    }

    /**
     * Gets the street.
     *
     * @return The street.
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Gets the postal code.
     *
     * @return The postal code.
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Gets the city.
     *
     * @return The city.
     */
    public String getCity() {
        return this.city;
    }

}
