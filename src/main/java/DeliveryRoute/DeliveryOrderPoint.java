package DeliveryRoute;

import java.util.LinkedHashMap;

/**
 * Represents a delivery point within a delivery route.
 */
public class DeliveryOrderPoint extends DeliveryPointBase {

    private final LinkedHashMap<String, String> order;

    /**
     * Creates a new delivery point.
     *
     * @param order The order.
     */
    public DeliveryOrderPoint(LinkedHashMap<String, String> order) {
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
     * {@inheritDoc}
     */
    @Override
    public String getPostalCode() {
        return this.order.get("DeliveryPostalCode");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHouseNumber() {
        String[] address = this.order.get("DeliveryAddressLine1").split(" ");
        StringBuilder houseNumber = new StringBuilder();
        for (String addressPiece : address) {
            if (addressPiece.matches(".*\\d.*")) {
                houseNumber.append(" ").append(addressPiece);
            }
        }

        return houseNumber.toString();
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
class DeliveryPoint extends DeliveryPointBase {

    private final String name, postalCode;
    private final int houseNumber;
    private final double longitude, latitude, altitude;

    /**
     * Creates a new delivery start point.
     *
     * @param name The name.
     * @param postalCode The postal code.
     * @param houseNumber The house number.
     * @param longitude The longitude.
     * @param latitude The latitude.
     * @param altitude The altitude.
     */
    public DeliveryPoint(String name, String postalCode, int houseNumber, double longitude, double latitude, double altitude) {
        this.name = name;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String label() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHouseNumber() {
        return " " + this.houseNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAltitude() {
        return this.altitude;
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
     * Gets the address label of this delivery point.
     *
     * @return The address label of this delivery point.
     */
    public String addressLabel() {
        return String.format("%s (%s,%s)", this.label(), this.getPostalCode(), this.getHouseNumber());
    }

    /**
     * Gets the postal code of the delivery point.
     *
     * @return The postal code.
     */
    public abstract String getPostalCode();

    /**
     * Gets the house number of the delivery point.
     *
     * @return The house number.
     */
    public abstract String getHouseNumber();

    /**
     * Calculates the distance between 2 delivery points.
     *
     * @param deliveryPoint The delivery point.
     *
     * @return The distance between the delivery points.
     */
    public int distance(DeliveryPointBase deliveryPoint) {
        if (deliveryPoint == null) {
            return 0;
        }

        DeliveryLocation location = new DeliveryLocation(this.getLatitude(), this.getLongitude(), this.getAltitude());
        DeliveryLocation compareLocation = new DeliveryLocation(deliveryPoint.getLatitude(), deliveryPoint.getLongitude(), deliveryPoint.getAltitude());

        return (int) Math.round(location.distance(compareLocation));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof DeliveryPointBase)) {
            return false;
        }

        DeliveryPointBase that = (DeliveryPointBase) obj;

        return this.getAltitude() == that.getAltitude()
                && this.getLongitude() == that.getLongitude()
                && this.getLatitude() == that.getLatitude();
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