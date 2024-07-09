package webserver.http.response;

import db.Database;
import model.User;
import org.junit.jupiter.api.Test;
import webserver.http.request.Method;
import webserver.http.request.Request;
import static util.Utils.getFile;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

    @Test
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
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
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
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
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
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);
    }


    @Test
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
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
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
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
    void testAccessRegistrationSuccess() throws IOException {

        //given
        Request request = new Request.Builder(Method.GET, "/registration")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .build();

        Response expected = new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/registration/index.html")
                .build();

        //when
        Response actual = ResponseHandler.response(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
    void testRegistrationSuccess() throws IOException {

        Request request = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .build();

        User expected = new User("javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net");

        //when
        ResponseHandler.response(request);
        User actual = Database.findUserById("javajigi");

        //then
        assertEquals(expected, actual);

    }



}