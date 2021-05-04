package DeliveryRoute;

import System.Error.SystemError;

import java.util.ArrayList;

/**
 * Provides a delivery route for deliverers.
 */
public class DeliveryRoute {

    public final static int deliverers = 6;

    private final int id;
    private int distance;

    private final ArrayList<DeliveryPointBase> deliveryPoints;

    /**
     * Creates a new delivery route.
     *
     * @param id The id of this route.
     * @param capacity The capacity of this route.
     */
    public DeliveryRoute(int id, int capacity) {
        this.id = id + 1;
        this.deliveryPoints = new ArrayList<>(capacity + 1);
    }

    /**
     * Gets the label of this delivery route.
     *
     * @return The label.
     */
    public String label() {
        return String.format("%s    %skm", id, this.getDistance());
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the delivery route.
     *
     * @return The name of the route.
     */
    public String getName() {
        String name = "Unknown";
        if (this.deliveryPoints.isEmpty()) {
            return name;
        }

        int orderCount = this.deliveryPoints.size();
        int middlePointKey = (int) Math.round((double) orderCount / 2);

        DeliveryPointBase firstPoint = this.deliveryPoints.get(0);
        if (firstPoint != null) {
            name = firstPoint.label() + " - ";
        }

        if (middlePointKey < orderCount) {
            DeliveryPointBase middlePoint = this.deliveryPoints.get(middlePointKey);
            if (middlePoint != null) {
                name += middlePoint.label() + " - ";
            }
        }

        DeliveryPointBase lastPoint = this.deliveryPoints.get(orderCount - 1);
        if (lastPoint != null) {
            name += lastPoint.label();
        }

        return name;
    }

    /**
     * Gets the distance of this delivery route.
     *
     * @return The distance.
     */
    public int getDistance() {
        if (this.distance != 0) {
            return this.distance;
        }

        double distance = 0;
        DeliveryPointBase previousDeliveryPoint = null;
        for (DeliveryPointBase deliveryPoint : this.deliveryPoints) {
            try {
                distance += deliveryPoint.distance(previousDeliveryPoint);

                // Keeps track of the previous order.
                if (previousDeliveryPoint == null || !previousDeliveryPoint.equals(deliveryPoint)) {
                    previousDeliveryPoint = deliveryPoint;
                }
            } catch (NumberFormatException e) {
                SystemError.handle(e);
            }
        }

        this.distance = (int) Math.round(distance);
        return this.distance;
    }

    /**
     * Gets the delivery points.
     *
     * @return The delivery points.
     */
    public ArrayList<DeliveryPointBase> getDeliveryPoints() {
        return deliveryPoints;
    }

    /**
     * Gets a delivery point.
     *
     * @param delta The delta of this point
     *
     * @return The delivery point.
     */
    public DeliveryPointBase get(int delta) {
        return this.deliveryPoints.get(delta);
    }

    /**
     * Adds a delivery point to the map.
     *
     * @param deliveryPoint The delivery point.
     */
    public void add(DeliveryPointBase deliveryPoint) {
        this.deliveryPoints.add(deliveryPoint);
    }

}
