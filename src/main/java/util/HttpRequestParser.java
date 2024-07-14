package util;

import constant.MimeType;
import dto.HttpRequest;
import exception.InvalidHttpRequestException;
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
    private static final String CONTENT_LENGTH = "Content-Length";

    private HttpRequestParser() {}


    public static HttpRequest parseHttpRequest(BufferedReader br) throws IOException {
        HttpRequest request = new HttpRequest();
        StringBuilder headers = new StringBuilder();
        String requestLine = br.readLine();
        headers.append(requestLine);

        String[] requestLineParts = requestLine.split(" ");
        request.setHttpMethod(requestLineParts[0]);
        String uri = requestLineParts[1];

        //uri 파싱
        parseUri(uri, request);

        String line;
        // request header 파싱 및 로그를 통해 출력
        while(!(line = br.readLine()).isEmpty()) {
            headers.append(line).append("\n");

            // header line 파싱 및 key, value 형태로 헤더 정보 저장
            String[] headerParts = line.split(":", 2);
            // header value의 구분자로 분리
            String[] headerValues = headerParts[1].split("[,;]");
            for(String headerValue : headerValues) {
                headerValue = URLDecoder.decode(headerValue, StandardCharsets.UTF_8);
                request.setHeader(headerParts[0].trim(), headerValue.trim());
            }
        }
        logger.debug("Request Headers:\n {}", headers);

        //request body 읽기 및 HttpRequest에 저장
        if(request.getHeader(CONTENT_LENGTH).isPresent()) {
            int contentLength = Integer.parseInt(request.getHeader(CONTENT_LENGTH).get().get(0));
            char[] body = new char[contentLength];
            if(br.read(body, 0, body.length)!=contentLength) {
                logger.error("Invalid request body");
                throw new InvalidHttpRequestException("Invalid request body");
            }
            String requestBody = new String(body);
            request.setBody(requestBody);
            logger.debug("Request Body:\n {}", requestBody);
        }

        return request;
    }

    private static void parseUri(String uri, HttpRequest request) {
        String[] uriParts = uri.split("\\?");
        if(uriParts.length == 1){
            String path = uriParts[0];
            request.setPath(path);

            String[] pathParts = path.split("\\.");
            if(pathParts.length >= 2)
                request.setExtensionType(pathParts[pathParts.length-1]);
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

    public static Map<String, String> parseRequestBody(String body, MimeType mimeType){
        switch(mimeType){
            case APPLICATION_FORM_URLENCODED :
                String[] bodyParts = body.split("&");
                Map<String, String> bodyParams = new HashMap<>();
                for (String bodyPart : bodyParts) {
                    String[] formData = bodyPart.split("=");
                    if(formData.length == 2){
                        formData[0] = URLDecoder.decode(formData[0], StandardCharsets.UTF_8);
                        formData[1] = URLDecoder.decode(formData[1], StandardCharsets.UTF_8);
                        bodyParams.put(formData[0], formData[1]);
                    }
                    else if(formData.length == 1){
                        formData[0] = URLDecoder.decode(formData[0], StandardCharsets.UTF_8);
                        bodyParams.put(formData[0], "");
                    }

                }
                return bodyParams;

            // TODO: JSON 데이터 파싱 작업
            case APPLICATON_JSON:
                break;
        }
        return null;
    }

}
