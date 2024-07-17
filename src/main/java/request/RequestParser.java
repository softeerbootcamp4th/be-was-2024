package request;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.StartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

import static handler.Router.requestMapping;
import static util.Constants.*;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public HttpResponse ParsingRequest(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        StringBuilder log = new StringBuilder().append("\n\n****** REQUEST ******\n");

        StartLine startLine = getStartLine(in, log);
        HashMap<String, String> headers = getHeaders(in, log);
        byte[] body = getBody(in, log, headers.get(CONTENT_LENGTH));

        logger.debug(log.toString());

        HttpRequest request = new HttpRequest()
                .setStartLine(startLine)
                .setHeaders(headers)
                .setBody(body);

        return requestMapping(request);
    }

    private StartLine getStartLine(InputStream in, StringBuilder log) {
        String startLine = byteReader(in, 2);

        log.append(startLine).append("\n");
        String[] splitStartLine = startLine.split(REG_SPC, 3);

        HttpMethod method = HttpMethod.getMethod(splitStartLine[0]);
        String requestUrl = splitStartLine[1];
        String version = splitStartLine[2];

        return new StartLine(method, requestUrl, version);
    }

    private HashMap<String, String> getHeaders(InputStream in, StringBuilder log) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String headerLine;

        while (!(headerLine = byteReader(in, 2)).isEmpty()) {
            log.append(headerLine).append("\n");

            String[] headerParts = headerLine.split(REG_CLN, 2);

            String key = headerParts[0].strip();
            String value = headerParts[1].strip();

            headers.put(key, value);
        }


        return headers;
    }

    private byte[] getBody(InputStream in, StringBuilder log, String contentLengthValue) throws IOException {
        if (contentLengthValue == null) return new byte[0];

        int contentLength = Integer.parseInt(contentLengthValue);
        byte[] body = new byte[contentLength];

        for (int i = 0; i < contentLength; i++) {
            byte read = (byte) in.read();
            body[i] = read;
            log.append((char) read);
        }
        return body;
    }

    private String byteReader(InputStream in, int CRLF) {
        StringBuilder line = new StringBuilder();

        int character;
        int flag = 0;
        try {
            while (flag < CRLF && (character = in.read()) != -1) {
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
