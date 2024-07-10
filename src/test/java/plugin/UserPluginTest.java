package plugin;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.request.Method;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import static org.junit.jupiter.api.Assertions.*;

class UserPluginTest {

    private UserPlugin userPlugin = new UserPlugin();

    @Test
    @DisplayName("POST 회원가입이 정상 동작")
    void testRegistrationSuccessful() {
        Database.deleteAll();

        //given
        Request request = new Request.Builder(Method.POST, "/create")
                .body(("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net").getBytes())
                .build();

        User expected = new User("javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net");

        //when
        userPlugin.create(request);
        User actual = Database.findUserById("javajigi");

        //then
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("/registration 요청에 대한 응답 성공")
    void testAccessRegistrationSuccess() {

        //given
        Request request = new Request.Builder(Method.GET, "/registration")
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .build();

        Response expected = new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/registration/index.html")
                .build();

        //when
        Response actual = userPlugin.registration(request);

        //then
        assertEquals(expected,actual);

    }

    @Test
    @DisplayName("가입을 완료하면 /index.html 페이지로 이동")
    void testRegistrationRedirection() {
        Database.deleteAll();

        //given
        Request request = new Request.Builder(Method.POST, "/create")
                .body(("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net").getBytes())
                .build();

        //when
        Response response = userPlugin.create(request);

        //then
        assertEquals(response.getStatus(), Status.SEE_OTHER);
        assertEquals(response.getHeaderValue("Location"), "/index.html");

    }

    @Test
    @DisplayName("GET 으로 회원가입 할 경우 실패")
    void testRegistrationGETFailure() {
        Database.deleteAll();

        //given
        Request request = new Request.Builder(Method.GET, "/create")
                .addParameter("userId", "javajigi")
                .addParameter("password", "password")
                .addParameter("name", "%EB%B0%95%EC%9E%AC%EC%84%B1")
                .addParameter("email", "javajigi%40slipp.net")
                .build();

        //when
        userPlugin.create(request);
        User actual = Database.findUserById("javajigi");

        //then
        assertNull(actual);

    }

    @Test
    @DisplayName("가입한 회원 정보로 로그인")
    public void testLoginSuccess(){

    }

    @Test
    @DisplayName("로그인 메뉴를 클릭하면 로그인 화면으로 이동")
    public void testAccessLoginPage(){

        //given
        Request request = new Request.Builder(Method.GET, "/login")
                .build();

        //when
        Response response = userPlugin.redirect(request);

        //then
        assertEquals(Status.SEE_OTHER, response.getStatus());
        assertEquals(response.getHeaderValue("Location"), "/login/index.html");

    }

    @Test
    @DisplayName("로그인이 성공하면 index.html 로 이동")
    public void testLoginSuccessRedirection(){

    }

    @Test
    @DisplayName("로그인이 실패하면 /user/login_failed.html로 이동")
    public void testLoginFailure(){

    }

}