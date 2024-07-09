package routehandler.route;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StaticResourceHandlerTest {
    private StaticResourceHandler staticResourceHandler;

    @BeforeEach()
    void beforeEach() {
        staticResourceHandler = new StaticResourceHandler();
    }

    @Test
    @DisplayName("존재하는 + 지원하는 파일 요청 시 OK")
    void handle_StatusOK_IfFileExistAndSupported() {
        MyHttpRequest req = new MyHttpRequest("GET /index.html HTTP/1.1", new ArrayList<>());
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceHandler.handle(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("Content-Type");

        Assertions.assertThat(type).isEqualTo(HttpStatusType.OK);
        Assertions.assertThat(contentType).isEqualTo("text/html");
    }

    @Test
    @DisplayName("존재하지 않는 파일 요청 시 NOT_FOUND")
    void handle_StatusOK_IfFileNotExist() {
        MyHttpRequest req = new MyHttpRequest("GET /NotExist.html HTTP/1.1", new ArrayList<>());
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceHandler.handle(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("Content-Type");

        Assertions.assertThat(type).isEqualTo(HttpStatusType.NOT_FOUND);
        Assertions.assertThat(contentType).isNull();
    }
    @Test
    @DisplayName("지원되지 않는 확장 요청 시 UNSURPPORTED_MEDIA_TYPE")
    void handle_StatusUnSupported_IfFileNotSupported() {
        MyHttpRequest req = new MyHttpRequest("GET /test/main.rs HTTP/1.1", new ArrayList<>());
        MyHttpResponse res = new MyHttpResponse("HTTP/1.1");

        staticResourceHandler.handle(req, res);

        HttpStatusType type = res.getStatusType();
        String contentType = res.getHeaders().getHeader("Content-Type");

        Assertions.assertThat(type).isEqualTo(HttpStatusType.UNSUPPORTED_MEDIA_TYPE);
        Assertions.assertThat(contentType).isNull();
    }
}