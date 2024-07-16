package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.Unauthorized;
import webserver.api.login.LoginHandler;
import webserver.api.logout.LogoutHandler;
import webserver.api.FunctionHandler;
import webserver.api.ReadFileHandler;
import webserver.api.pagehandler.RegistrationPageHandler;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;
import webserver.http.path.PathMap;

import static org.junit.jupiter.api.Assertions.*;

class PathMapTest {

    @DisplayName("check finding registration function")
    @Test
    void getPathMethodTest(){
        //given
        Methods method = Methods.POST;
        String path = "/create";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path , null);

        //then
        assertEquals(function.getClass(), Registration.class);
    }

    @DisplayName("check reading file function")
    @Test
    void getReadFileTest(){
        //given
        Methods method = Methods.GET;
        String path = "/resource";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path , null);

        //then
        assertEquals(function.getClass(), ReadFileHandler.class);
    }

    @DisplayName("check login file function")
    @Test
    void getLoginTest(){
        //given
        Methods method = Methods.POST;
        String path = "/login";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path , null);

        //then
        assertEquals(function.getClass(), LoginHandler.class);
    }

    @DisplayName("check logout file function")
    @Test
    void getLogoutTest(){
        //given
        Methods method = Methods.GET;
        String path = "/logout";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path, null);

        //then
        assertEquals(function.getClass(), LogoutHandler.class);
    }


    @DisplayName("check unauthorized access")
    @Test
    void unauthorizedTest(){
        //given
        Methods method = Methods.GET;
        String path = "/user/login";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path, null);

        //then
        assertEquals(function.getClass(), Unauthorized.class);
    }

    @DisplayName("check pagehanlder access")
    @Test
    void pagehandlerTest(){
        //given
        Methods method = Methods.GET;
        String path = "/registration";

        //when
        FunctionHandler function = PathMap.getPathMethod(method, path, null);

        //then
        assertEquals(function.getClass(), RegistrationPageHandler.class);
    }


}