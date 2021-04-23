package DeliveryRoute;

import Crud.Orders;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoute {

    private final ArrayList<HashMap<String, String>> orders;

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoute(String date) {
        Orders orders = new Orders(date);

        this.orders = orders.filterOnGeometry();
    }

}
