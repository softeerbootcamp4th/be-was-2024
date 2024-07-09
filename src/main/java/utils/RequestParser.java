package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private static class LazyRequestParser {
        public static RequestParser instance = new RequestParser();
    }

    public static RequestParser getRequestParser() {
        return LazyRequestParser.instance;
    }

    private RequestParser() {}

    public Request getRequest(InputStream rawHttpRequest) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(rawHttpRequest));

        // request line parse
        String[] requestLines = requestLineParse(br.readLine());
        String method = requestLines[0];
        Map<String, String> queryParameters = pathParse(requestLines[1]);
        String path = queryParameters.get("path");
        queryParameters.remove("path");
        String httpVersion = requestLines[2];

        // request header parse
        Map<String, String> headers = requestHeaderParse(br);

        // request body parse
        String body = requestBodyParse(headers.get("Content-Length"), br);
        String contentType = headers.get("Content-Type");
        if(contentType != null && contentType.equals("application/x-www-form-urlencoded")) {
            Map<String, String> bodyQuerys = queryParse(body);
            queryParameters.putAll(bodyQuerys);
        }
        return new Request(method, path, httpVersion, headers, queryParameters, body);
    }

    private String[] requestLineParse(String requestLine) {
        return requestLine.split("\\s");
    }

    private String requestBodyParse(String _contentLength, BufferedReader br) throws IOException {
        Map<String, String> tmpStore = new HashMap<>();
        if(_contentLength == null) {
            return null;
        }
        int contentLength = Integer.parseInt(_contentLength);
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        String s = String.valueOf(body);
        return s;

    }

    private Map<String, String> requestHeaderParse(BufferedReader br) throws IOException {
        String tmp;
        Map<String, String> headers = new HashMap<>();
        while((tmp = br.readLine()) != null && !tmp.isEmpty()) {
            int delimeterLoc = tmp.indexOf(":");
            if(delimeterLoc < 0) {
               throw new IllegalStateException("유효하지 않은 Http Header 형식");
            }
            String key = tmp.substring(0, delimeterLoc).trim();
            String value = tmp.substring(delimeterLoc + 1).trim();
            headers.put(key, value);
        }
        return headers;
    }

    private Map<String, String> queryParse(String queryString) {
        Map<String, String> tmpStore = new HashMap<>();
        String[] rawKeyValues= queryString.split("\\&");
        for(String rawKeyValue: rawKeyValues) {
            String[] keyValue = rawKeyValue.split("\\=");
            tmpStore.put(keyValue[0], keyValue[1]);
        }
        return tmpStore;
    }

    private Map<String, String> pathParse(String path) {
        // api path 파싱
        String[] pathSplit = path.split("\\?");
        String apiPath = pathSplit[0];
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("path", apiPath);

        if(pathSplit.length == 1) return queryParameters;

        String rawQueryParameter = pathSplit[1];
        String[] rawQueryParameters = rawQueryParameter.split("\\&");
        for(String queryParameter: rawQueryParameters) {
            String[] queryKeyValue = queryParameter.split("\\=");
            queryParameters.put(queryKeyValue[0], queryKeyValue[1]);
        }
        return queryParameters;
    }
}
