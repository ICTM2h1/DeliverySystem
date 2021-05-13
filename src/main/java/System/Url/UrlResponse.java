package System.Url;

import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonParser;

import java.net.http.HttpResponse;

/**
 * Provides a class for processing the response.
 */
public class UrlResponse {

    private final HttpResponse<String> httpResponse;

    /**
     * Creates a new url response.
     *
     * @param httpResponse The http response.
     */
    public UrlResponse(HttpResponse<String> httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Renders the string to json.
     *
     * @return The JSON resonse.
     */
    public DbDoc toJson() {
        return JsonParser.parseDoc(this.httpResponse.body());
    }

}
