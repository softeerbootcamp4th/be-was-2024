package webserver.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("Response 객체가 같다.")
        void testEqualsTrue() {

            //given
            Response response1 = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/html; charset=UTF-8")
                    .addHeader("Connection", "keep-alive")
                    .body(("<!DOCTYPE html>\r\n" +
                            "<html>\r\n" +
                            "<head>\r\n" +
                            "    <title>Example</title>\r\n" +
                            "</head>\r\n" +
                            "<body>\r\n" +
                            "    <h1>Hello, World!</h1>\r\n" +
                            "</body>\r\n" +
                            "</html>\r\n\r\n").getBytes()
                    )
                    .build();

            Response response2 = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/html; charset=UTF-8")
                    .addHeader("Connection", "keep-alive")
                    .body(("<!DOCTYPE html>\r\n" +
                            "<html>\r\n" +
                            "<head>\r\n" +
                            "    <title>Example</title>\r\n" +
                            "</head>\r\n" +
                            "<body>\r\n" +
                            "    <h1>Hello, World!</h1>\r\n" +
                            "</body>\r\n" +
                            "</html>\r\n\r\n").getBytes()
                    )
                    .build();

            //when
            boolean equality = response1.equals(response2);

            //then
            assertTrue(equality);
        }

        @Test
        @DisplayName("Status 가 다르면 거짓")
        void testEqualsFalseByStatus(){
            //given
            Response response1 = new Response.Builder(Status.OK)
                    .build();

            Response response2 = new Response.Builder(Status.BAD_REQUEST)
                    .build();

            //when
            boolean equality = response1.equals(response2);

            //then
            assertFalse(equality);
        }

        @Test
        @DisplayName("Header 가 다르면 거짓")
        void testEqualsFalseByHeader(){
            //given
            Response response1 = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/css; charset=UTF-8")
                    .build();

            Response response2 = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/html; charset=UTF-8")
                    .build();

            //when
            boolean equality = response1.equals(response2);

            //then
            assertFalse(equality);
        }

        @Test
        @DisplayName("Body 가 다르면 거짓")
        void testEqualsFalseByBody(){
            //given
            Response response1 = new Response.Builder(Status.OK)
                    .body(("<!DOCTYPE html>\r\n" +
                            "<html>\r\n" +
                            "<head>\r\n" +
                            "    <title>Example1</title>\r\n" +
                            "</head>\r\n" +
                            "<body>\r\n" +
                            "    <h1>Hello, World!</h1>\r\n" +
                            "</body>\r\n" +
                            "</html>\r\n\r\n").getBytes()
                    )
                    .build();

            Response response2 = new Response.Builder(Status.OK)
                    .body(("<!DOCTYPE html>\r\n" +
                            "<html>\r\n" +
                            "<head>\r\n" +
                            "    <title>Example2</title>\r\n" +
                            "</head>\r\n" +
                            "<body>\r\n" +
                            "    <h1>Hello, World!</h1>\r\n" +
                            "</body>\r\n" +
                            "</html>\r\n\r\n").getBytes()
                    )
                    .build();

            //when
            boolean equality = response1.equals(response2);

            //then
            assertFalse(equality);
        }

    }



}