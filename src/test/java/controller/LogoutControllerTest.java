package controller;

import exception.RequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import session.Session;
import util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LogoutControllerTest {

    LogoutController logoutController;
    HttpRequest request = HttpRequest.from("POST " + HttpRequestMapper.LOGOUT_REQUEST + " HTTP/1.1");

    @BeforeEach
    void setUp() {
        logoutController = new LogoutController();
    }

    @DisplayName("doPost: 세션을 삭제하고 로그아웃 처리한다.")
    @Test
    void doPost(){
        // given
        Session session = new Session("sdjlkqw", "userId");
        request.putSession(session);

        // when
        HttpResponse response = logoutController.doPost(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.FOUND.getStatus());
        assertThat(response.getTotalHeaders())
                .contains("Location: " + HttpRequestMapper.DEFAULT_PAGE.getPath())
                .contains("sid=" + session.getSessionId() + "; Path=/; Max-Age=0");
    }


    @DisplayName("doPost: 세션이 없으면 예외가 발생해야 한다.")
    @Test
    void doPost_noSession() {
        // when & then
        assertThatThrownBy(() -> logoutController.doPost(request))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(ConstantUtil.INVALID_HEADER);
    }
}
