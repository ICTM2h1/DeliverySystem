package DeliveryRoute;

import java.util.LinkedHashMap;

/**
 * Represents a delivery point within a delivery route.
 */
public class DeliveryPoint {

    private final LinkedHashMap<String, String> order;

    /**
     * Creates a new delivery point.
     *
     * @param order The order.
     */
    public DeliveryPoint(LinkedHashMap<String, String> order) {
        this.order = order;
    }

    /**
     * Gets the label of this delivery point.
     *
     * @return The label.
     */
    public String label() {
        return this.order.get("OrderID");
    }

    /**
     * Gets the order.
     *
     * @return The order.
     */
    public LinkedHashMap<String, String> getOrder() {
        return order;
    }
    
}
