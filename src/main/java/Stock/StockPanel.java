package Stock;

import Crud.StockItem;
import Crud.StockItemHoldings;
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

    private int selectedProductIndex;

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
        if (outOfStock.equals("0")) {
            return String.format("%s producten", items.size());
        }

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
    protected String getNoResultsText() {
        return "Er zijn geen producten gevonden.";
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
            /**
             * {@inheritDoc}
             */
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                try {
                    LinkedHashMap<String, String> item = (LinkedHashMap<String, String>) listItemsCopy.get(index);
                    int quantityOnHand = Integer.parseInt(item.get("QuantityOnHand"));

                    Color background = Color.WHITE, foreground;
                    foreground = Color.GREEN;
                    if (quantityOnHand >= StockItem.STOCK_DANGER && quantityOnHand <= StockItem.STOCK_WARNING) {
                        foreground = Color.ORANGE;
                    } else if (quantityOnHand < StockItem.STOCK_DANGER) {
                        foreground = Color.RED;
                    }

                    if (!isSelected && !cellHasFocus) {
                        background = Color.GREEN;
                        foreground = Color.BLACK;
                        if (quantityOnHand >= StockItem.STOCK_DANGER && quantityOnHand <= StockItem.STOCK_WARNING) {
                            background = Color.ORANGE;
                            foreground = Color.BLACK;
                        } else if (quantityOnHand < StockItem.STOCK_DANGER) {
                            background = Color.RED;
                            foreground = Color.WHITE;
                        }
                    }

                    this.setBackground(background);
                    this.setForeground(foreground);
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
        this.selectedProductIndex = this.list.getSelectedIndex();
        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        this.preview.addComponent(new JLabel("Product:"), true);
        this.preview.addComponent(new JLabel(listItem.get("StockItemName")));

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 50, 0);
        this.preview.addComponent(new JLabel("Voorraad:"), true);
        this.preview.addComponent(new JLabel(listItem.get("QuantityOnHand")));

        this.preview.gridBagConstraints.insets = new Insets(5, 25, 300, 25);
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
            LinkedHashMap<String, String> product = this.getListItems().get(this.selectedProductIndex);
            StockItemEditDialog stockEditDialog = new StockItemEditDialog(new JFrame(), true, product);
            if (stockEditDialog.getClickedButton() != stockEditDialog.getOkButton()) {
                return;
            }

            int currentQuantityOnHand = Integer.parseInt(product.get("QuantityOnHand"));

            StockItem stockItem = new StockItem();
            StockItemHoldings stockItemHoldings = new StockItemHoldings();
            stockItemHoldings.addCondition("StockItemID", product.get("StockItemID"));
            stockItemHoldings.addValue("QuantityOnHand", String.valueOf(currentQuantityOnHand + stockEditDialog.getQuantityChange()));
            stockItemHoldings.update();

            // Get the updated product and write it back to the item on the selected index and update the preview.
            LinkedHashMap<String, String> updatedProduct = stockItem.get(Integer.parseInt(product.get("StockItemID")));
            this.listItems.set(this.selectedProductIndex, updatedProduct);
            this.titleLabel.setText(this.getTitle());
            this.updatePreview(updatedProduct);
        }
    }

}
