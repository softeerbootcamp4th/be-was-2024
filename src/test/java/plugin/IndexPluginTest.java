package plugin;

import db.PostDatabase;
import model.Post;
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
        Request request = createLoginedRequest(user);

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
        Request request = createLoginedRequest(createTestUser());
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

    @Test
    @DisplayName("index.html 하단에 글쓰기 버튼을 추가한다")
    public void testIndexWithLogin() throws IOException {

        //given
        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .build();

        //when
        Response response = indexPlugin.index(request);
        String body = response.getBody();

        //then
        assertTrue(body.contains("글쓰기"));

    }

    @Test
    @DisplayName("로그인한 사용자가 글쓰기 버튼을 클릭하면 /article/index.html 로 이동한다.")
    public void testIndexWithWrite() throws IOException {

        //given
        Request request = createLoginedRequest(createTestUser());

        //when
        Response response = indexPlugin.index(request);
        String body = response.getBody();

        //then
        assertTrue(body.contains("/article/index.html"));

    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 글쓰기 버튼을 클릭하면 로그인 페이지로 이동한다.")
    public void testIndexWithLoginAndWrite() throws IOException {

        //given
        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .build();

        //when
        Response response = indexPlugin.index(request);

        //then
        assertTrue(response.getBody().contains("/login"));

    }

    @Test
    @DisplayName("작성한 글의 제목을 index.html에서 보여준다.")
    public void testIndexWithoutLoginAndWrite() throws IOException {

        //given
        PostDatabase.addPost(new Post("testContent1", "testTitle1", "testAuthorName1"));
        Request request = new Request.Builder(HttpMethod.GET, "/index.html").build();

        //when
        Response response = indexPlugin.index(request);

        //then
        assertTrue(response.getBody().contains("testTitle1"));
    }

    private Request createLoginedRequest(User user){

        String sessionId = Session.save(user);

        Request request = new Request.Builder(HttpMethod.GET, "/index.html")
                .addHeader("Cookie", "sid="+sessionId)
                .build();

        return request;

    }

}