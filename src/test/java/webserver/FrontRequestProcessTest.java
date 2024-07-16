package webserver;

import db.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FrontRequestProcessTest {

    FrontRequestProcess frontRequestProcess;
    HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        frontRequestProcess = FrontRequestProcess.getInstance();
    }

    @DisplayName("정적 자원 요청을 매핑하여 응답에 대한 정보를 반환한다.")
    @Test
    void staticResourceTest() throws IOException {
        // given
        httpRequest = HttpRequest.from("GET /index.html HTTP/1.1");

        // when
        HttpResponse httpResponseObject = frontRequestProcess.handleRequest(httpRequest);

        // then
        assertThat(httpResponseObject.getType()).isEqualTo("static");
        assertThat(httpResponseObject.getPath()).isEqualTo("/index.html");
        assertThat(httpResponseObject.getStatusCode()).isEqualTo("200");
    }

    @DisplayName("회원가입 요청을 매핑 및 처리하고 응답에 대한 정보를 반환한다.")
    @Test
    void signUpTest() throws IOException {
        // given
        httpRequest = HttpRequest.from("POST /user/create HTTP/1.1");
        //httpRequest.putBody("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");

        // when
        HttpResponse httpResponseObject = frontRequestProcess.handleRequest(httpRequest);

        // then
        assertThat(httpResponseObject.getType()).isEqualTo("dynamic");
        assertThat(httpResponseObject.getPath()).isEqualTo("/index.html");
        assertThat(httpResponseObject.getStatusCode()).isEqualTo("302");
        assertThat(Database.findUserById("javajigi").get().getUserId()).isEqualTo("javajigi");
    }
}
