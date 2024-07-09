package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("Request 객체가 같다.")
        void testEqualsTrue() {

            //given
            Request request1 = new Request.Builder(Method.GET, "/img/signiture.svg")
                    .addHeader("Host", "localhost:8080")
                    .addParameter("userId", "javajigi")
                    .body(("body contents").getBytes())
                    .build();

            Request request2 = new Request.Builder(Method.GET, "/img/signiture.svg")
                    .addHeader("Host", "localhost:8080")
                    .addParameter("userId", "javajigi")
                    .body(("body contents").getBytes())
                    .build();

            //when
            boolean equality = request1.equals(request2);

            //then
            assertTrue(equality);

        }

        @Test
        @DisplayName("Method 가 다르면 거짓")
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
        @DisplayName("Path 가 다르면 거짓")
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
        @DisplayName("Header 가 다르면 거짓")
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

    @Test
    @DisplayName("Body가 없는 요청에 대해 정상적으로 파싱 가능")
    void testParseRequestWithoutBody() throws IOException {

        //given
        String request = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n\r\n";

        Request expected = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Host", "localhost:8080")
                .build();

        //when
        Request actual = Request.parseRequestWithoutBody(request);

        //then
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("String 변환 테스트")
    void testToString(){

        //given
        Request request = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Host", "localhost:8080")
                .build();

        String expected = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n\r\n";

        //when
        String actual = request.toString();

        //then
        assertEquals(expected, actual);
    }

}