package model;

import util.RequestParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private String httpMethod;
    private String path;
    private String protocolVersion;
    private List<String> headers;

    // 생성자로 사용해도 되는가, 컨버컨버터로 사용해여  하눈거
    public HttpRequest(String httpMethod, String path, String protocolVersion, List<String> headers) {

        this.httpMethod = httpMethod;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
    public String getPath() {
        return path;
    }
    public String getProtocolVersion() {
        return protocolVersion;
    }
    public List<String> getHeaders() {
        return headers;
    }

}
