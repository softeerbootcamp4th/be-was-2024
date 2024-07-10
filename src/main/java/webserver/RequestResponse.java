package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class RequestResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponse.class);
    private HttpRequest httpRequest;
    private DataOutputStream dos;

    public RequestResponse(HttpRequest httpRequest, DataOutputStream dos) {
        this.httpRequest = httpRequest;
        this.dos = dos;
    }

    public void redirectPath(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void setCookieAndRedirectPath(String sessionId, String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("Set-Cookie: sid=" + sessionId + "; Path=/\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void openPath(String url) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String contentType = FileHandler.determineContentType(file.getName());

            response200Header(fileBody.length, contentType);
            responseBody(fileBody);
        }
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void response404Header() throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes("<h1>404 Not Found</h1>");
        dos.flush();
    }

    private void responseBody(byte[] body) throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}
