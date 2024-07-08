package webserver.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathHandlerTest {

    @Test
    void getPathTest(){
        HttpRequest request = new HttpRequest.ReqeustBuilder("GET /registration?id=1&username=2&password=3 HTTP/1.1").build();

    }


}