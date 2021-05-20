package Crud;

import DeliveryRoute.DeliveryAddress;
import System.Error.SystemError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for interacting with the order table.
 */
// @todo replace the status = 0 value for the correct value.
public class Order extends CrudBase {

    protected final Customer customer = new Customer();

    protected String date;

    /**
     * Constructs a new orders object.
     */
    public Order() {
        super("orders", "OrderID");

        this.addSelectField("OrderID");
        this.addSelectField("CustomerID");
        this.addSelectField("OrderDate");
        this.addSelectField("ExpectedDeliveryDate");
        this.addSelectField("CityName");
        this.addSelectField("DeliveryAddressLine1");
        this.addSelectField("DeliveryPostalCode");
        this.addSelectField("Longitude");
        this.addSelectField("Latitude");
        this.addSelectField("Altitude");
        this.addSelectField("Status");
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
                "INNER JOIN customers CU ON O.CustomerID = CU.CustomerID " +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID " +
                "WHERE O.Status = 0 ";
        if (this.date != null && !this.date.isEmpty()) {
            this.bindParam("ExpectedDeliveryDate", this.date);

            query += "AND ExpectedDeliveryDate = :ExpectedDeliveryDate ";
        }
        query += "ORDER BY OrderID ";

        return super.all(query);
    }

    /**
     * Gets all orders from table.
     *
     * @return An array list with the selected orders.
     */
    public ArrayList<LinkedHashMap<String, String>> allLimited() {
        String query = "SELECT * FROM orders O " +
                "INNER JOIN customers CU ON O.CustomerID = CU.CustomerID \n" +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID " +
                "WHERE YEAR(OrderDate) >= 2020 " +
                "AND O.Status = 0 " +
                "ORDER BY ExpectedDeliveryDate DESC " +
                "LIMIT 1000";

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

        for (LinkedHashMap<String, String> order : entities) {
            String postalCode = order.get("DeliveryPostalCode").replaceAll("[^0-9]", "");

            int numericPostalCode;
            double latitude, longitude, altitude;
            saveGeometricDataOnce(order);
            try {
                numericPostalCode = Integer.parseInt(postalCode);
                latitude = Double.parseDouble(order.get("Latitude"));
                longitude = Double.parseDouble(order.get("Longitude"));
                altitude = Double.parseDouble(order.get("Altitude"));
            } catch (NumberFormatException e) {
                continue;
            }

            order.put("geometry.postalCode", String.valueOf(numericPostalCode));
            order.put("geometry.latitude", String.valueOf(latitude));
            order.put("geometry.longitude", String.valueOf(longitude));
            order.put("geometry.altitude", String.valueOf(altitude));
            orders.add(order);
        }

        orders.sort((order, compareOrder) -> {
            try {
                Integer distance = Integer.valueOf(order.get("geometry.postalCode"));
                Integer compareDistance = Integer.valueOf(compareOrder.get("geometry.postalCode"));

                return distance.compareTo(compareDistance);
            } catch (NumberFormatException e) {
                SystemError.handle(e);
            }

            return 0;
        });

        return orders;
    }

    /**
     * Saves the geometric data from an order into a customer.
     *
     * @param order The order.
     */
    public void saveGeometricDataOnce(LinkedHashMap<String, String> order) {
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
    }

}
