package webserver;

import auth.Cookie;
import constant.RequestHeader;
import enums.HttpCode;
import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private MimeType contentType;
    private HttpCode httpCode;
    private Integer contentLength;
    private String location;
    private List<Cookie> cookies = new ArrayList<>();

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public void sendRedirect(DataOutputStream dos) {
        responseHeader(dos);
    }

    public void send(DataOutputStream dos, byte[] body) {
       contentLength = body.length;
       responseHeader(dos);
       responseBody(dos, body);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    private void responseHeader(DataOutputStream dos) {
        HttpCode httpCode = this.httpCode == null ? HttpCode.OK : this.httpCode;
        try {
            dos.writeBytes("HTTP/1.1 " + httpCode.getCode() + " " + httpCode.getMessage() + "\r\n");
            if(httpCode.isRedirect()) {
                dos.writeBytes(RequestHeader.LOCATION + ": " + location + "\r\n");
            } else {
                dos.writeBytes(RequestHeader.CONTENT_TYPE + ": " + contentType + "\r\n");
                dos.writeBytes(RequestHeader.CONTENT_LENGTH + ": " + contentLength + "\r\n");
            }
            for(Cookie cookie: cookies) {
                dos.writeBytes(Cookie.SET_COOKIE + ": " +
                        cookie.getKey() + "=" + cookie.getValue() + "; " +
                        Cookie.MAX_AGE + "=" + cookie.getMaxAge() + "; " +
                        Cookie.PATH + "=" + cookie.getPath() + "\r\n"
                );
            }
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
