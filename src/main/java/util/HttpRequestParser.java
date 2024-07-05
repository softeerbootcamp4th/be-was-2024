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
import java.util.Optional;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    private HttpRequestParser() {}


    public static HttpRequest parseHeader(BufferedReader br) throws IOException {
        HttpRequest request = new HttpRequest();
        StringBuilder headers = new StringBuilder();
        String line = br.readLine();
        headers.append(line);

        String[] tokens = line.split(" ");
        request.setHttpMethod(tokens[0]);
        String requestTarget = tokens[1];

        parseRequestTarget(requestTarget, request);

        // request header 출력
        while(!(line = br.readLine()).isEmpty()) {
            headers.append(line).append("\n");
        }
        logger.debug("Request Headers:\n {}", headers);

        return request;
    }

    private static void parseRequestTarget(String requestTarget, HttpRequest request) {
        String[] tokens = requestTarget.split("\\?");
        if(tokens.length == 1){
            String path = tokens[0];
            request.setPath(path);

            String[] pathTokens = path.split("\\.");
            if(pathTokens.length == 2){
                request.setExtensionType(pathTokens[1]);
            }
        }
        else if(tokens.length == 2){
            request.setPath(tokens[0]);

            parseQueryParams(tokens[1], request);
        }
    }

    private static void parseQueryParams(String queryString, HttpRequest request) {
        String[] pairs = queryString.split("&");
        Map<String, String> queryParams  = new HashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            queryParams.put(key, value);
        }

        request.setQueryParams(queryParams);
    }


}
