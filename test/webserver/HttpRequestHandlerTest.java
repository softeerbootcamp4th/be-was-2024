package webserver;

import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;


import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHandlerTest {

    @Test
    void getMethod() {
        HttpRequest requestHandler  = new HttpRequest("GET / HTTP/1.1");
        assertEquals(requestHandler.getMethod(), "GET");
    }


    @Test
    void getUri() {
        HttpRequest requestHandler  = new HttpRequest("GET / HTTP/1.1");
        assertEquals(requestHandler.getMethod(), "GET");
    }

}
