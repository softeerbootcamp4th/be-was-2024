package webserver;

import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.enums.Methods;


import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHandlerTest {

    @Test
    void getMethod() {
        HttpRequest requestHandler  = new HttpRequest.ReqeustBuilder("GET / HTTP/1.1").build();
        assertEquals(requestHandler.getMethod(), Methods.GET);
    }


    @Test
    void getUri() {
        HttpRequest requestHandler  = new HttpRequest.ReqeustBuilder("GET / HTTP/1.1").build();
        assertEquals(requestHandler.getMethod(), Methods.GET);
    }

    @Test
    void getParam(){
        HttpRequest requestHandler  = new HttpRequest.ReqeustBuilder("GET /registration?id=1&username=3&password=5 HTTP/1.1").build();
        System.out.println(requestHandler.getUri().getParamsMap().toString());
        assertFalse(requestHandler.getUri().getParamsMap().isEmpty());
    }

    @Test
    void getemptyParam(){
        HttpRequest requestHandler  = new HttpRequest.ReqeustBuilder("GET /registration HTTP/1.1").build();
        System.out.println(requestHandler.getUri().getParamsMap().toString());
        assertTrue(requestHandler.getUri().getParamsMap().isEmpty());
    }
}
