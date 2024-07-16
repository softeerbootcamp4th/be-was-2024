package webserver.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.PluginMapper;
import webserver.http.request.HttpMethod;
import webserver.http.request.Request;
import static util.Utils.getFile;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

    ResponseHandler responseHandler = new ResponseHandler(new PluginMapper());

    @Test
    @DisplayName("존재하지 않는 URL 요청에 대해 404 상태코드를 응답하도록 구현한다.")
    void testResponse404() throws IOException {

        //given
        Request notExistPathRequest = new Request.Builder(HttpMethod.GET, "/a/b/c/d/e.html").build();

        //when
        Response response = responseHandler.response(notExistPathRequest);

        //then
        assertEquals(Status.NOT_FOUND, response.getStatus());

    }

    @Nested
    @DisplayName("정적 요청에 대한 응답")
    class StaticResponseTest {

        @Test
        @DisplayName("HTML")
        void testResponseIndexHtmlSuccess() throws IOException {
            //given
            Request request = new Request.Builder(HttpMethod.GET, "/login/index.html")
                    .build();

            Response expected = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", "text/html;charset=utf-8")
                    .body(getFile("/login/index.html"))
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
            Request request = new Request.Builder(HttpMethod.GET, "/img/signiture.png")
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
            Request request = new Request.Builder(HttpMethod.GET, "/img/signiture.svg")
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
            Request request = new Request.Builder(HttpMethod.GET, "/main.css")
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
            Request request = new Request.Builder(HttpMethod.GET, "/favicon.ico")
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