package request;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
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
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder log = new StringBuilder().append("\n\n****** REQUEST ******\n");

        HttpRequest request = new HttpRequest();

        String startLine = bufferedReader.readLine();
        setStartLine(startLine, request, log);
        setHeaders(bufferedReader, request, log);
        setBody(bufferedReader, request, log);

        logger.debug(log.toString());

        return requestMapping(request);
    }

    private void setStartLine(String startLine, HttpRequest request, StringBuilder log) {

        log.append(startLine).append("\n");
        String[] splitStartLine = startLine.split(REG_SPC, 3);

        HttpMethod method = HttpMethod.getMethod(splitStartLine[0]);
        String requestUrl = splitStartLine[1];
        String version = splitStartLine[2];

        request.setStartLine(method, requestUrl, version);
    }

    private void setHeaders(BufferedReader bufferedReader, HttpRequest request, StringBuilder log) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String headerLine;

        while (!(headerLine = bufferedReader.readLine()).isEmpty()) {
            log.append(headerLine).append("\n");

            String[] headerParts = headerLine.split(REG_CLN, 2);

            String key = headerParts[0].strip();
            String value = headerParts[1].strip();

            headers.put(key, value);
        }
        log.append("\n");
        request.setHeaders(headers);
    }

    private void setBody(BufferedReader bufferedReader, HttpRequest request, StringBuilder log) throws IOException {
        String contentLengthValue = request.getHeaders(CONTENT_LENGTH);
        if (contentLengthValue == null) return;

        int contentLength = Integer.parseInt(contentLengthValue);
        byte[] body = new byte[contentLength];

        for (int i = 0; i < contentLength; i++) {
            byte read = (byte) bufferedReader.read();
            body[i] = read;
            log.append((char) read);
        }

        log.append("\n");
        request.setBody(body);
    }
}
