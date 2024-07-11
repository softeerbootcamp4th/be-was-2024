package plugin;

import db.Database;
import model.User;
import org.junit.jupiter.api.*;
import webserver.PluginMapper;
import webserver.http.Session;
import webserver.http.request.Method;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseHandler;
import webserver.http.response.Status;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserPluginTest {

    private UserPlugin userPlugin = new UserPlugin();

    @BeforeEach
    void after(){
        Database.deleteAll();
        Session.deleteAll();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class RegistrationTest {

        @Test
        @DisplayName("POST 회원가입이 정상 동작")
        void testRegistrationSuccessful() {

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


    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

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

            //given
            Database.addUser(new User("testUserId", "testPassword", "testName", "testEmail"));

            Request request = new Request.Builder(Method.POST, "/login")
                    .addHeader("Content-Length", String.valueOf("userId=testUserId&password=testPassword".length()))
                    .body("userId=testUserId&password=testPassword")
                    .build();

            //when
            Response response = userPlugin.login(request);

            //then
            assertRedirection(response, "/index.html");
        }

        @Test
        @DisplayName("로그인이 실패하면 /user/login_failed.html로 이동")
        public void testLoginFailure(){

            Request request = new Request.Builder(Method.POST, "/login")
                    .addHeader("Content-Length", String.valueOf("userId=testUserId&password=testPassword".length()))
                    .body("userId=testUserId&password=testPassword")
                    .build();

            //when
            Response response = userPlugin.login(request);

            //then
            assertRedirection(response, "/user/login_failed.html");

        }

        @Test
        @DisplayName("로그인이 성공할 경우 HTTP 헤더의 쿠키 값을 SID = 세션 ID 로 응답한다. ")
        public void testLoginSuccessCookie(){

            //given
            Database.addUser(createTestUser());

            Request request = new Request.Builder(Method.POST, "/login")
                    .addHeader("Content-Length", String.valueOf("userId=testUserId&password=testPassword".length()))
                    .body("userId=testUserId&password=testPassword")
                    .build();

            //when
            Response response = userPlugin.login(request);

            //then
            String cookieValue = response.getHeaderValue("Set-Cookie");
            assertNotNull(cookieValue);
            assertTrue(cookieValue.contains("sid="));
            assertTrue(cookieValue.contains("Path=\\/"));
        }

        @Test
        @DisplayName("사용자가 로그인 상태일 경우 /index.html 에서 사용자 이름을 표시해준다.")
        public void testIndexUserNameWithLogin() throws IOException {

            //given
            User user = createTestUser();
            String sessionId = Session.save(user);

            Request request = new Request.Builder(Method.GET, "/index.html")
                    .addHeader("Cookie", "sid="+sessionId)
                    .build();

            ResponseHandler responseHandler = new ResponseHandler(new PluginMapper());

            //when
            Response response = responseHandler.response(request);
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

            Request request = new Request.Builder(Method.GET, "/index.html")
                    .addHeader("Cookie", "sid="+sessionId)
                    .build();

            ResponseHandler responseHandler = new ResponseHandler(new PluginMapper());

            //when
            Response response = responseHandler.response(request);
            String body = response.getBody();

            //then
            assertTrue(body.contains("로그아웃"));
        }

        @Test
        @DisplayName("사용자가 로그인 상태가 아닐 경우 /index.html에서 [로그인] 버튼을 표시해 준다.")
        public void testIndexWithoutLogin() throws IOException {

            //given
            Request request = new Request.Builder(Method.GET, "/index.html")
                    .build();

            ResponseHandler responseHandler = new ResponseHandler(new PluginMapper());

            //when
            Response response = responseHandler.response(request);
            String body = response.getBody();

            //then
            assertTrue(body.contains("로그인"));

        }

        @Test
        @DisplayName("사용자가 정상적으로 로그아웃되는지 검증한다.")
        public void testLogout(){
            //given
            User user = createTestUser();
            String sessionId = Session.save(user);

            Request request = new Request.Builder(Method.POST, "/logout")
                    .addHeader("Cookie", "sid="+sessionId)
                    .build();

            //when
            userPlugin.logout(request);

            //then
            assertFalse(Session.isExist(sessionId));

        }

        @Test
        @DisplayName("사용자가 로그인 상태일 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.")
        public void testListWithLogin(){

            //given
            String sessionId = Session.save(createTestUser());

            Request request = new Request.Builder(Method.GET, "/user/list")
                    .addHeader("Cookie", "sid="+sessionId)
                    .build();

            //when
            Response response = userPlugin.userList(request);

            //then
            assertEquals(Status.OK, response.getStatus());
        }

        @Test
        @DisplayName("http://localhost:8080/user/list  페이지 접근시 로그인하지 않은 상태일 경우 로그인 페이지(login.html)로 이동한다.")
        public void testRedirection(){

            //given
            //쿠키가 없는 경우
            Request request1 = new Request.Builder(Method.GET, "/user/list")
                    .build();

            //쿠키가 있으나 서버에 저장되지 않은 경우
            Request request2 = new Request.Builder(Method.GET, "/user/list")
                    .addHeader("Cookie", "sid=123")
                    .build();

            //when
            Response response1 = userPlugin.userList(request1);
            Response response2 = userPlugin.userList(request2);

            //then
            assertEquals(response1.getStatus(), Status.SEE_OTHER);
            assertEquals(response2.getStatus(), Status.SEE_OTHER);

        }
    }

    private void assertRedirection(Response response, String dist){
        assertEquals(Status.SEE_OTHER, response.getStatus());
        assertEquals(dist, response.getHeaderValue("Location"));
    }

    public static User createTestUser(){
        return new User("testUserId", "testPassword", "testName", "testEmail");
    }

}