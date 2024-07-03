package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseTest {

    @Test
    public void testSendResponse200() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        HttpResponse response = new HttpResponse(dos);

        String body = "Hello World!";
        response.sendResponse(200, "OK", "text/html;charset=utf-8", body.getBytes());

        String expectedResponse = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: 12\r\n" +
                "\r\n" +
                "Hello World!";
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testSendResponse404() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        HttpResponse response = new HttpResponse(dos);

        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        response.sendResponse(404, "Not Found", "text/html;charset=utf-8", body.getBytes());

        String expectedResponse = "HTTP/1.1 404 Not Found \r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: 44\r\n" +
                "\r\n" +
                "<html><body><h1>404 Not Found</h1></body></html>";
        assertEquals(expectedResponse, outputStream.toString());
    }
}
