package request;

import http.HttpMethod;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

import static handler.Router.requestMapping;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public void ParsingRequest(InputStream in, OutputStream out) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder log = new StringBuilder().append("\n\n****** REQUEST ******\n");

        HttpRequest request = new HttpRequest();

        setStartLine(bufferedReader, request, log);
        setHeaders(bufferedReader, request, log);
        setBody(bufferedReader, request, log);

        logger.debug(log.toString());

        requestMapping(request, out);
    }

    private void setStartLine(BufferedReader bufferedReader, HttpRequest request, StringBuilder log) throws IOException {
        String startLine = bufferedReader.readLine();
        if(startLine == null) return;
        log.append(startLine).append("\n");

        String[] splitStartLine = startLine.split(" ", 3);

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

            String[] headerParts = headerLine.split(":", 2);

            String key = headerParts[0].strip();
            String value = headerParts[1].strip();

            headers.put(key, value);
        }
        log.append("\n");
        request.setHeaders(headers);
    }

    private void setBody(BufferedReader bufferedReader, HttpRequest request, StringBuilder log) throws IOException {
        Optional<String> optionalContentLength = request.getHeaders("Content-Length");
        if (optionalContentLength.isEmpty()) return;

        int contentLength = Integer.parseInt(optionalContentLength.get());
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
