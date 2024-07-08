package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            Pattern headerPattern = Pattern.compile("^(.*?):\\s*(.*)$");

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Matcher matcher = headerPattern.matcher(line);
                if (matcher.matches()) {
                    headers.put(matcher.group(1).trim(), matcher.group(2).trim());
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
        
        String[] urlParts = url.split("\\?", 2);
        queryParams.put("path", urlParts[0]);
        if (urlParts.length > 1) {
            String[] queryParamsArray = urlParts[1].split("&");
            for (String param : queryParamsArray) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
    }
}