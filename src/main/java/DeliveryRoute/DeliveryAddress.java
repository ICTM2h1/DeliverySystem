package DeliveryRoute;

import System.Config.Config;
import System.Url.UrlRequest;
import System.Url.UrlResponse;
import com.mysql.cj.xdevapi.DbDocImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonNumber;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Provides a class for the delivery address with generic behavior.
 */
public class DeliveryAddress {

    protected Config config = Config.getInstance();

    private String address, postalCode, city;

    /**
     * Creates a new delivery address.
     *
     * @param address The address.
     * @param postalCode The postal code.
     * @param city The city.
     */
    public DeliveryAddress(String address, String postalCode, String city) {
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

    /**
     * Renders an address to geometric data.
     *
     * @return A hash map with the geometric data.
     */
    public LinkedHashMap<String, BigDecimal> toGeometry() {
        String address = String.format("%s,+%s,+%s", this.address, this.postalCode, this.city).replace(" ", "+");
        String url = this.config.get("google_maps_api_geocode_url") + "?" + "address="+ address + "&key=" + this.config.get("google_maps_api_key");

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
        JsonNumber altitude = this.toAltitude(longitude.getBigDecimal().doubleValue(), latitude.getBigDecimal().doubleValue());

        LinkedHashMap<String, BigDecimal> items = new LinkedHashMap<>();
        items.put("latitude", latitude.getBigDecimal());
        items.put("longitude", longitude.getBigDecimal());
        items.put("altitude", altitude.getBigDecimal());

        return items;
    }

    /**
     * Renders a location, in geometric form, to altitude.
     *
     * @param longitude The longitude.
     * @param latitude The latitude.
     *
     * @return A resolution value, indicating the maximum distance between data points from which the elevation was
     *         interpolated, in meters. This property will be missing if the resolution is not known. Note that
     *         elevation data becomes more coarse (larger resolution values) when multiple points are passed. To obtain
     *         the most accurate elevation value for a point, it should be queried independently.
     */
    public JsonNumber toAltitude(double longitude, double latitude) {
        String location = String.format("%s,%s", latitude, longitude);
        String url = this.config.get("google_maps_api_elevation_url") + "?" + "locations="+ location + "&key=" + this.config.get("google_maps_api_key");

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

        return (JsonNumber) result.get("elevation");
    }

}
