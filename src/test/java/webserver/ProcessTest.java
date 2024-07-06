package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import webserver.http.Processor;

import java.io.*;

public class ProcessTest {

    private String getFile(String fileName) throws IOException {
        File file = new File("src/main/resources/static/"+fileName);
        FileInputStream fis = new FileInputStream(file);
        return new String(fis.readAllBytes());
    }

    @Test
    public void accessIndexSuccess() throws IOException {

        //given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n";

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        String expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Length: 6326\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n\r\n" +getFile("index.html");
        String actual;

        //when
        processor.process();
        actual = outputStream.toString();

        //then
        assertEquals(expected,actual);

    }

    @Test
    void accessStaticResourcePngSuccess() throws IOException {

        //given
        String request = "GET /img/signiture.png HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";

        String expected="HTTP/1.1 200 OK \r\n" +
                "Content-Length: 2825\r\n" +
                "Content-Type: image/png;charset=utf-8\r\n\r\n" +getFile("/img/signiture.png");

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        //when
        processor.process();
        String actual = outputStream.toString();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void accessStaticResourceSvgSuccess() throws IOException {

        //given
        String request = "GET /img/signiture.svg HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";

        String expected="HTTP/1.1 200 OK \r\n" +
                "Content-Length: 11586\r\n" +
                "Content-Type: image/svg+xml;charset=utf-8\r\n\r\n" +getFile("/img/signiture.svg");

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        //when
        processor.process();
        String actual = outputStream.toString();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void accessStaticResourceCssSuccess() throws IOException {

        //given
        String request = "GET /main.css HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";

        String expected="HTTP/1.1 200 OK \r\n" +
                "Content-Length: 2085\r\n" +
                "Content-Type: text/css;charset=utf-8\r\n\r\n" +getFile("main.css");

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        //when
        processor.process();
        String actual = outputStream.toString();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void accessStaticResourceIcoSuccess() throws IOException {

        //given
        String request = "GET /favicon.ico HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";

        String expected="HTTP/1.1 200 OK \r\n" +
                "Content-Length: 38505\r\n" +
                "Content-Type: image/png;charset=utf-8\r\n\r\n" +getFile("favicon.ico");

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        //when
        processor.process();
        String actual = outputStream.toString();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void accessRegistrationSuccess() throws IOException {

        //given
        String request = "GET /registration HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        String expected = "HTTP/1.1 303 OK \r\n" +
                "Content-Length: 0\r\n" +
                "Location: /registration/index.html\r\n\r\n";

        //when
        processor.process();
        String actual = outputStream.toString();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void registrationSuccess() throws IOException {

        //given
        String request = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Processor processor = new Processor(inputStream, outputStream);

        User user = new User("javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net");

        //when
        processor.process();
        User actual = Database.findUserById("javajigi");

        //then
        assertEquals(user.toString(), actual.toString());

    }


}
