package webserver;

import data.HttpRequestMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    public static String getRequestString(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String tempStr;
        StringBuilder stringBuilder = new StringBuilder();
        while ((tempStr = bufferedReader.readLine()) != null && !tempStr.isEmpty()) {
            stringBuilder.append(tempStr);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static HttpRequestMessage getHttpRequestMessage(String requestMessage){
        String[] messageSplit = requestMessage.split("\n\n",2);
        String headerPart = messageSplit[0];
        String bodyPart = messageSplit.length > 1 ? messageSplit[1] : "";
        return parseAndCreateHttpRequestMessage(headerPart, bodyPart);
    }

    private static HttpRequestMessage parseAndCreateHttpRequestMessage(String headerPart, String bodyPart) {
        Map<String, String> queryParams = new HashMap<>();
        String[] headerSplit = headerPart.split("\n",2);
        String startLine = headerSplit[0];

        String[] startLineSplit = startLine.split(" ");
        String method = startLineSplit[0];
        String uri = startLineSplit[1];
        String version = startLineSplit[2];
        if (uri.contains("?")) {
            uri = processQuery(uri, queryParams);
        }
        String[] headerArray =  headerSplit[1].split("\n");
        Map<String, String> headers = getHeaderMap(headerArray);
        return new HttpRequestMessage(method,uri,version,queryParams,headers, bodyPart);
    }

    private static Map<String, String> getHeaderMap(String[] headerArray) {
        Map<String, String> headers = new HashMap<>();
        for (String header : headerArray) {
            String[] headerParts = header.split(":", 2);
            headers.put(headerParts[0].strip(), headerParts[1].strip());
        }
        return headers;
    }

    private static String processQuery(String uri, Map<String, String> queryParams) {
        String queryString = uri.substring(uri.indexOf("?") + 1);
        String[] queries = queryString.split("&");
        for (String query : queries) {
            String[] entry = query.split("=");
            queryParams.put(entry[0], entry[1]);
        }
        uri = uri.substring(0, uri.indexOf("?"));
        return uri;
    }
}
