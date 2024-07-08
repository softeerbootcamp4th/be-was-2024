package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        Request request = new Request();
        BufferedReader br = new BufferedReader(new InputStreamReader(rawHttpRequest));
        requestLineParse(request, br.readLine());
        requestHeaderParse(request, br);
        requestBodyParse(request, br);
        return request;
    }

    private void requestBodyParse(Request request, BufferedReader br) throws IOException {
        String _contentLength = request.getHeader("Content-Length");
        if(_contentLength == null) {
            request.setBody(null);
            return;
        }
        int contentLength = Integer.parseInt(_contentLength);
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        request.setBody(String.valueOf(body));
    }

    private void requestHeaderParse(Request request, BufferedReader br) throws IOException {
        String tmp;
        Map<String, String> headers = new ConcurrentHashMap<>();
        while((tmp = br.readLine()) != null && !tmp.isEmpty()) {
            int delimeterLoc = tmp.indexOf(":");
            if(delimeterLoc < 0) {
               throw new IllegalStateException("유효하지 않은 Http Header 형식");
            }
            String key = tmp.substring(0, delimeterLoc).trim();
            String value = tmp.substring(delimeterLoc + 1).trim();
            headers.put(key, value);
        }
        request.setHttpHeaders(headers);
    }

    private void requestLineParse(Request request, String requestLine) {
        String[] requestLineSplit = requestLine.split("\\s");
        request.setMethod(requestLineSplit[0]);
        pathParse(request, requestLineSplit[1]);
        request.setHttpVersion(requestLineSplit[2]);
    }

    private void pathParse(Request request, String path) {
        // api path 파싱
        String[] pathSplit = path.split("\\?");
        String apiPath = pathSplit[0];
        request.setPath(apiPath);

        if(pathSplit.length == 1) return;

        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        String rawQueryParameter = pathSplit[1];
        String[] rawQueryParameters = rawQueryParameter.split("\\&");
        for(String queryParameter: rawQueryParameters) {
            String[] queryKeyValue = queryParameter.split("\\=");
            queryParameters.put(queryKeyValue[0], queryKeyValue[1]);
        }
        request.setParameters(queryParameters);
    }

}
