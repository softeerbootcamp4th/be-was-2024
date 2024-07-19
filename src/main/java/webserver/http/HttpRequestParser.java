package webserver.http;

import db.SessionTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpMethod;
import webserver.http.multipart.ContentDisposition;
import webserver.http.multipart.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);
    private static final HttpRequestParser instance = new HttpRequestParser();

    public static final String CONTENT_LENGTH_HEADER = "content-length";
    public static final String COOKIE_HEADER = "cookie";
    public static final String SESSION_ID = "sId";
    public static final String HTTP_VERSION = "HTTP/1.1";

    private HttpRequestParser() {
    }

    public static HttpRequestParser getInstance() {
        return instance;
    }

    public MyHttpRequest parseRequest(InputStream in) {
        ByteArrayOutputStream requestLineBuffer = new ByteArrayOutputStream();
        int nextByte;

        try {
            // Read the request line
            while ((nextByte = in.read()) != -1) {
                requestLineBuffer.write(nextByte);
                // 개행 문자('\r', '\n')가 나타나면 request line을 완료함
                if (nextByte == '\n') {
                    break;
                }
            }

            // request line UTF-8 문자열로 변환하여 처리
            String requestLine = requestLineBuffer.toString().trim();
            String[] requestLineParts = parseRequestFirstLine(requestLine);
            HttpMethod method = HttpMethod.of(requestLineParts[0].toUpperCase());

            String[] url = requestLineParts[1].split("\\?");
            String path = url[0];
            Map<String, String> queries = new HashMap<>();
            if (url.length > 1) {
                queries = parseQuery(url[1]);
            }

            String version = requestLineParts[2].toUpperCase();
            if (!version.equals(HTTP_VERSION)) {
                throw new IllegalArgumentException("Invalid HTTP version: " + version);
            }

            requestLineBuffer.reset();

            // Read the request headers
            Map<String, String> requestHeaders = new HashMap<>();
            boolean endOfHeaders = false;
            while (!endOfHeaders && (nextByte = in.read()) != -1) {
                requestLineBuffer.write(nextByte);
                // 개행 문자('\r', '\n')가 나타나면 header 처리
                if (nextByte == '\n') {
                    String headerLine = requestLineBuffer.toString().trim();
                    requestLineBuffer.reset();

                    if (headerLine.isEmpty()) {
                        endOfHeaders = true;
                    } else {
                        int index = headerLine.indexOf(':');
                        if (index > 0) {
                            String headerName = headerLine.substring(0, index).trim().toLowerCase();
                            String headerValue = headerLine.substring(index + 1).trim();
                            requestHeaders.put(headerName, headerValue);
                        } else {
                            throw new IllegalArgumentException("Invalid HTTP header: " + headerLine);
                        }
                    }
                }
            }

            // Read the request body
            byte[] requestBody = null;
            if (requestHeaders.containsKey(CONTENT_LENGTH_HEADER)) {
                int contentLength = Integer.parseInt(requestHeaders.get(CONTENT_LENGTH_HEADER));
                requestBody = new byte[contentLength];
                int bytesRead = 0;
                while (bytesRead < contentLength) {
                    int read = in.read(requestBody, bytesRead, contentLength - bytesRead);
                    if (read == -1) {
                        break;
                    }
                    bytesRead += read;
                }
            }


            return new MyHttpRequest(method, path, queries, version, requestHeaders, requestBody);

        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid HTTP request : " + e.getMessage());
        }
    }


    public String[] parseRequestFirstLine(String requestLine) {
        // Split the request line by spaces
        String[] firstLines = requestLine.split("\\s+");

        // The request URI (path) is the second element in the tokens array
        if (firstLines.length == 3) {
            return firstLines;
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }

    public Map<String, String> parseQuery(String query) {
        Map<String, String> queries = new HashMap<>();

        String[] keyValues = query.split("&");
        for (String keyValue : keyValues) {
            String[] keyAndValue = keyValue.split("=");

            if (keyAndValue.length != 2) {
                throw new IllegalArgumentException("Invalid query: " + query);
            }

            String key = URLDecoder.decode(keyAndValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyAndValue[1], StandardCharsets.UTF_8);

            if (key == null || value == null) {
                throw new IllegalArgumentException("Invalid query: " + query);
            }

            queries.put(key, value);
        }

        return queries;
    }

    public Map<String, String> parseCookie(String cookie) {
        Map<String, String> cookies = new HashMap<>();

        String[] keyValues = cookie.split("; ");
        for (String keyValue : keyValues) {
            String[] keyAndValue = keyValue.split("=");

            if (keyAndValue.length != 2) {
                throw new IllegalArgumentException("Invalid cookie: " + cookie);
            }

            String key = keyAndValue[0];
            String value = keyAndValue[1];

            if (key == null || value == null) {
                throw new IllegalArgumentException("Invalid cookie: " + cookie);
            }

            cookies.put(key, value);
        }

        return cookies;
    }

    public boolean isLogin(MyHttpRequest httpRequest) {
        Map<String, String> cookie = parseCookie(httpRequest.getHeaders().get(COOKIE_HEADER));
        return cookie.containsKey(SESSION_ID) && SessionTable.findUserIdBySessionId(UUID.fromString(cookie.get(SESSION_ID))) != null;
    }

    public ArrayList<Part> parseMultipartFormData(byte[] body, String boundary) {
        ArrayList<Part> multiParts = new ArrayList<>();

        String[] parts = new String(body, StandardCharsets.ISO_8859_1).split("--" + boundary);

        if (parts.length > 4) {
            throw new IllegalArgumentException("Invalid multipart/form-data: " + new String(body, StandardCharsets.ISO_8859_1));
        }

        for (int i = 1; i < parts.length - 1; i++) {
            int headersAndBodyIndex = parts[i].indexOf("\r\n\r\n");
            if (headersAndBodyIndex == -1) {
                throw new IllegalArgumentException("Invalid multipart/form-data: " + parts[i]);
            }

            String headers = parts[i].substring(0, headersAndBodyIndex);
            String bodyPart = trimCRLF(parts[i].substring(headersAndBodyIndex + 4));

            String[] headerLines = trimCRLF(headers).split("\r\n");
            Map<String, String> headerMap = new HashMap<>();
            ContentDisposition disposition = new ContentDisposition();

            for (String headerLine : headerLines) {
                String[] header = headerLine.split(":");
                if (header.length != 2) {
                    throw new IllegalArgumentException("Invalid header: " + headerLine);
                }

                if (header[0].trim().toLowerCase().equals("content-disposition")) {
                    disposition = ContentDisposition.parse(header[1]);
                } else {
                    headerMap.put(header[0].trim(), header[1].trim());
                }

            }

            multiParts.add(new Part(disposition, disposition.getFilename(), headerMap, bodyPart.getBytes(StandardCharsets.ISO_8859_1)));
        }

        return multiParts;
    }

    private String trimCRLF(String str) {
        if (str.startsWith("\r\n")) {
            str = str.substring(2);
        }
        if (str.endsWith("\r\n")) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}
