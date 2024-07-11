package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RequestReaderTest {

    @Test
    @DisplayName("Body 가 존재하는 요청을 정상적으로 읽을 수 있어야 한다.")
    void readRequestWithBody() throws IOException {

        //given
        ByteArrayInputStream inputStream = new ByteArrayInputStream((
                "POST /create HTTP/1.1\r\n" +
                        "Content-Length: 93\r\n" +
                        "\r\n" +
                        "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net\r\n").getBytes()
        );

        Request expected = new Request.Builder(Method.POST, "/create")
                .addHeader("Content-Length", "93")
                .body("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net")
                .build();

        //when
        Request actual = RequestReader.readRequest(inputStream);

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Body 가 없는 요청을 정상적으로 읽을 수 있어야 한다.")
    void readRequest() throws IOException {

        //given
        ByteArrayInputStream inputStream = new ByteArrayInputStream((
                "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                        "Content-Length: 0\r\n" +
                        "\r\n").getBytes()
        );

        Request expected = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Content-Length", "0")
                .build();

        //when
        Request actual = RequestReader.readRequest(inputStream);

        //then
        assertEquals(expected, actual);
    }
}