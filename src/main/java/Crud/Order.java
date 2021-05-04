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
        this.addSelectField("Altitude");
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
        ArrayList<LinkedHashMap<String, String>> entities = this.all();
        ArrayList<LinkedHashMap<String, String>> orders = new ArrayList<>();
        LinkedHashMap<String, String> nextOrder;
        int counter = 0;
        for (LinkedHashMap<String, String> order : entities) {
            int nextOrderIndex = counter + 1;
            if (nextOrderIndex >= entities.size()) {
                nextOrderIndex = 0;
            }

            nextOrder = entities.get(nextOrderIndex);

            double latitude, longitude, altitude;
            double distance = calculateDistance(order, nextOrder);
            try {
                latitude = Double.parseDouble(order.get("Latitude"));
                longitude = Double.parseDouble(order.get("Longitude"));
                altitude = Double.parseDouble(order.get("Altitude"));
            } catch (NumberFormatException e) {
                continue;
            }

            if (latitude == 0 || longitude == 0) {
                this.withoutGeometry++;
            }

            order.put("geometry.distance", String.valueOf(distance));
            order.put("geometry.latitude", String.valueOf(latitude));
            order.put("geometry.longitude", String.valueOf(longitude));
            order.put("geometry.altitude", String.valueOf(altitude));
            orders.add(order);
        }

        orders.sort((order, compareOrder) -> {
            try {
                Double distance = Double.valueOf(order.get("geometry.distance"));
                Double compareDistance = Double.valueOf(compareOrder.get("geometry.distance"));

                return distance.compareTo(compareDistance);
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
     * @return The distance between two orders, in kilometers.
     */
    public double calculateDistance(LinkedHashMap<String, String> order, LinkedHashMap<String, String> compareOrder) {
        String customerID = order.get("CustomerID");
        String city = order.get("CityName");
        String deliveryAddressLine1 = order.get("DeliveryAddressLine1");
        String postalCode = order.get("DeliveryPostalCode");
        String latitudeString = order.get("Latitude");
        String longitudeString = order.get("Longitude");
        String altitudeString = order.get("Altitude");

        // This is an expensive task, so we run this only when the order does not have geometric data. After the
        // calculation we save it into the customer in order to skip this the next time we want to get the orders.
        if (latitudeString == null || longitudeString == null || altitudeString == null) {
            DeliveryAddress deliveryAddress = new DeliveryAddress(deliveryAddressLine1, postalCode, city);
            LinkedHashMap<String, BigDecimal> geometry = deliveryAddress.toGeometry();
            latitudeString = "0";
            longitudeString = "0";
            altitudeString = "0";
            if (geometry != null) {
                latitudeString = String.valueOf(geometry.get("latitude"));
                longitudeString = String.valueOf(geometry.get("longitude"));
                altitudeString = String.valueOf(geometry.get("altitude"));
            }

            // Write the updates values back to the order.
            order.put("Latitude", latitudeString);
            order.put("Longitude", longitudeString);
            order.put("Altitude", altitudeString);

            this.customer.addValue("Longitude", longitudeString);
            this.customer.addValue("Latitude", latitudeString);
            this.customer.addValue("Altitude", altitudeString);
            this.customer.addCondition("CustomerID", customerID);
            this.customer.update();
        }

        if (compareOrder == null) {
            return 0;
        }

        try {
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);
            double altitude = Double.parseDouble(altitudeString);
            double compareLatitude = latitude;
            double compareLongitude = longitude;
            double compareAltitude = altitude;

            String previousLatitudeString = compareOrder.get("Latitude");
            String previousLongitudeString = compareOrder.get("Longitude");
            String previousAltitudeString = compareOrder.get("Altitude");
            if (previousLongitudeString != null && previousLatitudeString != null && previousAltitudeString != null) {
                compareLatitude = Double.parseDouble(previousLatitudeString);
                compareLongitude = Double.parseDouble(previousLongitudeString);
                compareAltitude = Double.parseDouble(previousAltitudeString);
            }

            DeliveryLocation location = new DeliveryLocation(latitude, longitude, altitude);
            DeliveryLocation compareLocation = new DeliveryLocation(compareLatitude, compareLongitude, compareAltitude);

            return location.distance(compareLocation);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
