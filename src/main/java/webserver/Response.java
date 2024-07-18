package webserver;

import utils.CookieUtil;
import utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private final String url;
    private final int redirectCode;
    private final int statusCode;
    private final DataOutputStream dos;
    private final String cookie;
    private final byte[] body;

    private Response(Builder builder) {
        this.url = builder.url;
        this.redirectCode = builder.redirectCode;
        this.statusCode = builder.statusCode;
        this.dos = builder.dos;
        this.cookie = builder.cookie;
        this.body = builder.body;
    }

    public static class Builder {
        private String url;
        private int redirectCode = 0;
        private int statusCode = 0;
        private DataOutputStream dos;
        private String cookie = "";
        private byte[] body;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder redirectCode(int redirectCode) {
            this.redirectCode = redirectCode;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder dataOutputStream(DataOutputStream dos) {
            this.dos = dos;
            return this;
        }

        public Builder cookie(String sessionId) {
            this.cookie = sessionId;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private static final ResourceUtil RESOURCE_UTIL = new ResourceUtil();

    public void sendResponse() throws IOException {
        if (redirectCode != 0) {
            redirect(url, dos, redirectCode);
        } else {
            response(url, dos);
        }
    }

    public void response(String url, DataOutputStream dos) {
        if (statusCode == 404) {
            response404Header(dos, this.body.length);
            responseBody(dos, this.body);
        } else {
            if (CookieUtil.isExist(cookie)) {
                response200HeaderWithCookie(dos, this.body.length, RESOURCE_UTIL.getContentType(url));
            } else {
                response200Header(dos, this.body.length, RESOURCE_UTIL.getContentType(url));
            }
            responseBody(dos, this.body);
        }
    }

    public void redirect(String url, DataOutputStream dos, int redirectionCode) {
        if (cookie != null) {
            if (cookie.isEmpty()) {
                redirectWithoutCookie(url, dos, redirectionCode);
            } else {
                redirectWithCookie(url, dos, redirectionCode);
            }
        } else {
            redirectWithoutCookie(url, dos, redirectionCode);
        }
    }

    private void redirectWithCookie(String url, DataOutputStream dos, int redirectionCode) {
        if (redirectionCode == 301) {
            response301Header(dos, url);
        } else if (redirectionCode == 302) {
            response302HeaderWithCookie(dos, url);
        }
    }

    private void redirectWithoutCookie(String url, DataOutputStream dos, int redirectionCode) {
        if (redirectionCode == 301) {
            response301Header(dos, url);
        } else if (redirectionCode == 302) {
            response302Header(dos, url);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: SID=" + cookie + "; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response301Header(DataOutputStream dos, String newLocation) {
        try {
            dos.writeBytes("HTTP/1.1 301 Moved Permanently \r\n");
            dos.writeBytes("Location: " + newLocation + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String newLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + newLocation + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String newLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + newLocation + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("Set-Cookie: SID=" + cookie + "; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
}
