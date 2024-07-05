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
        String bodyPart = "";
        if (messageSplit.length > 1) {
            bodyPart = messageSplit[1];
        }

        String[] headerSplit = headerPart.split("\n",2);
        String startLine = headerSplit[0];

        String[] startLineSplit = startLine.split(" ");
        String method = startLineSplit[0];
        String uri = startLineSplit[1];
        String version = startLineSplit[2];

        String[] headerArray =  headerSplit[1].split("\n");
        Map<String, String> headers = new HashMap<>();
        for (String header : headerArray) {
            String[] headerParts = header.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }

        return new HttpRequestMessage(method,uri,version,headers,bodyPart);
    }
}
