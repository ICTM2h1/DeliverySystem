package DeliveryRoute;

import Authenthication.User;
import Crud.Orders;
import UI.JPanelListBase;

import javax.swing.*;
import java.util.HashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoutePanel extends JPanelListBase {

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
        super((new Orders(date)).filterOnGeometry(), user);
    }

    @Override
    public String getTitle() {
        return "Bezorgingstrajecten";
    }

    @Override
    protected String getListItemLabel(HashMap<String, String> listItem) {
        return String.format("Bestelling #%s", listItem.get("OrderID"));
    }

    @Override
    protected void updateListItemPreview(HashMap<String, String> listItem) {
        this.preview.add(new JLabel(String.format("Bestelling #%s", listItem.get("OrderID"))));
    }

}
