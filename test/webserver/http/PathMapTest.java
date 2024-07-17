package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.Unauthorized;
import webserver.api.login.LoginHandler;
import webserver.api.logout.LogoutHandler;
import webserver.api.FunctionHandler;
import webserver.api.ReadFileHandler;
import webserver.api.pagehandler.MainPageHandler;
import webserver.api.pagehandler.RegistrationPageHandler;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;
import webserver.http.path.PathMap;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PathMapTest {

    @DisplayName("check finding registration function")
    @Test
    void getPathMethodTest() throws IOException {
        //given
        Methods method = Methods.POST;
        String path = "/create";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), Registration.class);
    }

    @DisplayName("check reading file function")
    @Test
    void getReadFileTest() throws IOException {
        //given
        Methods method = Methods.GET;
        String path = "/resource";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), ReadFileHandler.class);
    }

    @DisplayName("check login file function")
    @Test
    void getLoginTest() throws IOException {
        //given
        Methods method = Methods.POST;
        String path = "/login";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), LoginHandler.class);
    }

    @DisplayName("check logout file function")
    @Test
    void getLogoutTest() throws IOException {
        //given
        Methods method = Methods.GET;
        String path = "/logout";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), LogoutHandler.class);
    }


    @DisplayName("check unauthorized access")
    @Test
    void unauthorizedTest() throws IOException {
        //given
        Methods method = Methods.GET;
        String path = "/user/login";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), Unauthorized.class);
    }

    @DisplayName("check pagehanlder access")
    @Test
    void pagehandlerTest() throws IOException {
        //given
        Methods method = Methods.GET;
        String path = "/registration";

        //when
        FunctionHandler function = PathMap.getPathMethod(
                new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build()
        );

        //then
        assertEquals(function.getClass(), RegistrationPageHandler.class);
    }

    @DisplayName("check pathvariable access")
    @Test
    void pathvariableTest() throws IOException {
        //given
        Methods method = Methods.GET;
        String path = "/post/3";

        HttpRequest request = new HttpRequest.RequestBuilder(method.getMethod() + " "+path+" HTTP/1.1").build();
        //when
        FunctionHandler function = PathMap.getPathMethod(request);

        //then
        assertEquals(function.getClass(), MainPageHandler.class);
        assertEquals(request.getPathVariables().get("postid"), "3");
    }



}