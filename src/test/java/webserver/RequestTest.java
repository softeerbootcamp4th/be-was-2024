package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

class RequestTest {
    @Test
    @DisplayName("GET과 query가 포함된 path가 들어왔을 때 isQueryString()이 정상적으로 동작하는지 테스트")
    void isQueryStringTest() throws IOException {
        // given
        String query = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        // when
        Request request = Request.from(new ByteArrayInputStream(query.getBytes()));

        // then
        assertThat(request.isQueryString()).isEqualTo(true);
    }

    @Test
    @DisplayName("GET과 qeury가 포함된 path가 들어왔을 때 path와 query string이 정상적으로 분리되는지 테스트")
    void splitPathAndQueryStringTest() throws IOException {
        // given
        String query = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        // when
        Request request = Request.from(new ByteArrayInputStream(query.getBytes()));

        // then
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com");
    }

    @Test
    @DisplayName("request header의 필드에서 띄어쓰기가 한 칸이 아닌 경우 제대로 파싱 되는지 테스트")
    void requestHeaderFieldRandomSpaceTest() throws IOException {
        // given
        String query = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

        // when
        Request request = Request.from(new ByteArrayInputStream(query.getBytes()));

        // then
        HashMap<String, String> headers = (HashMap<String, String>) request.getHeaders();
        // Connection 필드에 예상치 못한 공백이 있지만 제대로 파싱 되는지 테스트
        assertThat(headers.get("Connection")).isEqualTo("keep-alive");
    }

    @Test
    @DisplayName("header의 Content-Length 필드 값 파싱 테스트")
    void requestHeaderContentLengthTest() throws IOException {
        // given
        String query = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 48";

        // when
        Request request = Request.from(new ByteArrayInputStream(query.getBytes()));

        //then
        int contentLength = request.getContentLength();
        assertThat(contentLength).isEqualTo(48);
    }
}