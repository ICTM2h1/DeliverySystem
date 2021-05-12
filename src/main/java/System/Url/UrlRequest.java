package System.Url;

import System.Error.SystemError;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Provides a class for sending url requests.
 */
public class UrlRequest {

    private final HttpRequest.Builder httpBuilder;

    /**
     * Constructs a new url request.
     *
     * @param url The url.
     */
    public UrlRequest(String url) {
        this.httpBuilder = HttpRequest.newBuilder(URI.create(url));
    }

    /**
     * Adds a header to the http request.
     *
     * @param key The key.
     * @param value The value.
     */
    public void addHeader(String key, String value) {
        this.httpBuilder.header(key, value);
    }

    /**
     * Sends the requests and returns the response.
     *
     * @return The response body.
     */
    public UrlResponse send() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = this.httpBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new UrlResponse(response);
        } catch (IOException | InterruptedException e) {
            SystemError.handle(e);
        }

        return null;
    }

}
