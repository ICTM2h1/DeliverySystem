package DeliveryRoute;

import Authenthication.User;
import Crud.Order;
import UI.Panels.JPanelRawListBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoutePanel extends JPanelRawListBase {

    private final String date;

    /**
     * Creates a new delivery route object.
     */
    public DeliveryRoutePanel(User user) {
        // @todo use the date of today instead of this date
        this("2013-01-03", user);
    }

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoutePanel(String date, User user) {
        super(user);

        this.date = date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Bezorgingstrajecten";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<LinkedHashMap> getRawListItems() {
        Order order = new Order(this.date);

        ArrayList<LinkedHashMap> orders = new ArrayList<>(order.filterOnGeometry());

        return orders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawListItemLabel(LinkedHashMap listItem) {
        LinkedHashMap<String, String> entity = (LinkedHashMap<String, String>) listItem;

        return String.format("Bestelling #%s", entity.get("OrderID"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateRawListItemPreview(LinkedHashMap listItem) {
        LinkedHashMap<String, String> entity = (LinkedHashMap<String, String>) listItem;

        this.preview.add(new JLabel(String.format("Bestelling #%s", entity.get("OrderID"))));
    }

}
