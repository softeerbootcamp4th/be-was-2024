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
    private Map<String, String> parameters;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String body;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Request() {}

    public String getHeader(String HeaderKey) {
        return httpHeaders.get(HeaderKey);
    }

    public String getParameter(String parameterKey) {
        return parameters.get(parameterKey);
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

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
