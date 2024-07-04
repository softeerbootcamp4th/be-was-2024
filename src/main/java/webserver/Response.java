package webserver;

import enums.HttpCode;
import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private MimeType contentType;
    private HttpCode httpCode;
    private Integer contentLength;

    public void sendRedirect(DataOutputStream dos, String location) {
        HttpCode httpCode = this.httpCode == null ? HttpCode.OK : this.httpCode;
        try {
            dos.writeBytes("HTTP/1.1 " + httpCode.getCode() + " " + httpCode.getMessage() + "\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void send(DataOutputStream dos, byte[] body) {
       contentLength = body.length;
       responseHeader(dos);
       responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos) {
        HttpCode httpCode = this.httpCode == null ? HttpCode.OK : this.httpCode;
        try {
            dos.writeBytes("HTTP/1.1 " + httpCode.getCode() + " " + httpCode.getMessage() + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public MimeType getContentType() {
        return contentType;
    }

    public void setContentType(MimeType contentType) {
        this.contentType = contentType;
    }

    public HttpCode getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(HttpCode httpCode) {
        this.httpCode = httpCode;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }
}
