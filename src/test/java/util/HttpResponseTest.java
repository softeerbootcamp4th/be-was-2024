package util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

class HttpResponseTest {

    private ByteArrayOutputStream baos;
    private HttpResponse response;

    @BeforeEach
    void setUp() {
        baos = new ByteArrayOutputStream();
        response = new HttpResponse(new DataOutputStream(baos));
    }

    @Test
    void testSendResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        byte[] body = "Hello, World!".getBytes();

        response.sendResponse(200, "OK", headers, body);

        String responseString = baos.toString();
        assertThat(responseString).contains("HTTP/1.1 200 OK");
        assertThat(responseString).contains("Content-Type: text/plain");
        assertThat(responseString).contains("Hello, World!");
    }
}