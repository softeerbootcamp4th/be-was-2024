package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpCode;
import util.HttpRequest;
import util.HttpRequestMapper;
import util.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class LoginFailControllerTest {

    @DisplayName("doGet: 로그인 실패 화면을 불러옴")
    @Test
    void doGet() {
        // given
        HttpRequest request = HttpRequest.from("GET " + HttpRequestMapper.LOGIN_FAIL.getPath() + " HTTP/1.1");
        LoginFailController loginFailController = new LoginFailController();

        // when
        HttpResponse response = loginFailController.doGet(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.OK.getStatus());
        assertThat(response.getPath()).isEqualTo(HttpRequestMapper.LOGIN_FAIL.getPath());
        assertThat(response.getHttpVersion()).isEqualTo(request.getHttpVersion());
    }
}
