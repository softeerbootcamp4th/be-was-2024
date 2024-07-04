package util;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void sendResponse(int statusCode, String statusMessage, String contentType, byte[] body) {
        try {

            writeStatusLine(statusCode, statusMessage);
            writeHeader("Content-Type", contentType);
            writeHeader("Content-Length", String.valueOf(body.length));
            writeBlankLine();
            writeBody(body);
        } catch (IOException e) {
            logger.error("Error sending HTTP response: " + e.getMessage());
        }
    }

    public void sendRedirect(String location) {
        try {
            logger.info("Sending redirect to " + location);
            writeStatusLine(303, "See Other");
            writeHeader("Location", location);
            writeBlankLine();
            logger.info("완료 redirect to " + location);
        } catch (IOException e) {
            logger.error("Error sending redirect response: " + e.getMessage());
        }
    }

    private void writeStatusLine(int statusCode, String statusMessage) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n");
    }

    private void writeHeader(String name, String value) throws IOException {
        dos.writeBytes(name + ": " + value + "\r\n");
    }

    private void writeBlankLine() throws IOException {
        dos.writeBytes("\r\n");
    }

    private void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
