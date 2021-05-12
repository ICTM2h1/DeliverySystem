package Orders;

import Authenthication.User;
import Crud.Order;
import UI.Panels.JPanelListBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a panel for richard items.
 */
public class OrderPanel extends JPanelListBase {

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public OrderPanel(User user) {
        super(user);
    }

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
        this.preview.add(new JLabel(listItem.get("OrderID")));

    }

}