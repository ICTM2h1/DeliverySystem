package Crud;

import DeliveryRoute.DeliveryAddress;
import DeliveryRoute.DeliveryLocation;
import System.Error.SystemError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for interacting with the order table.
 */
public class Order extends CrudBase {

    protected Customer customer = new Customer();

    protected String date;
    protected int withoutGeometry;

    /**
     * Constructs a new orders object.
     */
    public Order() {
        super("orders", "OrderID");

        this.addSelectField("OrderID");
        this.addSelectField("CustomerID");
        this.addSelectField("ExpectedDeliveryDate");
        this.addSelectField("CityName");
        this.addSelectField("DeliveryAddressLine1");
        this.addSelectField("DeliveryPostalCode");
        this.addSelectField("Longitude");
        this.addSelectField("Latitude");
    }

    /**
     * Constructs a new orders object.
     *
     * @param date The date to filter on.
     */
    public Order(String date) {
        this();

        this.date = date;
    }

    /**
     * Gets all orders from table.
     *
     * @return An array list with the selected orders.
     */
    public ArrayList<LinkedHashMap<String, String>> all() {
        String query = "SELECT * FROM orders O " +
                "INNER JOIN customers CU ON O.CustomerID = CU.CustomerID \n" +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID ";
        if (this.date != null && !this.date.isEmpty()) {
            this.bindParam("ExpectedDeliveryDate", this.date);

            query += " WHERE ExpectedDeliveryDate = :ExpectedDeliveryDate";
        }

        return super.all(query);
    }

    /**
     * Gets the orders filtered on geometric data.
     *
     * @return An array list with the sorted orders.
     */
    public ArrayList<LinkedHashMap<String, String>> filterOnGeometry() {
        ArrayList<LinkedHashMap<String, String>> orders = new ArrayList<>();
        LinkedHashMap<String, String> previousOrder = null;
        for (LinkedHashMap<String, String> order : this.all()) {
            String latitudeString = String.valueOf(order.get("Latitude"));
            String longitudeString = String.valueOf(order.get("Longitude"));

            double distance = calculateDistance(order, previousOrder);

            double latitude, longitude;
            try {
                latitude = Double.parseDouble(latitudeString);
                longitude = Double.parseDouble(longitudeString);
            } catch (NumberFormatException e) {
                continue;
            }

            if (latitude == 0 || longitude == 0) {
                this.withoutGeometry++;
            }

            order.put("geometry.distance", String.valueOf(distance));
            order.put("geometry.latitude", latitudeString);
            order.put("geometry.longitude", longitudeString);
            orders.add(order);

            // Keeps track of the previous order.
            if (previousOrder == null || !previousOrder.equals(order)) {
                previousOrder = order;
            }
        }

        orders.sort((order, order_two) -> {
            try {
                Double distance = Double.valueOf(order.get("geometry.distance"));
                Double distance_two = Double.valueOf(order_two.get("geometry.distance"));

                return distance.compareTo(distance_two);
            } catch (NumberFormatException e) {
                SystemError.handle(e);
            }

            return 0;
        });

        return orders;
    }

    /**
     * Gets the number of orders without geometry.
     *
     * @return The number of orders.
     */
    public int getWithoutGeometry() {
        return this.withoutGeometry;
    }

    /**
     * Calculates the distance between two orders.
     *
     * @param order The order.
     * @param compareOrder The order to compare the current one to.
     *
     * @return The distance between two orders.
     */
    private double calculateDistance(LinkedHashMap<String, String> order, LinkedHashMap<String, String> compareOrder) {
        String customerID = order.get("CustomerID");
        String city = order.get("CityName");
        String deliveryAddressLine1 = order.get("DeliveryAddressLine1");
        String postalCode = order.get("DeliveryPostalCode");
        String latitudeString = String.valueOf(order.get("Latitude"));
        String longitudeString = String.valueOf(order.get("Longitude"));

        // This is an expensive task, so we run this only when the order does not have geometric data. After the
        // calculation we save it into the customer in order to skip this the next time we want to get the orders.
        if (latitudeString.equals("null") || longitudeString.equals("null")) {
            DeliveryAddress deliveryAddress = new DeliveryAddress(deliveryAddressLine1, postalCode, city);
            LinkedHashMap<String, BigDecimal> geometry = deliveryAddress.toGeometry();
            latitudeString = "0";
            longitudeString = "0";
            if (geometry != null) {
                latitudeString = String.valueOf(geometry.get("latitude"));
                longitudeString = String.valueOf(geometry.get("longitude"));
            }

            this.customer.addValue("Longitude", longitudeString);
            this.customer.addValue("Latitude", latitudeString);
            this.customer.addCondition("CustomerID", customerID);
            this.customer.update();
        }

        double latitude = Double.parseDouble(latitudeString);
        double longitude = Double.parseDouble(longitudeString);
        double latitude_two = latitude;
        double longitude_two = longitude;
        if (compareOrder != null) {
            String previousLatitudeString = String.valueOf(compareOrder.get("Latitude"));
            String previousLongitudeString = String.valueOf(compareOrder.get("Longitude"));
            if (!previousLongitudeString.equals("null") && !previousLatitudeString.equals("null")) {
                latitude_two = Double.parseDouble(previousLatitudeString);
                longitude_two = Double.parseDouble(previousLongitudeString);
            }
        }

        DeliveryLocation location = new DeliveryLocation(latitude, longitude, 0);
        DeliveryLocation location_two = new DeliveryLocation(latitude_two, longitude_two, 0);

        return location.distance(location_two);
    }

}
