package request;

import http.HttpMethod;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

import static handler.Router.requestMapping;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public void ParsingRequest(InputStream in, OutputStream out) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String firstLine = bufferedReader.readLine();
        String[] request = firstLine.split(" ");

        HttpMethod method = HttpMethod.getMethod(request[0]);

        String requestUrl = request[1];
        HashMap<String, String> header = new HashMap<>();

        StringBuilder log = new StringBuilder().append("\n\n***** REQUEST *****\n").append(firstLine + "\n");

        String headerLine = bufferedReader.readLine();
        while (headerLine != null && !headerLine.isEmpty()) {
            log.append(headerLine + "\n");

            String[] headerParts = headerLine.split(":");

            String key = headerParts[0].strip();
            String value = headerParts[1].strip();
            header.put(key, value);

            headerLine = bufferedReader.readLine();
        }
        logger.debug(log.toString());

        HttpRequest httpRequest = new HttpRequest(method, requestUrl, header);
        requestMapping(httpRequest, out);
    }
}
