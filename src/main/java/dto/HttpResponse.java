package dto;

import dto.enums.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    public String getProtocolVersion() {
        return protocolVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    private String protocolVersion;
    private HttpStatus httpStatus;
    private Map<String,String> headers = new HashMap<>();
    private byte[] body;

    private HttpResponse(String protocolVersion, HttpStatus httpStatus, Map<String,String> headers, byte[] body) {
        this.protocolVersion = protocolVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse of(String protocolVersion, HttpStatus httpStatus, Map<String,String> headers, byte[] body) {
        return new HttpResponse(protocolVersion, httpStatus, headers, body);
    }








}
