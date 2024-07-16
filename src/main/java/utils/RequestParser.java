package utils;

import enums.HttpCode;
import enums.HttpMethod;
import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        HttpMethod method = HttpMethod.from(requestLines[0]);
        Map<String, String> queryParameters = pathParse(requestLines[1]);
        String path = queryParameters.get("path");
        String httpVersion = requestLines[2];

        // request header parse
        Map<String, String> headers = requestHeaderParse(br);

        // request body parse
        String body = requestBodyParse(headers.get(Request.CONTENT_LENGTH), br);
        String contentType = headers.get(Request.CONTENT_TYPE);
        if(contentType != null && contentType.equals(MimeType.FORM.getMimeType())) {
            Map<String, String> bodyQuerys = new HashMap<>();
            if(body != null) {
                bodyQuerys = queryParse(body, "&", "=");
            }
            queryParameters.putAll(bodyQuerys);
        }

        // cookie parse
        Map<String, String> cookies;
        String rawCookie = headers.get("Cookie");
        if(rawCookie != null) {
            cookies = queryParse(rawCookie, ";", "=");
        } else {
            cookies = new HashMap<>();
        }
        return new Request.RequestBuilder()
                .setMethod(method)
                .setPath(path)
                .setHttpVersion(httpVersion)
                .setHttpHeaders(headers)
                .setParameters(queryParameters)
                .setCookies(cookies)
                .setBody(body)
                .build();
    }

    private String[] requestLineParse(String requestLine) {
        return requestLine.split("\\s");
    }

    private String requestBodyParse(String _contentLength, BufferedReader br) throws IOException {
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

    private Map<String, String> queryParse(String queryString, String pairDelimeter, String keyValueDelimeter) {
        Map<String, String> tmpStore = new HashMap<>();
        if(queryString.isEmpty()) return tmpStore;
        String[] rawKeyValues= queryString.split(pairDelimeter);
        for(String rawKeyValue: rawKeyValues) {
            logger.debug("rawKeyValue = {}, {}, {}", rawKeyValue, pairDelimeter, keyValueDelimeter);
            String[] keyValue = rawKeyValue.split(keyValueDelimeter);
            if(keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0].trim(), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1].trim(), StandardCharsets.UTF_8);
                tmpStore.put(key, value);
            } else if(!rawKeyValue.contains(keyValueDelimeter)) {
                throw new IllegalArgumentException("잘못된 인자가 들어왔습니다.");
            }
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
        queryParameters = queryParse(rawQueryParameter, "&", "=");
        return queryParameters;
    }
}
