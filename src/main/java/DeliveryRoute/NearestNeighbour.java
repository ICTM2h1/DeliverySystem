package DeliveryRoute;

import java.util.ArrayList;

/**
 * Provides an implementation for the Nearest Neighbour algorithm.
 */
public class NearestNeighbour {

    private DeliveryPointBase startPoint;

    // Closest city variables
    private DeliveryPointBase closestCity;
    private double closestDistance = Double.MAX_VALUE;
    private int closestArrayIndex;
    private final int citiesCount;

    // Route (calculation) variables
    private DeliveryPointBase currentCity;
    private final ArrayList<DeliveryPointBase> route = new ArrayList<>();
    private double routeDistance = 0;

    /**
     * Calculates the route using the Nearest Neighbour algorithm.
     *
     * Results can be fetched by the getRoute and getDistance functions.
     *
     * @param startPoint location where you start.
     * @param cities array with locations you need to pass.
     */
    public NearestNeighbour(DeliveryPoint startPoint, ArrayList<DeliveryPointBase> cities) {
        this.startPoint = startPoint;
        Integer firstAdressToDeliver = 0;

        // Calculate the amount of cities you're going to pass (excluding the trip back to starting point).
        this.citiesCount = cities.size() + 1;

        // Add the starting point to the route array.
        this.route.add(startPoint);

        // Set starting point as city the city from where you want to calculate.
        this.currentCity = startPoint;

        // Loop until you have the expected amount of cities in your route array.
        while (route.size() < citiesCount) {

            // Loop through all the (remaining) cities in the city array.
            for (DeliveryPointBase city : cities) {

                // Calculate the distance (in kilometers) between current position to the city you want to go to.
                double calculateDistance = this.currentCity.distance(city);

                // If the distance is shorter than the previous given city, overwrite the variables.
                if (calculateDistance < this.closestDistance) {
                    this.closestCity = city;
                    this.closestDistance = calculateDistance;
                    this.closestArrayIndex = cities.indexOf(city);
                }
            }

            // Remove the nearest city from the array with cities.
            cities.remove(this.closestArrayIndex);

            // Set nearest city as your current point.
            this.currentCity = this.closestCity;

            // Add the nearest city to the route array.
            this.route.add(this.closestCity);

            // Add the distance to the total distance variable.
            this.routeDistance += this.closestDistance;

            // Reset closest city distance
            this. closestDistance = Double.MAX_VALUE;

            // Incrementation necessery to define the first deliverypoint
            firstAdressToDeliver++;
        }

        // Add the starting point to the route array (again).
        this.route.add(startPoint);

        // Calculate distance between the last city and the starting point, so you can calculate the complete distance of the route.
        double endCalculateDistance = this.currentCity.distance(startPoint);

        // Add the calculated distance to the total distance variable.
        this.routeDistance += endCalculateDistance;
    }

    /**
     * Returns an NN-ordered array with cities starting and ending with the given starting point.
     *
     * @return ordered array with cities.
     */
    public ArrayList<DeliveryPointBase> getRoute() {
        return this.route;
    }

    /**
     * Returns a list of delivery points without the first one (startingpoint).
     * Function necessary since routes.remove(0) wouldn't work xd.
     *
     * @return ArrayList<DeliveryPoint> with all points except the first one.
     */
    public ArrayList<DeliveryPointBase> getRouteListItems() {
        ArrayList<DeliveryPointBase> routeListItems = new ArrayList<DeliveryPointBase>();
        boolean removedTheFirstElement = false;

        for (DeliveryPointBase city : this.getRoute()) {
            if (!city.equals(this.startPoint) || removedTheFirstElement) {
                routeListItems.add(city);
            } else {
                removedTheFirstElement = true;
            }
        }

        return routeListItems;
    }

    /**
     * Returns the NN-ordered route his distance in kilometers.
     *
     * @return double value distance in kilometers.
     */
    public double getDistance() {
        return this.routeDistance;
    }

    /**
     * Returns the given starting- and endpoint.
     *
     * @return DeliveryPointBase-value of starting point.
     */
    public DeliveryPointBase getStartPoint() {
        return this.startPoint;
    }
}
