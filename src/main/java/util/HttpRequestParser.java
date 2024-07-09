package util;

import constant.HttpMethod;
import dto.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    private HttpRequestParser() {}


    public static HttpRequest parseHeader(BufferedReader br) throws IOException {
        HttpRequest request = new HttpRequest();
        StringBuilder headers = new StringBuilder();
        String requestLine = br.readLine();
        headers.append(requestLine);

        String[] requestLineParts = requestLine.split(" ");
        request.setHttpMethod(requestLineParts[0]);
        String uri = requestLineParts[1];

        parseUri(uri, request);

        String line;
        // request header 출력
        while(!(line = br.readLine()).isEmpty()) {
            headers.append(line).append("\n");

            // header line 파싱 및 key, value 형태로 헤더 정보 저장
            String[] headerParts = line.split(":", 2);
            request.setHeader(headerParts[0].trim(), headerParts[1].trim());
        }
        logger.debug("Request Headers:\n {}", headers);

        return request;
    }

    private static void parseUri(String uri, HttpRequest request) {
        String[] uriParts = uri.split("\\?");
        if(uriParts.length == 1){
            String path = uriParts[0];
            request.setPath(path);

            String[] pathParts = path.split("\\.");
            if(pathParts.length == 2){
                request.setExtensionType(pathParts[1]);
            }
        }
        else if(uriParts.length == 2){
            request.setPath(uriParts[0]);

            parseQueryParams(uriParts[1], request);
        }
    }

    private static void parseQueryParams(String queryString, HttpRequest request) {
        String[] queryParts = queryString.split("&");
        Map<String, String> queryParams  = new HashMap<>();

        for (String queryParam : queryParts) {
            String[] queryParamParts = queryParam.split("=");
            String key = URLDecoder.decode(queryParamParts[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(queryParamParts[1], StandardCharsets.UTF_8);
            queryParams.put(key, value);
        }

        request.setQueryParams(queryParams);
    }


}
