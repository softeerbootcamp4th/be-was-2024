package utils;

import enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final DataOutputStream dos;
    private final Map<String, String> responseHeadersMap = new HashMap<>();

    private Status status;

    public HttpResponseHandler(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
    }

    public HttpResponseHandler setStatus(Status status) {
        this.status = status;
        return this;
    }

    public HttpResponseHandler addHeader(String key, String value) {
        responseHeadersMap.put(key, value);
        return this;
    }

    public void respond(byte[] body) {
        writeHeaders();
        if (body != null) {
            writeBody(body);
        }
        flush();
    }

    private void writeHeaders() {
        try {
            dos.writeBytes("HTTP/1.1 " + status.getStatusCode() + " " + status.getStatusMessage() + "\r\n");
            for (Map.Entry<String, String> header : responseHeadersMap.entrySet()) {
                dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody(byte[] body) {
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
}
