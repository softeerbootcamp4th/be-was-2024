package webserver;

import constant.HttpMethod;
import db.Database;
import dto.HttpRequest;
import handler.Handler;
import handler.HandlerManager;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("RequestHandler 테스트")
public class RequestHandlerTest {

    private final HandlerManager handlerManager = HandlerManager.getInstance();


    @Test
    @DisplayName("회원가입 성공")
    public void HandleRequestCreateSuccess() throws IOException {
        // given
        HttpRequest httpRequest = new HttpRequest();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "javajigi");
        queryParams.put("password", "password");
        queryParams.put("name", "박재성");
        queryParams.put("email", "javajigi@slipp.net");

        httpRequest.setPath("/user/create");
        httpRequest.setHttpMethod(HttpMethod.GET.name());
        httpRequest.setQueryParams(queryParams);

        Handler handler = handlerManager.getHandler(httpRequest);
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);

        // when
        handler.handle(new DataOutputStream(dataOutputStream), queryParams);

        // then
        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
        assertThat(user.getName()).isEqualTo("박재성");
    }

    @Test
    @DisplayName("회원가입 실패")
    public void HandleRequestCreateFail() throws IOException {

        // given
        // email 정보 누락
        HttpRequest httpRequest = new HttpRequest();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "javajigi");
        queryParams.put("password", "password");
        queryParams.put("name", "박재성");

        httpRequest.setPath("/user/create");
        httpRequest.setHttpMethod(HttpMethod.GET.name());
        httpRequest.setQueryParams(queryParams);

        Handler handler = handlerManager.getHandler(httpRequest);
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);

        // when & then
        assertThatThrownBy(() -> handler.handle(new DataOutputStream(dataOutputStream), queryParams))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
