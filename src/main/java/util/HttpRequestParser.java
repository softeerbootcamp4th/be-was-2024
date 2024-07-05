package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses an HTTP request.
 */
public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    /**
     * Parses the given reader into an HttpRequest.
     *
     * @param reader the reader to parse
     * @return the parsed HttpRequest
     * @throws IOException if an I/O error occurs
     */
    public HttpRequest parse(BufferedReader reader) throws IOException {
        String method = "";
        String url = "";
        String httpVersion = "";
        String path = "";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();

        String line = reader.readLine();
        if (line != null && !line.isEmpty()) {
            String[] requestLine = line.split(" ");
            method = requestLine[0];
            url = requestLine[1];
            httpVersion = requestLine[2];
            parseUrl(url, queryParams);
            path = queryParams.remove("path");  // parseUrl에서 설정한 path

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] header = line.split(": ");
                if (header.length == 2) {
                    headers.put(header[0], header[1]);
                }
            }
        }

        return new HttpRequest(method, url, path, httpVersion, headers, queryParams);
    }

    /**
     * Parses the given URL into a path and query parameters.
     *
     * @param url the URL to parse
     * @param queryParams the map to store the query parameters for case: /path?key=value
     */
    private void parseUrl(String url, Map<String, String> queryParams) {
        int queryIndex = url.indexOf("?");
        if (queryIndex != -1) {
            queryParams.put("path", url.substring(0, queryIndex));
            String queryString = url.substring(queryIndex + 1);
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        } else {
            queryParams.put("path", url);
        }
    }
}