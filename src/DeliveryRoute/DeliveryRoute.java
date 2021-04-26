package DeliveryRoute;

import Crud.Orders;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoute {

    private final ArrayList<HashMap<String, String>> orders;

    public static int routeCount = 0;
    public static int ordersCount = 0;
    private static double ordersUnsorted = 0;

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoute(String date) {
        Orders orders = new Orders(date);

        this.orders = orders.filterOnGeometry();
        routeCount++;
        ordersCount += this.orders.size();
        ordersUnsorted += (((double) orders.getWithoutGeometry()) / ((double) this.orders.size())) * 100;
    }

    public static double getOrdersUnsortedPercentage() {
        return ordersUnsorted / (routeCount * 100) * 100;
    }

}
