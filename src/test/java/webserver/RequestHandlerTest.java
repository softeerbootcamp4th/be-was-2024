package webserver;

import constant.HttpMethod;
import db.Database;
import dto.HttpRequest;
import exception.InvalidHttpRequestException;
import handler.Handler;
import handler.HandlerManager;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RequestHandler 테스트")
public class RequestHandlerTest {

    private final HandlerManager handlerManager = HandlerManager.getInstance();


    @Test
    @DisplayName("회원가입 성공")
    public void HandleRequestCreateSuccess() throws IOException {
        // given
        HttpRequest httpRequest = new HttpRequest();
        String requestBody = "userId=javajigi" +
                "&password=password" +
                "&name=%EB%B0%95%EC%9E%AC%EC%84%B1" +
                "&email=javajigi%40slipp.net";


        httpRequest.setPath("/user/create");
        httpRequest.setHttpMethod(HttpMethod.POST.name());
        httpRequest.setBody(requestBody);
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setHeader("Content-Length", String.valueOf(requestBody.length()));

        Handler handler = handlerManager.getHandler(httpRequest);
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);

        // when
        handler.handle(new DataOutputStream(dataOutputStream), httpRequest);

        // then
        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
        assertThat(user.getName()).isEqualTo("박재성");
    }

    @Test
    @DisplayName("회원가입 실패 (회원정보 일부 누락)")
    public void HandleRequestCreateFail1() {

        // given
        // email 정보 누락
        HttpRequest httpRequest = new HttpRequest();
        String requestBody = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1";

        httpRequest.setPath("/user/create");
        httpRequest.setHttpMethod(HttpMethod.POST.name());
        httpRequest.setBody(requestBody);
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setHeader("Content-Length", String.valueOf(requestBody.length()));

        Handler handler = handlerManager.getHandler(httpRequest);
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);

        // when & then
        assertThatThrownBy(() -> handler.handle(new DataOutputStream(dataOutputStream), httpRequest))
                .isInstanceOf(InvalidHttpRequestException.class)
                .hasMessage("User information cannot be null");
    }

    @Test
    @DisplayName("회원가입 실패 (GET 요청)")
    public void HandleRequestCreateFail2() {

        // given
        // email 정보 누락
        HttpRequest httpRequest = new HttpRequest();
        String requestBody = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        httpRequest.setPath("/user/create");
        httpRequest.setHttpMethod(HttpMethod.GET.name());
        httpRequest.setBody(requestBody);
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setHeader("Content-Length", String.valueOf(requestBody.length()));

        // when & then
        // GET 요청일 경우, handler를 찾지 못해서 예외 발생
        assertThatThrownBy(() -> handlerManager.getHandler(httpRequest))
                .isInstanceOf(InvalidHttpRequestException.class)
                .hasMessage("handler not found");
    }

}
