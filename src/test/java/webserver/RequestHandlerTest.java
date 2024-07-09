package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {

    private static final String CRLF = "\r\n";

    @Test
    @DisplayName("유저 생성 테스트")
    public void testAddUser() throws IOException {
        // given
        String body = "userId=123&password=123&name=123&email=123@gmail.com";
        String httpRequest =
                "POST /user/create HTTP/1.1" + CRLF +
                "Host: localhost:8080" + CRLF +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                "Accept: application/json" + CRLF +
                "Connection: keep-alive" + CRLF +
                "Content-Type: application/x-www-form-urlencoded" + CRLF +
                "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + CRLF +
                CRLF +
                body;

        InputStream in = stringToInputStream(httpRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        User user = Database.findUserById("123");
        assertThat(user.getUserId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("123");
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(user.getEmail()).isEqualTo("123@gmail.com");
    }

    private InputStream stringToInputStream(String httpRequest) {
        // 문자열을 바이트 배열로 변환
        byte[] httpRequestBytes = httpRequest.getBytes(StandardCharsets.UTF_8);

        // 바이트 배열을 InputStream으로 변환
        return new ByteArrayInputStream(httpRequestBytes);
    }

}