package webserver.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.PluginLoader;
import webserver.http.request.Method;
import webserver.http.request.Request;
import static util.Utils.getFile;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

    @Nested
    @DisplayName("정적 요청에 대한 응답")
    class StaticResponseTest {

        ResponseHandler responseHandler = new ResponseHandler(new PluginLoader());

        @Test
        @DisplayName("HTML")
        void testResponseIndexHtmlSuccess() throws IOException {
            //given
            Request request = new Request.Builder(Method.GET, "/index.html")
                    .addHeader("Host", "localhost:8080")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/html;charset=utf-8")
                    .body(getFile("index.html"))
                    .build();

            //when
            Response actual = responseHandler.response(request);

            //then
            assertEquals(expected,actual);

        }

        @Test
        @DisplayName("PNG")
        void testAccessStaticResourcePngSuccess() throws IOException {
            //given
            Request request = new Request.Builder(Method.GET, "/img/signiture.png")
                    .addHeader("Host", "localhost:8080")
                    .addHeader("Connection", "keep-alive")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "image/png;charset=utf-8")
                    .body(getFile("/img/signiture.png"))
                    .build();

            //when
            Response actual = responseHandler.response(request);

            //then
            assertEquals(expected,actual);

        }

        @Test
        @DisplayName("SVG")
        void testAccessStaticResourceSvgSuccess() throws IOException {

            //given
            Request request = new Request.Builder(Method.GET, "/img/signiture.svg")
                    .addHeader("Host", "localhost:8080")
                    .addHeader("Connection", "keep-alive")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "image/svg+xml;charset=utf-8")
                    .body(getFile("/img/signiture.svg"))
                    .build();

            //when
            Response actual = responseHandler.response(request);

            //then
            assertEquals(expected,actual);
        }


        @Test
        @DisplayName("CSS")
        void testAccessStaticResourceCssSuccess() throws IOException {

            //given
            Request request = new Request.Builder(Method.GET, "/main.css")
                    .addHeader("Host", "localhost:8080")
                    .addHeader("Connection", "keep-alive")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/css;charset=utf-8")
                    .body(getFile("main.css"))
                    .build();

            //when
            Response actual = responseHandler.response(request);

            //then
            assertEquals(expected,actual);

        }

        @Test
        @DisplayName("ICO")
        void testAccessStaticResourceIcoSuccess() throws IOException {

            //given
            Request request = new Request.Builder(Method.GET, "/favicon.ico")
                    .addHeader("Host", "localhost:8080")
                    .addHeader("Connection", "keep-alive")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "image/png;charset=utf-8")
                    .body(getFile("favicon.ico"))
                    .build();

            //when
            Response actual = responseHandler.response(request);

            //then
            assertEquals(expected,actual);

        }

    }

}