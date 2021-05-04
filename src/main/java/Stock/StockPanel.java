package Stock;

import Authenthication.User;
import Crud.StockItem;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a panel for stock items.
 */
public class StockPanel extends JPanelListBase implements ActionListener {

    private JButton addButton, editButton, deleteButton;

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public StockPanel(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabTitle() {
        return "Voorraad";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        ArrayList<LinkedHashMap<String, String>> items = this.getListItems();
        if (items.size() == 1) {
            return "1 product";
        }

        String outOfStock = items.get(0).get("OutOfStock");
        return String.format("%s producten - %s niet voorradig", items.size(), outOfStock);
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
    protected boolean hasVerticalScrollbar() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ListCellRenderer<Object> getListCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                try {
                    LinkedHashMap<String, String> item = (LinkedHashMap<String, String>) listItemsCopy.get(index);
                    int quantityOnHand = Integer.parseInt(item.get("QuantityOnHand"));

                    this.setForeground(Color.GREEN);
                    if (quantityOnHand >= StockItem.STOCK_DANGER && quantityOnHand <= StockItem.STOCK_WARNING) {
                        this.setForeground(Color.ORANGE);
                    } else if (quantityOnHand < StockItem.STOCK_DANGER) {
                        this.setForeground(Color.RED);
                    }

                } catch (Exception exception) {
                    this.setForeground(Color.RED);
                }


                return this;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<LinkedHashMap<String, String>> getListItems() {
        StockItem stockItem = new StockItem();

        return stockItem.all();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListItemLabel(LinkedHashMap<String, String> listItem) {
        return listItem.get("StockItemName");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateListItemPreview(LinkedHashMap<String, String> listItem) {
        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        this.preview.addComponent(new JLabel("Product:"), true);
        this.preview.addComponent(new JLabel(listItem.get("StockItemName")));

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 50, 0);
        this.preview.addComponent(new JLabel("Voorraad:"), true);
        this.preview.addComponent(new JLabel(listItem.get("QuantityOnHand")));

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

    }

}