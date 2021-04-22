package DeliveryRoute;

import Crud.Orders;
import System.Config.Config;
import System.Url.UrlRequest;
import System.Url.UrlResponse;
import com.mysql.cj.xdevapi.DbDocImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonNumber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoute {

    private final Config config = Config.getInstance();
    private final ArrayList<HashMap<String, String>> rawOrders;
    private final ArrayList<HashMap<String, String>> orders = new ArrayList<>();

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoute(String date) {
        Orders orders = new Orders(date);
        this.rawOrders = orders.all();

        for (HashMap<String, String> order : this.rawOrders) {
            System.out.println(order);

            String city = order.get("CityName");
            String deliveryAddressLine1 = order.get("DeliveryAddressLine1");
            String postalCode = order.get("DeliveryPostalCode");
            String address = deliveryAddressLine1 + ",+" + postalCode + ",+" + city;
            address = address.replace(" ", "+");

            HashMap<String, BigDecimal> geometry = this.addressToGeometry(address);
            if (geometry == null) {
                continue;
            }

            order.put("geometry.latitude", String.valueOf(geometry.get("latitude")));
            order.put("geometry.longitude", String.valueOf(geometry.get("longitude")));
            this.orders.add(order);
        }

        System.out.println(this.orders.get(0));
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
