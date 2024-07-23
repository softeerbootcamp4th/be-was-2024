package utils;

import enums.HttpHeader;
import enums.Method;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser {
    private final String startLine;
    private final String url;
    private final String path;
    private final byte[] body;
    private final String extension;
    private final Method method;
    private final Map<String, String> cookiesMap = new HashMap<>();
    private final Map<HttpHeader, String> requestHeadersMap = new HashMap<>();
    private final Map<String, String> queryParametersMap = new HashMap<>();
    private final String EXTENSION_REGEX = "\\.([a-z]+)$";

    // 멀티파트 바디 파싱 결과를 저장할 필드
    private String parsedContent;
    private byte[] parsedImage;

    public HttpRequestParser(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
        int c;
        int prev1 = -1, prev2 = -1, prev3 = -1;

        // Read headers
        while ((c = inputStream.read()) != -1) {
            headerBuffer.write(c);
            if (prev3 == '\r' && prev2 == '\n' && prev1 == '\r' && c == '\n') {
                break;
            }
            prev3 = prev2;
            prev2 = prev1;
            prev1 = c;
        }

        String headersString = headerBuffer.toString();
        String[] headerLines = headersString.split("\r\n");

        startLine = headerLines[0];

        for (int i = 1; i < headerLines.length; i++) {
            String line = headerLines[i];
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                requestHeadersMap.put(HttpHeader.fromString(key), value);
            }
        }

        // 쿠키 파싱
        String cookieHeader = requestHeadersMap.get(HttpHeader.COOKIE);
        if (cookieHeader != null) {
            for (String keyValue : cookieHeader.split(";")) {
                int equalsIndex = keyValue.indexOf("=");
                if (equalsIndex != -1) {
                    String key = keyValue.substring(0, equalsIndex).trim();
                    String value = keyValue.substring(equalsIndex + 1).trim();

                    cookiesMap.put(key, value);
                }
            }
        }

        String contentLengthHeader = requestHeadersMap.get(HttpHeader.CONTENT_LENGTH);
        int contentLength = contentLengthHeader == null ? 0 : Integer.parseInt(contentLengthHeader);

        // body 파싱
        if (contentLength > 0) {
            body = new byte[contentLength];
            int bytesRead;
            int offset = 0;

            while (offset < contentLength) {
                bytesRead = inputStream.read(body, offset, contentLength - offset);
                if (bytesRead == -1) {
                    throw new IOException("Failed to read full request body");
                }
                offset += bytesRead;
            }
        } else {
            body = null;
        }

        method = Method.fromString(startLine.split(" ")[0]);
        url = startLine.split(" ")[1];
        String[] tokens = url.split("\\?");

        path = tokens[0];

        // 확장자 파싱
        Pattern compile = Pattern.compile(EXTENSION_REGEX);
        Matcher matcher = compile.matcher(path);

        // 파일 확장자를 가진 path인지 검증
        extension = matcher.find() ? matcher.group(1) : null;

        if (tokens.length > 1) {
            String queryParameters = tokens[1];
            for (String keyValue : queryParameters.split("&")) {
                int equalsIndex = keyValue.indexOf("=");
                if (equalsIndex != -1) {
                    String key = keyValue.substring(0, equalsIndex);
                    String value = keyValue.substring(equalsIndex + 1);
                    queryParametersMap.put(key, value);
                }
            }
        }

        // 멀티파트 바디 파싱 호출
        String contentTypeHeader = requestHeadersMap.get(HttpHeader.CONTENT_TYPE);
        if (contentTypeHeader != null && contentTypeHeader.startsWith("multipart/form-data")) {
            try {
                parseMultipartBody();
            } catch (Exception e) {
                e.printStackTrace(); // 에러 로그 출력
                throw new IOException("Failed to parse multipart body", e);
            }
        }
    }

    public String getStartLine() {
        return startLine;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<HttpHeader, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    public Map<String, String> getQueryParametersMap() {
        return queryParametersMap;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, String> getCookiesMap() {
        return cookiesMap;
    }

    public String getExtension() {
        return extension;
    }

    public String getParsedContent() {
        return parsedContent;
    }

    public byte[] getParsedImage() {
        return parsedImage;
    }

    public String headersToString() {
        StringBuilder sb = new StringBuilder();
        for (HttpHeader httpHeader : requestHeadersMap.keySet()) {
            sb.append(httpHeader).append(": ").append(requestHeadersMap.get(httpHeader)).append("\n");
        }

        return sb.toString();
    }

    // 멀티파트 바디를 파싱하여 파트별로 처리하는 메서드
    private void parseMultipartBody() throws Exception {
        String contentTypeHeader = requestHeadersMap.get(HttpHeader.CONTENT_TYPE);
        String boundary = extractBoundary(contentTypeHeader);
        if (boundary == null) {
            throw new Exception("Boundary not found in Content-Type header");
        }

        byte[] boundaryBytes = ("--" + boundary).getBytes();
        byte[] endBoundaryBytes = ("--" + boundary + "--").getBytes();

        int pos = 0;
        while (pos < body.length) {
            int boundaryIndex = indexOf(body, boundaryBytes, pos);
            if (boundaryIndex == -1) {
                break;
            }
            pos = boundaryIndex + boundaryBytes.length;

            int partEndIndex = indexOf(body, boundaryBytes, pos);
            if (partEndIndex == -1) {
                partEndIndex = indexOf(body, endBoundaryBytes, pos);
                if (partEndIndex == -1) {
                    partEndIndex = body.length;
                }
            }

            byte[] part;
            if (partEndIndex > body.length - 4) { // Check for the last part without \r\n\r\n
                part = Arrays.copyOfRange(body, pos, partEndIndex);
            } else {
                part = Arrays.copyOfRange(body, pos, partEndIndex - 2); // -2 to remove \r\n before boundary
            }

            try {
                parsePart(part);
            } catch (Exception e) {
                e.printStackTrace(); // 에러 로그 출력
                throw new Exception("Failed to parse part", e);
            }
            pos = partEndIndex;
        }
    }

    // 멀티파트 바디의 각 파트를 파싱하여 내용과 이미지를 분리
    private void parsePart(byte[] part) {
        int headerEndIndex = indexOf(part, "\r\n\r\n".getBytes(), 0);
        if (headerEndIndex == -1) {
            headerEndIndex = part.length;
        }

        String headers = new String(part, 0, headerEndIndex);
        byte[] body;

        if (headerEndIndex + 4 > part.length) {
            body = new byte[0]; // 본문이 없는 경우 빈 배열로 설정
        } else {
            body = Arrays.copyOfRange(part, headerEndIndex + 4, part.length);
        }

        if (headers.contains("Content-Disposition: form-data; name=\"content\"")) {
            parsedContent = new String(body);
        } else if (headers.contains("Content-Disposition: form-data; name=\"image\"; filename=\"")) {
            parsedImage = body;
        }
    }

    // 헬퍼 메서드: 바이트 배열에서 특정 바이트 배열을 검색하여 인덱스를 반환
    private int indexOf(byte[] array, byte[] target, int start) {
        outer: for (int i = start; i <= array.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    // 헬퍼 메서드: Content-Type 헤더에서 boundary 추출
    private String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length());
            }
        }
        return null;
    }
}