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

    @Test
    void getParam(){
        HttpRequest requestHandler  = new HttpRequest("GET /registration?id=1&username=3&password=5 HTTP/1.1");
        System.out.println(requestHandler.getUri().getParamsMap().toString());
        assertEquals(requestHandler.getUri().getParamsMap().isEmpty(), false);
    }

    @Test
    void getemptyParam(){
        HttpRequest requestHandler  = new HttpRequest("GET /registration HTTP/1.1");
        System.out.println(requestHandler.getUri().getParamsMap().toString());
        assertEquals(requestHandler.getUri().getParamsMap().isEmpty(), true);
    }
}
