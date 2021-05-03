package DeliveryRoute;

import java.util.LinkedHashMap;

/**
 * Represents a delivery point within a delivery route.
 */
public class DeliveryPoint extends DeliveryPointBase {

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
     * {@inheritDoc}
     */
    @Override
    public String label() {
        return this.order.get("CityName");
    }

    /**
     * Gets the order.
     *
     * @return The order.
     */
    public LinkedHashMap<String, String> getOrder() {
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLatitude() {
        try {
            return Double.parseDouble(this.getOrder().getOrDefault("Latitude", "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLongitude() {
        try {
            return Double.parseDouble(this.getOrder().getOrDefault("Longitude", "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAltitude() {
        return 0;
    }

}

/**
 * Provides a delivery start point.
 */
class DeliveryStartPoint extends DeliveryPointBase {

    private final String name;
    private final double longitude, latitude, altitude;

    /**
     * Creates a new delivery start point.
     *
     * @param name The name.
     * @param longitude The longitude.
     * @param latitude The latitude.
     * @param altitude The altitude.
     */
    public DeliveryStartPoint(String name, double longitude, double latitude, double altitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    @Override
    public String label() {
        return null;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getAltitude() {
        return 0;
    }

}

/**
 * Provides a base for delivery points.
 */
abstract class DeliveryPointBase {

    /**
     * Gets the label of this delivery point.
     *
     * @return The label of this delivery point.
     */
    public abstract String label();

    /**
     * Calculates the distance between 2 delivery points.
     *
     * @param deliveryPoint The delivery point.
     *
     * @return The distance between the delivery points.
     */
    public double distance(DeliveryPointBase deliveryPoint) {
        if (deliveryPoint == null) {
            return 0;
        }

        DeliveryLocation location = new DeliveryLocation(this.getLatitude(), this.getLongitude(), this.getAltitude());
        DeliveryLocation location_two = new DeliveryLocation(deliveryPoint.getLatitude(), deliveryPoint.getLongitude(), deliveryPoint.getAltitude());

        return location.distance(location_two);
    }

    /**
     * Gets the longitude.
     *
     * @return The longitude.
     */
    public abstract double getLongitude();

    /**
     * Gets the latitude.
     *
     * @return The latitude.
     */
    public abstract double getLatitude();

    /**
     * Gets the altitude.
     *
     * @return The altitude.
     */
    public abstract double getAltitude();

}