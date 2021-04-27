package DeliveryRoute;

/**
 * Provides a class for calculating the distance between two locations.
 */
public class DeliveryLocation {

    private final double latitude, longitude, altitude;
    private final int earthRadius = 6371; // Radius in kilometers.

    /**
     * Constructs a new location object.
     *
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @param altitude Altitude in meters.
     */
    public DeliveryLocation(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * This function is copied from stackoverflow.
     * @link https://stackoverflow.com/questions/3694380
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @return Distance in meters.
     */
    public double distance(DeliveryLocation location) {
        if (location == null || this.getLatitude() == 0.0 || this.getLongitude() == 0.0
                || location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
            return 0;
        }

        double latDistance = Math.toRadians(location.getLatitude() - this.getLatitude());
        double lonDistance = Math.toRadians(location.getLongitude() - this.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.getLatitude())) * Math.cos(Math.toRadians(location.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c * 1000; // convert to meters
        double height = this.getAltitude() - location.getAltitude();

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * Gets the latitude.
     *
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the altitude.
     *
     * @return The altitude in meters.
     */
    public double getAltitude() {
        return altitude;
    }

}
