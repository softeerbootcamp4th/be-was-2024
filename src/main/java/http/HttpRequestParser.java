package http;

import exception.InvalidHttpRequestException;
import exception.UnsupportedHttpVersionException;

import java.io.*;

import static util.StringUtil.*;
import static util.StringUtil.Method.*;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in)
            throws InvalidHttpRequestException, UnsupportedHttpVersionException, IOException {

        HttpRequest request = new HttpRequest();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String line = bufferedReader.readLine();
        if (line == null || line.isEmpty()){
            throw new InvalidHttpRequestException("Empty request line.");
        }

        String[] startLine = line.split(SPACES);

        throwIfInvalid(startLine);
        setStartLine(request, startLine);

        readHeaders(bufferedReader, request);
        readBody(bufferedReader, request);

        return request;
    }

    private static void readBody(BufferedReader bufferedReader, HttpRequest request) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while (bufferedReader.ready()) {
            buffer.write(bufferedReader.read());
        }
        request.setBody(buffer.toByteArray());
    }

    private static void readHeaders(BufferedReader bufferedReader, HttpRequest request) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) { //null check only는 broken pipe 에러를 발생시킨다..?
            int colonIndex = line.indexOf(COLON);
            if (colonIndex == -1) {
                throw new InvalidHttpRequestException("Invalid HTTP header: " + line);
            }
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            request.addHeader(key, value);
        }
    }

    private static void setStartLine(HttpRequest request, String[] startLine) throws InvalidHttpRequestException {
        request.setMethod(startLine[0]);
        request.setUrl(startLine[1]);
        request.setHttpVersion(startLine[2]);
    }

    private static void throwIfInvalid(String[] startLine) throws InvalidHttpRequestException {
        if(startLine.length != 3){
            throw new InvalidHttpRequestException("Invalid HTTP request line:");
        }
        String method = startLine[0];
        String httpVersion = startLine[2];
        if(!isValidMethod(method)){
            throw new InvalidHttpRequestException("Invalid HTTP method: " + method);
        }
        if(!isValidHttpVersion(httpVersion)){
            throw new InvalidHttpRequestException("Invalid HTTP version: " + httpVersion);
        }
        if(!isSupportedHttpVersion(httpVersion)){
            throw new UnsupportedHttpVersionException("Unsupported HTTP version: " + httpVersion);
        }
    }

    private static boolean isValidMethod(String method) {
        return switch (method) {
            case GET, POST, PUT, PATCH, DELETE -> true;
            default -> false;
        };
    }

    private static boolean isValidHttpVersion(String httpVersion) {
        return httpVersion.startsWith("HTTP/");
    }

    private static boolean isSupportedHttpVersion(String httpVersion) {
        return httpVersion.equals(SUPPORTED_HTTP_VERSION);
    }



}
