package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private final String startLine;
    private final Map<String, String> headers;
    public HttpRequestParser(BufferedReader br) throws IOException {
        startLine = br.readLine();
        headers = new HashMap<>();

        String readLine = null;
        while (!(readLine = br.readLine()).isEmpty()) {
            String[] keyValue = readLine.split(": ");
            headers.put(keyValue[0], keyValue[1]);
        }
    }

    public String getUrl() {
        return startLine.split(" ")[1];
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
