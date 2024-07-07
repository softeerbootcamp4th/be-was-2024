package webserver.http.request;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void testParseRequest() throws IOException {

        //given
        String request = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Accept: */*\r\n\r\n";

        Request expected = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .build();

        //when
        Request actual = Request.parseRequest(request);

        //then
        assertEquals(expected, actual);

    }

    @Test
    void testEqualsTrue() {

        //given
        Request request1 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .build();

        Request request2 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .build();

        //when
        boolean equality = request1.equals(request2);

        //then
        assertTrue(equality);

    }

    @Test
    void testEqualsFalseByMethod() {

        //given
        Request request1 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .build();

        Request request2 = new Request.Builder(Method.POST, "/img/signiture.svg")
                .build();

        //when
        boolean equality = request1.equals(request2);

        //then
        assertFalse(equality);

    }

    @Test
    void testEqualsFalseByPath() {

        //given
        Request request1 = new Request.Builder(Method.GET, "/img/signiture.jpg")
                .build();

        Request request2 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .build();

        //when
        boolean equality = request1.equals(request2);

        //then
        assertFalse(equality);

    }

    @Test
    void testEqualsFalseByHeader() {

        //given
        Request request1 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .addHeader("Host", "localhost:7080")
                .build();

        Request request2 = new Request.Builder(Method.GET, "/img/signiture.svg")
                .addHeader("Host", "localhost:8080")
                .build();

        //when
        boolean equality = request1.equals(request2);

        //then
        assertFalse(equality);

    }
}