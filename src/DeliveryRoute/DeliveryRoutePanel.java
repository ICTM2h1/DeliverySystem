package DeliveryRoute;

import Crud.Orders;
import UI.JPanelBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoutePanel extends JPanelBase {

    private final ArrayList<HashMap<String, String>> orders;

    public static int routeCount = 0;
    public static int ordersCount = 0;
    private static double ordersUnsorted = 0;

    /**
     * Creates a new delivery route object.
     */
    public DeliveryRoutePanel() {
        this("2013-01-03");
    }

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoutePanel(String date) {
        super();

        Orders orders = new Orders(date);

        this.orders = orders.filterOnGeometry();
        routeCount++;
        ordersCount += this.orders.size();
        ordersUnsorted += (((double) orders.getWithoutGeometry()) / ((double) this.orders.size())) * 100;

        this.add(new JButton("Test"));
        this.add(new JButton("Test 1"));
    }

    @Override
    public String getTitle() {
        return "Bezorgingstrajecten";
    }

    public static double getOrdersUnsortedPercentage() {
        return ordersUnsorted / (routeCount * 100) * 100;
    }

}
