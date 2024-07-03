package webserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {

    @Test
    void get() throws IOException {

        //given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n";

        //when
        Header header = new Header(request);

        //then
        assertEquals(header.get("Host"), "localhost:8080");

    }

}