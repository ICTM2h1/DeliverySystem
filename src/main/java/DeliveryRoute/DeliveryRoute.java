package DeliveryRoute;

import System.Error.SystemError;

import java.util.LinkedHashMap;

/**
 * Provides a delivery route for deliverers.
 */
public class DeliveryRoute {

    public final static int deliverers = 12;

    private final int id;
    private int distance;

    private LinkedHashMap<Integer, DeliveryPoint> deliveryPoints;

    /**
     * Creates a new delivery route.
     *
     * @param id The id of this route.
     * @param capacity The capacity of this route.
     */
    public DeliveryRoute(int id, int capacity) {
        this.id = id + 1;
        this.deliveryPoints = new LinkedHashMap<>(capacity);
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
        int orderCount = this.deliveryPoints.size();
        int middlePointKey = (int) Math.round((double) orderCount / 2);
        DeliveryPoint firstPoint = this.deliveryPoints.get(0);
        DeliveryPoint middlePoint = this.deliveryPoints.get(middlePointKey);
        DeliveryPoint lastPoint = this.deliveryPoints.get(orderCount - 1);

        String name = firstPoint.getOrder().get("CityName") + " - ";
        name += middlePoint.getOrder().get("CityName") + " - ";
        name += lastPoint.getOrder().get("CityName");

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
        for (DeliveryPoint deliveryPoint : this.deliveryPoints.values()) {
            try {
                distance += Double.parseDouble(deliveryPoint.getOrder().get("geometry.distance"));
            } catch (NumberFormatException e) {
                SystemError.handle(e);
            }
        }

        this.distance = (int) Math.round(distance);
        return this.distance;
    }

    /**
     * Adds a delivery point to the map.
     *
     * @param key The key of the delivery point.
     * @param deliveryPoint The delivery point.
     */
    public void add(int key, DeliveryPoint deliveryPoint) {
        this.deliveryPoints.put(key, deliveryPoint);
    }

}
