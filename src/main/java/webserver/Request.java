package webserver;

import model.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CookieUtil;
import utils.RequestLineUtil;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private String method;
    private String path;
    private String queryString;
    private byte[] byteBody;
    private FileData fileData;
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

        if (getBoundaryFromContentType() != null) {
            parseMultipartData(byteBody, getBoundaryFromContentType());
        } else {
            stringBody = new String(byteBody, StandardCharsets.UTF_8);
            stringBody = URLDecoder.decode(stringBody, StandardCharsets.UTF_8);
        }
    }

    private void parseRequestLine(String requestLine) {
        this.method = RequestLineUtil.getHttpMethod(requestLine);
        String url = RequestLineUtil.getUrl(requestLine);

        this.path = RequestLineUtil.getPath(url);
        this.queryString = RequestLineUtil.getQueryString(url);
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

    private String getBoundaryFromContentType() {
        String contentType = headers.get("Content-Type");
        if (contentType != null && contentType.contains("boundary=")) {
            return "--" + contentType.split("boundary=")[1];
        }
        return null;
    }

    private void parseMultipartData(byte[] body, String boundary) {
        String CONTENT_DISPOSITION = "Content-Disposition";

        String bodyStr = new String(body, StandardCharsets.ISO_8859_1);
        String[] splitData = bodyStr.split(boundary);

        StringBuilder stringBodyBuilder = new StringBuilder();

        for (String part : splitData) {
            if (part.trim().isEmpty() || part.equals("--")) continue;

            int headerEndIndex = part.indexOf("\r\n\r\n");
            if (headerEndIndex == -1) continue;

            String header = part.substring(0, headerEndIndex).trim();
            String bodyContent = part.substring(headerEndIndex + 4);

            // 헤더 파싱
            String[] headerLines = header.split("\r\n");
            String partName = null;
            String filename = null;
            for (String headerLine : headerLines) {
                if (headerLine.startsWith(CONTENT_DISPOSITION)) {
                    partName = extractPartName(headerLine);
                    filename = extractFileName(headerLine);
                }
            }

            if (filename != null) {
                // 파일 데이터
                this.fileData = new FileData.Builder()
                        .fileBinaryData(bodyContent.getBytes(StandardCharsets.ISO_8859_1))
                        .fileName(filename)
                        .build();
            } else {
                // 텍스트 데이터
                stringBodyBuilder.append(partName).append("=").append(bodyContent.trim()).append("&");
            }
        }

        this.stringBody = new String(stringBodyBuilder.substring(0, stringBodyBuilder.length() - 1).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    private String extractPartName(String contentDisposition) {
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("name")) {
                return part.substring(part.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private String extractFileName(String contentDisposition) {
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("filename")) {
                return part.substring(part.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
        return RequestLineUtil.getStaticPath(path);
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

    public String getSessionId() {
        String cookie = headers.get("Cookie");
        return CookieUtil.getCookie(cookie);
    }

    public HashMap<String, String> parseQueryString() {
        return createHashMap(splitString(queryString));
    }

    public HashMap<String, String> parseBody() {
        return createHashMap(splitString(stringBody));
    }

    public FileData getFileData() {
        return this.fileData;
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
