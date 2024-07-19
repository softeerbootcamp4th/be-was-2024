package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class RequestReaderTest {

    @Test
    @DisplayName("Body 가 존재하는 요청을 정상적으로 읽을 수 있어야 한다.")
    void readRequestWithBody() throws IOException {

        //given
        InputStream inputStream = getInputStream("POST /create HTTP/1.1\r\n" +
                "Content-Length: 93\r\n" +
                "\r\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net\r\n");

        RequestReader requestReader = new RequestReader(inputStream);
        Request expected = new Request.Builder(HttpMethod.POST, "/create")
                .addHeader("Content-Length", "93")
                .body("userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net")
                .build();

        //when
        Request actual = requestReader.readRequest();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Body 가 없는 요청을 정상적으로 읽을 수 있어야 한다.")
    void readRequest() throws IOException {

        //given
        InputStream inputStream = getInputStream(
                "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n");

        RequestReader requestReader = new RequestReader(inputStream);
        Request expected = new Request.Builder(HttpMethod.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .addHeader("Content-Length", "0")
                .build();

        //when
        Request actual = requestReader.readRequest();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("한글로 들어온 Body 내용을 변환할 수 있어야 한다.")
    void decoding() throws IOException {

        InputStream inputStream = getInputStream(
                "POST /create HTTP/1.1\r\n" +
                "Content-Length: 76\r\n" +
                "\r\n" +
                "userId=asd&name=%EC%A1%B0%EB%8F%84%EC%97%B0&email=asd%40asd.com&password=asd\r\n");

        RequestReader requestReader = new RequestReader(inputStream);
        Request expected = new Request.Builder(HttpMethod.POST, "/create")
                .addHeader("Content-Length", "76")
                .body("userId=asd&name=조도연&email=asd@asd.com&password=asd")
                .build();

        assertEquals(expected, requestReader.readRequest());

    }

    private InputStream getInputStream(String string){
        return new ByteArrayInputStream(string.getBytes());
    }
}