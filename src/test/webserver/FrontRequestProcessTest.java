package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestObject;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FrontRequestProcessTest {

    FrontRequestProcess frontRequestProcess;
    HttpRequestObject httpRequestObject;
    String simpleRequestLine = "GET /index.html HTTP/1.1";
    String requestLineWithParams = "GET /user/create?userId=1&password=pass&email=abc@naver.com&name=name HTTP/1.1";

    @BeforeEach
    void setUp() {
        frontRequestProcess = FrontRequestProcess.getInstance();
    }

    @DisplayName("정적 자원 요청을 매핑하여 응답에 대한 정보를 반환한다.")
    @Test
    void httpRequestMapperTest() {
        // given
        httpRequestObject = HttpRequestObject.from(simpleRequestLine);

        // when
        Map<String, String> responseInfo = frontRequestProcess.handleRequest(httpRequestObject);

        // then
        assertThat(responseInfo.get("type")).isEqualTo("static");
        assertThat(responseInfo.get("path")).isEqualTo("/index.html");
        assertThat(responseInfo.get("statusCode")).isEqualTo("200");
    }

    @DisplayName("회원가입 요청을 매핑 및 처리하고 응답에 대한 정보를 반환한다.")
    @Test
    void httpRequestMapperWithParamsTest() {
        // given
        httpRequestObject = HttpRequestObject.from(requestLineWithParams);

        // when
        Map<String, String> responseInfo = frontRequestProcess.handleRequest(httpRequestObject);

        // then
        assertThat(responseInfo.get("type")).isEqualTo("dynamic");
        assertThat(responseInfo.get("path")).isEqualTo("/index.html");
        assertThat(responseInfo.get("statusCode")).isEqualTo("302");
    }
}
