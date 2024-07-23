package http.utils;

import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseMessageBuildUtilTest {
    @Test
    @DisplayName("http version 또는 status가 지정되어 있지 않으면 예외 발생")
    void build_throwExceptionIfNoVersionOrStatus() {
        // http version은 반드시 설정하도록 구성되어 있음
        MyHttpResponse response = new MyHttpResponse("HTTP/1.1");

        Assertions.assertThatThrownBy(() -> {
            HttpResponseMessageBuildUtil.build(response);
        });
    }

    @Test
    @DisplayName("필수 인자가 모두 표현되어 있다면 정상적인 메시지 발생")
    void build_returnNormalMessage() {
        // http version은 반드시 설정하도록 구성되어 있음
        MyHttpResponse response = new MyHttpResponse("HTTP/1.1");
        response.setStatusInfo(HttpStatusType.OK);

        byte[] messageBytes = HttpResponseMessageBuildUtil.build(response);

        // 응답 라인 1줄, 공백 한줄
        byte[] expectedMessageBytes = "HTTP/1.1 200 OK\r\n\r\n".getBytes();

        Assertions.assertThat(expectedMessageBytes).isEqualTo(messageBytes);
    }

    @Test
    @DisplayName("필수 인자가 모두 표현되어 있다면 정상적인 메시지 발생(헤더, 바디 포함)")
    void build_returnNormalMessage2() {
        // http version은 반드시 설정하도록 구성되어 있음
        MyHttpResponse response = new MyHttpResponse("HTTP/1.1");
        String body = "test";

        byte[] expectedMessageBytes = "HTTP/1.1 200 OK\r\ncontent-length: 4\r\n\r\ntest".getBytes();

        response.setBody(body.getBytes());
        response.setStatusInfo(HttpStatusType.OK);

        byte[] messageBytes = HttpResponseMessageBuildUtil.build(response);

        Assertions.assertThat(expectedMessageBytes).isEqualTo(messageBytes);
    }


}