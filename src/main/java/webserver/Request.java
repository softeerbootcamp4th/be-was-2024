package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private String method;
    private String path;
    private String queryString;
    private byte[] byteBody;
    private String stringBody;
    private Map<String, String> headers = new HashMap<>();

    protected Request(InputStream inputStream) throws IOException {
        parseRequestAsByte(inputStream);
    }

    private void parseRequestAsByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
        int prev = -1, curr;

        // 헤더 부분 읽기
        while ((curr = inputStream.read()) != -1) {
            headerBuffer.write(curr);

            // 헤더가 끝났는지 체크
            if (prev == '\r' && curr == '\n') {
                String headers = headerBuffer.toString(StandardCharsets.UTF_8);
                if (headers.endsWith("\r\n\r\n")) {
                    break;
                }
            }

            prev = curr;
        }

        // 헤더버퍼를 스트링으로 변환
        String header = headerBuffer.toString(StandardCharsets.UTF_8);
        String[] splittedHeader = header.split("\r\n");

        // requestLine 파싱하기
        String requestLine = splittedHeader[0];
        parseRequestLine(requestLine);

        //읽어들인 InputStream 모두 출력
        printAllRequestHeader(splittedHeader);

        // header의 내용 hash map에 저장
        makeHeaderHashMap(splittedHeader);

        // Content-Length 값 추출하기
        int contentLength = getContentLength();

        // body 읽어서 byte array로 저장
        this.byteBody = new byte[contentLength];
        int bytesRead = 0;
        while (bytesRead < contentLength) {
            int result = inputStream.read(byteBody, bytesRead, contentLength - bytesRead);
            if (result == -1) break; // 스트림 끝
            bytesRead += result;
        }

        stringBody = new String(byteBody, StandardCharsets.UTF_8);
    }

    private void parseRequestLine(String requestLine) {
        this.method = requestLine.split(" ")[0];
        String url = requestLine.split(" ")[1];
        if (url.contains("?")) {
            this.path = url.split("\\?")[0];
            this.queryString = url.split("\\?")[1];
        } else {
            this.path = url;
            this.queryString = "";
        }
    }

    private void printAllRequestHeader(String[] splittedHeader) {
        //읽어들인 InputStream 모두 출력
        for (int i = 1; i < splittedHeader.length; i++) {
            logger.debug(splittedHeader[i]); // 읽어들인 라인 출력
        }
    }

    private void makeHeaderHashMap(String[] splittedHeader) {
        for (int i = 1; i < splittedHeader.length; i++) {
            String currentLine = splittedHeader[i];

            // header의 내용 headers hash map에 저장
            int colonIndex = currentLine.indexOf(':');
            String key = currentLine.substring(0, colonIndex).trim();
            String value = currentLine.substring(colonIndex + 1).trim();
            this.headers.put(key, value);
        }
    }

    private int getContentLength() {
        int contentLength = 0;
        String contentLengthValue = headers.get("Content-Length");
        if (contentLengthValue != null) {
            contentLength = Integer.parseInt(contentLengthValue);
        }
        return contentLength;
    }

    public static Request from(InputStream inputStream) throws IOException {
        return new Request(inputStream);
    }

    public boolean isQueryString() {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return !queryString.isEmpty();
    }

    private String getStaticPath() {
        File testFile = new File("src/main/resources/static" + path);

        if (testFile.isDirectory()) {
            return path + "/index.html";
        } else {
            return path;
        }
    }

    public String getHttpMethod() {
        return method;
    }

    public String getPath() {
        return getStaticPath();
    }

    public String getQueryString() {
        return this.queryString;
    }

    public HashMap<String, String> parseQueryString() {
        return createHashMap(splitString(queryString));
    }

    public HashMap<String, String> parseBody() {
        return createHashMap(splitString(stringBody));
    }

    private String[] splitString(String dataString) {
        return dataString.split("&");
    }

    private HashMap<String, String> createHashMap(String[] dataString) {
        HashMap<String, String> data = new HashMap<>();
        for (String s : dataString) {
            String[] fieldData = s.split("=");
            data.put(fieldData[0], fieldData[1]);
        }
        return data;
    }

    // Test를 위한 메소드
    public Map<String, String> getHeadersForTest() {
        return this.headers;
    }

    public int getContentLengthForTest() {
        return Integer.parseInt(headers.get("Content-Length"));
    }
}
