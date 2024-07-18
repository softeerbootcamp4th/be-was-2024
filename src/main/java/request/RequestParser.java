package request;

import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static handler.Router.requestMapping;
import static util.Constants.*;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public HttpResponse ParsingRequest(InputStream in) throws IOException {
        StringBuilder log = new StringBuilder().append("\n\n****** REQUEST ******\n");

        StartLine startLine = getStartLine(in, log);
        HashMap<String, String> headers = getHeaders(in, log);
        ArrayList<RequestBody> body = getBody(in, headers);

        logger.debug(log.toString());

        HttpRequest request = new HttpRequest()
                .setStartLine(startLine)
                .setHeaders(headers)
                .setBody(body);

        return requestMapping(request);
    }

    public StartLine getStartLine(InputStream in, StringBuilder log) {
        String startLine = byteReader(in);

        log.append(startLine).append("\n");
        String[] splitStartLine = startLine.split(REG_SPC);

        HttpMethod method = HttpMethod.getMethod(splitStartLine[0].trim().toUpperCase());
        String requestUrl = splitStartLine[1].trim();
        String version = splitStartLine[2].trim();

        return new StartLine(method, requestUrl, version);
    }

    public HashMap<String, String> getHeaders(InputStream in, StringBuilder log) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String headerLine;

        while (!(headerLine = byteReader(in)).isEmpty()) {
            log.append(headerLine).append("\n");

            String[] headerParts = headerLine.split(REG_CLN, 2);

            String key = headerParts[0].strip().toLowerCase();
            String value = headerParts[1].strip();

            headers.put(key, value);
        }
        return headers;
    }

    public ArrayList<RequestBody> getBody(InputStream in, HashMap<String, String> headers) throws IOException {
        String contentLengthValue = headers.get("content-length");

        ArrayList<RequestBody> bodies = new ArrayList<>();
        if (contentLengthValue == null) {
            bodies.add(new RequestBody(new byte[0]));
            return bodies;
        }

        String boundary = Utils.getBoundary(headers.get("content-type"));
        int contentLength = Integer.parseInt(contentLengthValue);

        if (boundary == null) {
            bodies.add(getSingleBody(in, contentLength));
            return bodies;
        } else return getMultipartBody(in, contentLength, boundary);

    }

    private RequestBody getSingleBody(InputStream in, int contentLength) throws IOException {
        byte[] body = new byte[contentLength];

        for (int i = 0; i < contentLength; i++) {
            byte read = (byte) in.read();
            body[i] = read;
        }
        return new RequestBody(body);
    }

    private ArrayList<RequestBody> getMultipartBody(InputStream in, int contentLength, String boundary) throws IOException {
        ArrayList<RequestBody> multipartBodies = new ArrayList<>();

        byte[] bodies = new byte[contentLength];
        for (int i = 0; i < contentLength; i++) {
            bodies[i] = (byte) in.read();
        }

        ArrayList<byte[]> splittedMultipartBodies = splitByBoundary(bodies, boundary);
        for (byte[] multipartBody : splittedMultipartBodies) {
            multipartBodies.add(parseMultipartBody(multipartBody));
        }

        return multipartBodies;
    }

    private RequestBody parseMultipartBody(byte[] multipartBody) {
        HashMap<String, String> headers;
        byte[] body;
        int headerEndIndex = 0;

        for (int i = 0; i < multipartBody.length - 3; i++) {
            if (multipartBody[i] == '\r' && multipartBody[i + 1] == '\n' && multipartBody[i + 2] == '\r' && multipartBody[i + 3] == '\n') {
                headerEndIndex = i + 4;
                break;
            }
        }

        byte[] headerBytes = new byte[headerEndIndex];
        System.arraycopy(multipartBody, 0, headerBytes, 0, headerEndIndex);
        body = new byte[multipartBody.length - headerEndIndex];
        System.arraycopy(multipartBody, headerEndIndex, body, 0, body.length);

        headers = parseHeaders(headerBytes);

        return new RequestMultipartBody(headers, body);
    }

    private HashMap<String, String> parseHeaders(byte[] headerBytes) {
        HashMap<String, String> headers = new HashMap<>();
        String headerString = new String(headerBytes);
        String[] headerLines = headerString.split("\r\n");
        for (String line : headerLines) {
            int index = line.indexOf(":");
            if (index != -1) {
                String key = line.substring(0, index).trim().toLowerCase();
                String value = line.substring(index + 1).trim();
                headers.put(key, value);
            }
        }
        return headers;
    }

    private ArrayList<byte[]> splitByBoundary(byte[] bodies, String boundary) throws IOException {
        ArrayList<byte[]> parts = new ArrayList<>();
        byte[] boundaryBytes = boundary.getBytes();
        byte[] endBoundaryBytes = (boundary + "--").getBytes();

        int start = 0;
        while (start < bodies.length) {

            int boundaryIndex = indexOf(bodies, boundaryBytes, start);
            if (boundaryIndex == -1) {
                break;
            }

            if (Arrays.equals(Arrays.copyOfRange(bodies, boundaryIndex, boundaryIndex + endBoundaryBytes.length), endBoundaryBytes)) {
                break;
            }

            int nextBoundaryIndex = indexOf(bodies, boundaryBytes, boundaryIndex + boundaryBytes.length);
            if (nextBoundaryIndex == -1) {
                nextBoundaryIndex = bodies.length;
            }

            byte[] part = Arrays.copyOfRange(bodies, boundaryIndex + boundaryBytes.length, nextBoundaryIndex);
            parts.add(part);

            start = nextBoundaryIndex;
        }

        return parts;
    }

    private static int indexOf(byte[] data, byte[] subArray, int start) {
        outer:
        for (int i = start; i <= data.length - subArray.length; i++) {
            for (int j = 0; j < subArray.length; j++) {
                if (data[i + j] != subArray[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }


    private String byteReader(InputStream in) {
        StringBuilder line = new StringBuilder();

        int character;
        int flag = 0;
        try {
            while (flag < 2 && (character = in.read()) != -1) {
                if (character == '\r' || character == '\n') flag++;
                else {
                    flag = 0;
                    line.append((char) character);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return line.toString();
    }

}
