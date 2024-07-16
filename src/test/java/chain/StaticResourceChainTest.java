package chain;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StaticResourceChainTest {
    StaticResourceChain staticResourceChain;

    @BeforeEach
    void setUp() {
        staticResourceChain = new StaticResourceChain();
    }

    @Test
    @DisplayName("존재하는 + 지원하는 파일 요청 시 OK")
    void handle_StatusOK_IfFileExistAndSupported() {
        MyHttpRequest req = new MyHttpRequest("GET /index.html HTTP/1.1", new ArrayList<>(), new byte[0]);
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceChain.act(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("content-type");

        Assertions.assertThat(type).isEqualTo(HttpStatusType.OK);
        Assertions.assertThat(contentType).isEqualTo("text/html");
    }

    @Test
    @DisplayName("존재하지 않는 파일 요청 시 반환 값 상태 코드 없음")
    void handle_StatusOK_IfFileNotExist() {
        MyHttpRequest req = new MyHttpRequest("GET /NotExist.html HTTP/1.1", new ArrayList<>(), new byte[0]);
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceChain.act(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("Content-Type");

        Assertions.assertThat(type).isNull();
        Assertions.assertThat(contentType).isNull();
    }

    @Test
    @DisplayName("지원되지 않는 확장 요청 시 상태 코드 없음")
    void handle_StatusUnSupported_IfFileNotSupported() {
        MyHttpRequest req = new MyHttpRequest("GET /test/main.rs HTTP/1.1", new ArrayList<>(), new byte[0]);
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceChain.act(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("Content-Type");

        Assertions.assertThat(type).isNull();
        Assertions.assertThat(contentType).isNull();
    }
}