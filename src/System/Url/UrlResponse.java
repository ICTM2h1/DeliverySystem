package System.Url;

import System.Error.SystemError;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;

/**
 * Provides a class for processing the response.
 */
public class UrlResponse {

    private HttpResponse<String> httpResponse;

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
