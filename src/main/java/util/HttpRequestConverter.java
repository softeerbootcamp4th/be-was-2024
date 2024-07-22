package util;

import dto.HttpRequest;
import dto.enums.HttpMethod;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static util.constant.StringConstants.*;

public class HttpRequestConverter {

    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public HttpRequest with(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, CHARSET));

        /* httpRequest start-line */
        String httpRequestLine = buffer.readLine();
        if (httpRequestLine == null) {
            throw new IOException("Invalid HTTP request: no request line found");
        }
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequestLine.split(SPACE)[0]);
        String path = httpRequestLine.split(SPACE)[1];
        Map<String, String> queryParams = new HashMap<>();
        if (path.contains(QUESTION_MARK)) {
            String[] queryPairs = path.substring(path.indexOf(QUESTION_MARK) + QUESTION_MARK.length()).split(AMPERSAND);
            path = path.substring(0, path.indexOf(QUESTION_MARK)); // string 재할당이 무슨 문제가 있는가
            for (String queryPair : queryPairs) {
                String queryKey = queryPair.substring(0, queryPair.indexOf(EQUAL));
                String queryValue = queryPair.substring(queryPair.indexOf(EQUAL) + 1, queryPair.length());
                queryParams.put(queryKey, queryValue);
            }
        }
        String protocolVersion = httpRequestLine.split(SPACE)[2];

        /* httpRequest headers */
        Map<String, String> headers = new HashMap<>();
        httpRequestLine = buffer.readLine();
        while (httpRequestLine != null && !httpRequestLine.isEmpty()) {
            String[] headerTokens = httpRequestLine.split(COLON, 2);
            String headerName = headerTokens[0].trim();
            String headerValue = headerTokens[1].trim();
            headers.put(headerName, headerValue);
            httpRequestLine = buffer.readLine();
        }

        /* httpRequest body */
        List<Byte> body = new ArrayList<Byte>();
        while (buffer.ready()) {
            byte readByte = (byte) buffer.read();
            body.add(readByte);
        }

        // List<Byte>를 byte[]로 변환
        byte[] bodyBytes = new byte[body.size()];
        for (int i = 0; i < body.size(); i++) {
            bodyBytes[i] = body.get(i);
        }

        // 멀티파트 데이터 처리
        if (headers.containsKey("Content-Type") && headers.get("Content-Type").startsWith("multipart/form-data")) {
            String boundary = "--" + headers.get("Content-Type").split("boundary=")[1];
            return handleMultipartData(httpMethod, path, queryParams, protocolVersion, headers, bodyBytes, boundary);
        } else {
            return HttpRequest.of(httpMethod, path, queryParams, protocolVersion, headers, bodyBytes);
        }
    }

    private HttpRequest handleMultipartData(HttpMethod method, String path, Map<String, String> queryParams, String protocolVersion, Map<String, String> headers, byte[] bodyBytes, String boundary) throws IOException {
        Map<String, byte[]> fileMap = new HashMap<>();
        Map<String, String> formFields = new HashMap<>();

        int pos = 0;
        while (pos < bodyBytes.length) {
            int boundaryPos = indexOf(bodyBytes, boundary.getBytes(), pos);
            if (boundaryPos < 0) break;
            pos = boundaryPos + boundary.length();

            int nextBoundaryPos = indexOf(bodyBytes, boundary.getBytes(), pos);
            if (nextBoundaryPos < 0) nextBoundaryPos = bodyBytes.length;

            byte[] partBytes = Arrays.copyOfRange(bodyBytes, pos, nextBoundaryPos);
            pos = nextBoundaryPos;

            BufferedReader partReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(partBytes), CHARSET));
            String disposition = partReader.readLine();
            if (disposition == null || !disposition.startsWith("Content-Disposition")) continue;

            String contentType = partReader.readLine();
            if (contentType == null) contentType = "";

            // 빈 줄까지 읽어들이기
            while (partReader.ready()) {
                String line = partReader.readLine();
                if (line.isEmpty()) break;
            }

            if (disposition.contains("filename=")) {
                String name = getPartHeaderValue(disposition, "name");
                ByteArrayOutputStream fileContent = new ByteArrayOutputStream();
                int read;
                while ((read = partReader.read()) != -1) {
                    fileContent.write(read);
                }
                fileMap.put(name, fileContent.toByteArray());
            } else {
                String name = getPartHeaderValue(disposition, "name");
                StringBuilder fieldContent = new StringBuilder();
                String line;
                while ((line = partReader.readLine()) != null) {
                    fieldContent.append(line).append("\r\n");
                }
                formFields.put(name, fieldContent.toString().trim());
            }
        }

        return HttpRequest.of(method, path, queryParams, protocolVersion, headers, formFields, fileMap);
    }

    private int indexOf(byte[] array, byte[] target, int start) {
        for (int i = start; i <= array.length - target.length; i++) {
            boolean found = true;
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    private String getPartHeaderValue(String partHeader, String key) {
        int keyStart = partHeader.indexOf(key + "=\"");
        if (keyStart == -1) {
            return null; // 키가 없을 경우 null 반환
        }
        int start = keyStart + key.length() + 2;
        int end = partHeader.indexOf("\"", start);
        if (end == -1) {
            return null; // 종료 따옴표가 없을 경우 null 반환
        }
        return partHeader.substring(start, end);
    }

    public static Map<String, String> bodyToMap(byte[] body) throws UnsupportedEncodingException {
        String bodyToString = URLDecoder.decode(new String(body, StandardCharsets.UTF_8), UTF_8);
        String[] bodyParsedPairList = bodyToString.split(AMPERSAND);
        Map<String, String> parsingBodyParams = new HashMap<>();
        for (String bodyParsedPair : bodyParsedPairList) {
            String queryKey = bodyParsedPair.substring(0, bodyParsedPair.indexOf(EQUAL));
            String queryValue = bodyParsedPair.substring(bodyParsedPair.indexOf(EQUAL) + 1);
            parsingBodyParams.put(queryKey, queryValue);
        }
        return parsingBodyParams;
    }
}
