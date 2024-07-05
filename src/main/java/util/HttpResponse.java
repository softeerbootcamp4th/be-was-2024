package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an HTTP response.
 */
public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    /**
     * Sends an HTTP response with the given status code, status message, headers, and body.
     *
     * @param statusCode the status code
     * @param statusMessage the status message
     * @param headers the headers
     * @param body the body
     */
    public void sendResponse(int statusCode, String statusMessage, Map<String, String> headers, byte[] body) {
        try {
            writeStatusLine(statusCode, statusMessage);
            logger.debug("HTTP/1.1 " + statusCode + " " + statusMessage);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                writeHeader(header.getKey(), header.getValue());
                logger.debug(header.getKey() + ": " + header.getValue());
            }
            writeBlankLine();
            if (body != null) {
                writeBody(body);
            }
        } catch (IOException e) {
            logger.error("Error sending HTTP response: " + e.getMessage());
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