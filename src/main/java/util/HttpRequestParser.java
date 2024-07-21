package util;

import constant.FileExtensionType;
import constant.HttpResponseAttribute;
import dto.HttpRequest;
import dto.multipart.MultiPartData;
import dto.multipart.MultiPartDataOfFile;
import dto.multipart.MultiPartDataOfText;
import exception.InvalidHttpRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    private HttpRequestParser() {}


    public static HttpRequest parseHttpRequest(BufferedReader br) throws IOException {
        HttpRequest request = new HttpRequest();
        StringBuilder headers = new StringBuilder();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        // HTTP 요청의 첫 번째 라인 읽기
        int byteRead;
        while ((byteRead = bis.read()) != -1) {
            buffer.write(byteRead);
            if (byteRead == '\r') {
                byteRead = bis.read();
                if (byteRead == '\n') {
                    break;
                } else {
                    buffer.write(byteRead);
                }
            }
        }

        // 요청 라인 파싱
        String requestLine = buffer.toString(StandardCharsets.UTF_8).trim();
        headers.append(requestLine).append("\n");
        String[] requestLineParts = requestLine.split(" ");
        request.setHttpMethod(requestLineParts[0]);
        String uri = requestLineParts[1];

        //uri 파싱
        parseUri(uri, request);


        boolean isHeaderLineEnd = false;
        // 남은 헤더 읽기
        while(!isHeaderLineEnd) {
            buffer.reset();
            while ((byteRead = bis.read()) != -1) {
                buffer.write(byteRead);
                if (byteRead == '\r') {
                    byteRead = bis.read();
                    if (byteRead == '\n') {
                        break;
                    } else {
                        buffer.write(byteRead);
                    }
                }
            }

            if(buffer.toString().trim().isEmpty()) {
                isHeaderLineEnd = true;
                continue;
            }

            // 헤더 라인 파싱
            String headerLine = buffer.toString(StandardCharsets.UTF_8).trim();
            headers.append(headerLine).append("\n");

            // header line 파싱 및 key, value 형태로 헤더 정보 저장
            String[] headerParts = headerLine.split(":", 2);

            // header value의 구분자로 분리
            String[] headerValues = headerParts[1].split("[,;]");
            for(String headerValue : headerValues) {
                headerValue = URLDecoder.decode(headerValue, StandardCharsets.UTF_8);
                request.setHeader(headerParts[0].trim(), headerValue.trim());
            }

        }

        logger.debug("Request Headers:\n {}", headers);

        //request body 읽기 및 HttpRequest에 저장
        if(request.getHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue()).isPresent()) {
            int contentLength = Integer.parseInt(request.getHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue()).get().get(0));
            byte[] body = new byte[contentLength];
            if(bis.read(body, 0, contentLength) != contentLength) {
                logger.error("Invalid request body");
                throw new InvalidHttpRequestException("Invalid request body");
            }

            request.setBody(body);
        }

        return request;
    }

    private static void parseUri(String uri, HttpRequest request) {
        String[] uriParts = uri.split("\\?");
        if(uriParts.length == 1){
            String path = URLDecoder.decode(uriParts[0], StandardCharsets.UTF_8);

            Pattern pattern = Pattern.compile("/\\{(\\d+)\\}");
            Matcher matcher = pattern.matcher(path);
            StringBuilder result = new StringBuilder();
            // 패턴과 일치하는 부분을 찾아서 처리
            if (matcher.find()) {
                String number = matcher.group(1); // 숫자 부분 추출
                request.setPathVariable(Integer.valueOf(number));
                // {} 부분을 삭제하고 결과 문자열에 추가
                matcher.appendReplacement(result, "");
            }
            matcher.appendTail(result); // 나머지 문자열 추가
            path=result.toString();
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

        Map<String, MultiPartData> params = new HashMap<>();

        for (String bodyPart : bodyParts) {
            Map<String, String> contentDispositionParams = new HashMap<>();
            for(String contentHeaderParts : bodyPart.split("\r\n")) {

                // Content-Disposition 헤더의 값 추출
                if (contentHeaderParts.contains("Content-Disposition")) {
                    String[] contentDispositionParts = contentHeaderParts.split("[:;]");

                    for (String contentDispositionPart : contentDispositionParts) {
                        if (contentDispositionPart.contains("=")) {
                            String[] filenameParts = contentDispositionPart.split("=");
                            contentDispositionParams.put(filenameParts[0].trim()
                                    , URLDecoder.decode(filenameParts[1].trim().replace("\"", ""), StandardCharsets.UTF_8));
                        }
                    }
                    // Content-Type 헤더의 값 추출
                } else if (contentHeaderParts.contains("Content-Type")) {
                    String[] contentTypeParts = contentHeaderParts.split(":");
                    contentDispositionParams.put("Content-Type"
                            , URLDecoder.decode(contentTypeParts[1].trim(), StandardCharsets.UTF_8));
                }
            }
            // boundary로 파싱 시, 데이터가 없으면 continue
            if(bodyPart.isEmpty())
                continue;

            // multipart 데이터 추출
            String[] contentParts = bodyPart.split("\r\n\r\n");
            String content;
            if(contentParts.length == 2){
                content = contentParts[1];

                if (content.endsWith("\r\n")) {
                    content = content.substring(0, content.length() - 2);
                }
            }
            else
                continue;

            // filename이 있을 시, MultiPartDataOfFile 클래스에 데이터 저장
            if(contentDispositionParams.containsKey("filename")) {
                String name = contentDispositionParams.get("name");
                String filename = contentDispositionParams.get("filename");
                FileExtensionType contentType = FileExtensionType.of(contentDispositionParams.get("Content-Type"));

                MultiPartDataOfFile multiPartDataOfFile = new MultiPartDataOfFile(
                        filename, contentType, content.getBytes(StandardCharsets.ISO_8859_1)
                );
                params.put(name, multiPartDataOfFile);
            }
            // filename이 없을 시, MultiPartDataOfText 클래스에 데이터 저장
            else{
                String name = contentDispositionParams.get("name");
                FileExtensionType contentType;

                if(!contentDispositionParams.containsKey("Content-Type"))
                    contentType = FileExtensionType.PLAIN;
                else
                    contentType = FileExtensionType.of(contentDispositionParams.get("Content-Type"));

                MultiPartDataOfText multiPartDataOfText = new MultiPartDataOfText(
                        contentType, new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
                );
                params.put(name, multiPartDataOfText);
            }
        }

        return params;
    }

    private static String extractBoundary(HttpRequest httpRequest) {
        List<String> contentTypeValues = httpRequest.getHeader("Content-Type").orElseThrow(
                () -> new InvalidHttpRequestException("Content-Type is empty")
        );
        for (String contentTypeValue : contentTypeValues) {
            if(contentTypeValue.contains("boundary")){
                String[] boundaryParts = contentTypeValue.split("=");
                return boundaryParts[boundaryParts.length-1].trim();
            }
        }
        throw new InvalidHttpRequestException("Boundary not found in multipart/form-data");
    }

}
