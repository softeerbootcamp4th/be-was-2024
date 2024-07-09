package webserver;

import enums.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Request{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private HttpMethod method;
    private String path;
    private String httpVersion;
    private Map<String, String> httpHeaders;
    private Map<String, String> parameters;
    private String body;

    public Request(HttpMethod method,
                   String path,
                   String httpVersion,
                   Map<String, String> httpHeaders,
                   Map<String, String> parameters,
                   String body
                   ) {
       this.method = method;
       this.path = path;
       this.httpVersion = httpVersion;
       this.httpHeaders = httpHeaders;
       this.parameters = parameters;
       this.body = body;
    }

    public String getHeader(String HeaderKey) {
        return httpHeaders.get(HeaderKey);
    }

    public String getParameter(String parameterKey) {
        return parameters.get(parameterKey);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
