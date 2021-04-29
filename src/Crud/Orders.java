package Crud;

import DeliveryRoute.DeliveryLocation;
import System.Config.Config;
import System.Url.UrlRequest;
import System.Url.UrlResponse;
import com.mysql.cj.xdevapi.DbDocImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonNumber;

import java.math.BigDecimal;
import java.util.*;

/**
 * Provides a class for interacting with the order table.
 */
public class Orders extends CrudBase {

    protected Config config = Config.getInstance();

    protected String date;

    /**
     * Constructs a new orders object.
     */
    public Orders() {
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
    public Orders(String date) {
        this();

        this.date = date;
    }

    /**
     * Gets all orders from table.
     *
     * @return An array list with the selected orders.
     */
    public ArrayList<HashMap<String, String>> all() {
        String query = "SELECT * FROM orders O " +
                "INNER JOIN customers CU ON O.CustomerID = CU.CustomerID \n" +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID ";
        if (this.date != null && !this.date.isEmpty()) {
            this.bindParam("ExpectedDeliveryDate", this.date);

            query += " WHERE ExpectedDeliveryDate = :ExpectedDeliveryDate";
        }

        return super.all(query + " LIMIT 10");
    }

    /**
     * Gets the orders filtered on geometric data.
     *
     * @return An array list with the sorted orders.
     */
    public ArrayList<HashMap<String, String>> filterOnGeometry() {
        Customer customer = new Customer();
        ArrayList<HashMap<String, String>> orders = new ArrayList<>();
        HashMap<String, String> previousOrder = null;
        for (HashMap<String, String> order : this.all()) {
            String customerID = order.get("CustomerID");
            String city = order.get("CityName");
            String deliveryAddressLine1 = order.get("DeliveryAddressLine1");
            String postalCode = order.get("DeliveryPostalCode");
            String address = deliveryAddressLine1 + ",+" + postalCode + ",+" + city;
            address = address.replace(" ", "+");

            String latitudeString = String.valueOf(order.get("Latitude"));
            String longitudeString = String.valueOf(order.get("Longitude"));

            // This is an expensive task, so we run this only when the order does not have geometric data. After the
            // calculation we save it into the customer in order to skip this the next time we want to get the orders.
            if (latitudeString.equals("null") || longitudeString.equals("null")) {
                HashMap<String, BigDecimal> geometry = this.addressToGeometry(address);
                latitudeString = "0";
                longitudeString = "0";
                if (geometry != null) {
                    latitudeString = String.valueOf(geometry.get("latitude"));
                    longitudeString = String.valueOf(geometry.get("longitude"));
                }

                customer.addValue("Longitude", longitudeString);
                customer.addValue("Latitude", latitudeString);
                customer.addCondition("CustomerID", customerID);
                customer.update();
            }

            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);
            double latitude_two = latitude;
            double longitude_two = longitude;
            if (previousOrder != null) {
                latitude_two = Double.parseDouble(previousOrder.get("Latitude"));
                longitude_two = Double.parseDouble(previousOrder.get("Longitude"));
            }

            DeliveryLocation location = new DeliveryLocation(latitude, longitude, 0);
            DeliveryLocation location_two = new DeliveryLocation(latitude_two, longitude_two, 0);
            double distance = location.distance(location_two);

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
            Double distance = Double.valueOf(order.get("geometry.distance"));
            Double distance_two = Double.valueOf(order_two.get("geometry.distance"));

            return distance.compareTo(distance_two);
        });

        return orders;
    }

    /**
     * Renders an address to geometric data.
     *
     * @param address The address.
     *
     * @return A hash map with the geometric data.
     */
    private HashMap<String, BigDecimal> addressToGeometry(String address) {
        String url = this.config.get("google_maps_api_url") + "?" + "address="+ address + "&key=" + this.config.get("google_maps_api_key");

        UrlRequest urlRequest = new UrlRequest(url);
        urlRequest.addHeader("accept", "application/json");
        UrlResponse urlResponse = urlRequest.send();
        JsonArray jsonResponse = (JsonArray) urlResponse.toJson().get("results");
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }

        DbDocImpl result = (DbDocImpl) jsonResponse.get(0);
        if (result == null || result.isEmpty()) {
            return null;
        }

        DbDocImpl geometry = (DbDocImpl) result.get("geometry");
        if (geometry == null || geometry.isEmpty()) {
            return null;
        }

        DbDocImpl location = (DbDocImpl) geometry.get("location");
        if (location == null || location.isEmpty()) {
            return null;
        }

        JsonNumber latitude = (JsonNumber) location.get("lat");
        JsonNumber longitude = (JsonNumber) location.get("lng");

        HashMap<String, BigDecimal> items = new HashMap<>();
        items.put("latitude", latitude.getBigDecimal());
        items.put("longitude", longitude.getBigDecimal());

        return items;
    }

}
