package utils;

import enums.HttpHeader;
import enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseHandler.class);
    private final DataOutputStream dos;
    private final Map<HttpHeader, String> responseHeadersMap = new HashMap<>();
    private final List<Cookie> cookieList = new ArrayList<>();
    private byte[] body;
    private Status status;

    public HttpResponseHandler(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
    }

    public HttpResponseHandler setStatus(Status status) {
        this.status = status;
        return this;
    }

    public HttpResponseHandler setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponseHandler addHeader(HttpHeader httpHeader, String value) {
        responseHeadersMap.put(httpHeader, value);
        return this;
    }

    public HttpResponseHandler addCookie(Cookie cookie) {
        cookieList.add(cookie);
        return this;
    }

    public void respond() {
        writeHeaders();
        if (body != null) {
            writeBody();
        }
        flush();
    }

    private void writeHeaders() {
        try {
            dos.writeBytes("HTTP/1.1 " + status.getStatusCode() + " " + status.getStatusMessage() + "\r\n");
            for (Map.Entry<HttpHeader, String> header : responseHeadersMap.entrySet()) {
                dos.writeBytes(header.getKey().getHeaderName() + ": " + header.getValue() + "\r\n");
            }
            for (Cookie cookie : cookieList) {
                dos.writeBytes(HttpHeader.SET_COOKIE.getHeaderName() + ": " + cookie.toString() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody() {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void flush() {
        try {
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public Status getStatus() {
        return status;
    }

    public Map<HttpHeader, String> getResponseHeadersMap() {
        return responseHeadersMap;
    }

    public List<Cookie> getCookieList() {
        return cookieList;
    }
}
