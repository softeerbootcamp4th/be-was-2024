package util;

import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseFactory {
    public static void addHeader(DataOutputStream dos, HttpStatus httpStatus) throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " " + httpStatus.getMessage() + " \r\n");
    }

    public static void addContentType(DataOutputStream dos, String contentType) throws IOException {
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
    }

    public static void addContentLength(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
    }

    public static void addRedirection(DataOutputStream dos, String redirectUrl) throws IOException {
        dos.writeBytes("Location: " + redirectUrl + "\r\n");
    }

    public static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
