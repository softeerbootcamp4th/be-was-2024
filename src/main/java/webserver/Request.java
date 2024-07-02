package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Request{
    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> httpHeaders;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Request(BufferedReader br) throws IOException {
        requestLineParse(br);
        httpHeaders = new ConcurrentHashMap<>();
        requestHeaderParse(br);
    }

    private void requestHeaderParse(BufferedReader br) throws IOException {
        String tmp;
        while((tmp = br.readLine())!=null && !tmp.isEmpty()) {
            logger.debug("header " + tmp);
            String[] headerLine = tmp.split(": ");
            httpHeaders.put(headerLine[0], headerLine[1]);
        }
    }

    private void requestLineParse(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
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
