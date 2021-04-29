package DeliveryRoute;

import Authenthication.User;
import Crud.Order;
import UI.Panel.JPanelListBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoutePanel extends JPanelListBase {

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
    protected ArrayList<LinkedHashMap<String, String>> getListItems() {
        return (new Order(this.date)).filterOnGeometry();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListItemLabel(LinkedHashMap<String, String> listItem) {
        return String.format("Bestelling #%s", listItem.get("OrderID"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateListItemPreview(LinkedHashMap<String, String> listItem) {
        this.preview.add(new JLabel(String.format("Bestelling #%s", listItem.get("OrderID"))));
    }

}
