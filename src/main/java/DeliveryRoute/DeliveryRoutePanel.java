package DeliveryRoute;

import Authenthication.User;
import Crud.Order;
import UI.Components.Table;
import UI.Panels.JPanelRawListBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
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
    public String getTabTitle() {
        return "Bezorgingstrajecten";
    }

    /**
     * Gets the title for this panel.
     *
     * @return The title.
     */
    @Override
    public String getTitle() {
        if (this.listItems.size() == 1) {
            return "1 bezorgingstraject voor vandaag";
        }

        return String.format("%s bezorgingstrajecten voor vandaag", this.listItems.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getNoResultsText() {
        return "Er zijn geen bezorgingstrajecten gevonden voor vandaag.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<Object> getRawListItems() {
        if (this.listItems != null && !this.listItems.isEmpty()) {
            return this.listItems;
        }

        Order order = new Order(this.date);

        ArrayList<LinkedHashMap<String, String>> entities = order.filterOnGeometry();
        ArrayList<DeliveryRoute> listItems = new ArrayList<>();

        int delivererCount = DeliveryRoute.deliverers;
        int ordersPerDeliverer = Math.round((float) entities.size() / delivererCount);
        if (ordersPerDeliverer == 0) {
            delivererCount = 1;
            ordersPerDeliverer = entities.size();
        }

        Iterator<LinkedHashMap<String, String>> iterator = entities.iterator();
        for (int deliverer = 0; deliverer < delivererCount; deliverer++) {
            int delivererOrderCount = 0;
            DeliveryRoute deliveryRoute = new DeliveryRoute(deliverer, ordersPerDeliverer);

            while (iterator.hasNext()) {
                LinkedHashMap<String, String> entity = iterator.next();

                if (delivererOrderCount >= ordersPerDeliverer) {
                    break;
                }

                deliveryRoute.add(new DeliveryPoint(entity));
                delivererOrderCount++;

                iterator.remove(); // avoids a ConcurrentModificationException
            }

            listItems.add(deliverer, deliveryRoute);
        }

        return new ArrayList<>(listItems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawListItemLabel(Object listItem) {
        DeliveryRoute deliveryRoute = (DeliveryRoute) listItem;

        return String.format("#%s", deliveryRoute.label());
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
    protected void updateRawListItemPreview(Object listItem) {
        DeliveryRoute deliveryRoute = (DeliveryRoute) listItem;

        this.preview.addComponent(new JLabel("Bezorgingstraject:"), true);
        this.preview.addComponent(new JLabel(String.format("%s km", deliveryRoute.getName())));

        this.preview.addComponent(new JLabel("Afstand:"), true);
        this.preview.addComponent(new JLabel(String.format("%s km", deliveryRoute.getDistance())));

        Table table = new Table();
        table.addColumn("Nr.");
        table.addColumn("Stad");
        table.addColumn("Afstand");

        int counter = 1;
        DeliveryPointBase previousDeliveryPoint = null;
        for (DeliveryPointBase deliveryPoint : deliveryRoute.getDeliveryPoints()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(counter));
            row.add(deliveryPoint.label());
            row.add(String.valueOf(deliveryPoint.distance(previousDeliveryPoint)));

            table.addRow(row);
            counter++;

            if (previousDeliveryPoint == null || previousDeliveryPoint.equals(deliveryPoint)) {
                previousDeliveryPoint = deliveryPoint;
            }
        }


        this.preview.addFullWidthComponent(table.render());
    }

}
