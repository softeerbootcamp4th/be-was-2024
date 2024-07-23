package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.enums.Methods;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHandlerTest {

    @DisplayName("get method test")
    @Test
    void getMethod() throws IOException {
        //given
        Methods method = Methods.GET;

        //when
        HttpRequest requestHandler  = new HttpRequest.RequestBuilder(method.getMethod()+" / HTTP/1.1").build();

        //then
        assertEquals(requestHandler.getMethod(), method);
    }

    @DisplayName("get url test")
    @Test
    void getUrl() throws IOException {
        //given
        String path = "/test/url";

        //when
        HttpRequest requestHandler  = new HttpRequest.RequestBuilder("GET " + path +" HTTP/1.1").build();

        //then
        assertEquals(requestHandler.getUrl().getPath(),path );
    }

    @DisplayName("get parameter of url test")
    @Test
    void getParam() throws IOException {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("b", "2");
        params.put("c", "3");

        StringBuilder url = new StringBuilder();
        url.append("/registration?");
        for(Map.Entry<String, String> entry : params.entrySet()) {
            url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }


        //when
        HttpRequest request = new HttpRequest.RequestBuilder("GET " + url.toString() + " HTTP/1.1").build();

        //then
        for(Map.Entry<String, String> entry : params.entrySet()) {
            assertEquals(request.getUrl().getParamsMap().get(entry.getKey()), entry.getValue());
        }
    }

    @DisplayName("check if parameter is empty test")
    @Test
    void getemptyParam() throws IOException {
        //given

        //when
        HttpRequest requestHandler  = new HttpRequest.RequestBuilder("GET /registration HTTP/1.1").build();

        //then
        assertTrue(requestHandler.getUrl().getParamsMap().isEmpty());
    }

    @DisplayName("check if ioexception throws for wrong request")
    @Test
    void testIOException()  {
        //given

        //when & then
        assertThrows(IOException.class, () ->
        { HttpRequest requestHandler  = new HttpRequest.RequestBuilder("wrong request").build(); });
    }
}
