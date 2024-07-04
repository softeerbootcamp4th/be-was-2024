package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Request{
    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> httpHeaders;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Request(BufferedReader br) throws IOException {
        requestLineParse(br.readLine());
        httpHeaders = new ConcurrentHashMap<>();
        List<String> headerLines = new ArrayList<>();
        String tmp;
        while((tmp = br.readLine()) != null && !tmp.isEmpty()) {
            headerLines.add(tmp);
        }
        requestHeaderParse(headerLines);
    }

    public void requestHeaderParse(List<String> headerLines) throws IOException {
        for(String headerLine: headerLines) {
            logger.debug("header " + headerLine);
            String[] headerkv = headerLine.split(": ");
            httpHeaders.put(headerkv[0], headerkv[1]);
        }
    }

    public void requestLineParse(String requestLine) throws IOException {
        logger.debug("requestLine " + requestLine);
        String[] s = requestLine.split(" ");
        this.method = s[0];
        this.path = s[1];
        this.httpVersion = s[2];
    }

    public String getHeader(String HeaderName) {
        return httpHeaders.get(HeaderName);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
