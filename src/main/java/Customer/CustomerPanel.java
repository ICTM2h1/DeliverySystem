package Customer;

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

    private JButton addButton, editButton, deleteButton;

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

        this.addButton = new JButton("Toevoegen");
        this.addButton.addActionListener(this);

        titlePanel.add(this.addButton, BorderLayout.EAST);

        this.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.gridBagConstraints.weightx = 0.5;
        this.addFullWidthComponent(titlePanel);
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
        this.preview.addComponent(this.editButton, true);

        this.deleteButton = new JButton("Verwijderen");
        this.deleteButton.addActionListener(this);
        this.preview.addComponent(this.deleteButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
