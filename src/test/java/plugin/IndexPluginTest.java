package plugin;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.Session;
import webserver.http.request.HttpMethod;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static plugin.UserPluginTest.createTestUser;

class IndexPluginTest {

    private IndexPlugin indexPlugin = new IndexPlugin();

    @Test
    @DisplayName("사용자가 로그인 상태일 경우 /index.html 에서 사용자 이름을 표시해준다.")
    public void testIndexUserNameWithLogin() throws IOException {

        //given
        User user = createTestUser();
        String sessionId = Session.save(user);

        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .addHeader("Cookie", "sid="+sessionId)
                .build();

        //when
        Response response = indexPlugin.index(request);
        String body = response.getBody();

        //then
        assertTrue(body.contains(user.getName()));
    }

    @Test
    @DisplayName("사용자가 로그인 상태일 경우 /index.html 에서 로그아웃 버튼을 표시해준다.")
    public void testIndexLogoutBtnWithLogin() throws IOException {
        //given
        User user = createTestUser();
        String sessionId = Session.save(user);

        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .addHeader("Cookie", "sid="+sessionId)
                .build();

        //when
        Response response = indexPlugin.index(request);
        String body = response.getBody();

        //then
        assertTrue(body.contains("로그아웃"));
    }

    @Test
    @DisplayName("사용자가 로그인 상태가 아닐 경우 /index.html에서 [로그인] 버튼을 표시해 준다.")
    public void testIndexWithoutLogin() throws IOException {

        //given
        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .build();

        //when
        Response response = indexPlugin.index(request);
        String body = response.getBody();

        //then
        assertTrue(body.contains("로그인"));

    }


}