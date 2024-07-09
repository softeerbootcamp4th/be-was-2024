package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;

import static org.junit.jupiter.api.Assertions.*;

class PathMapTest {

    @DisplayName("check finding registration function")
    @Test
    void getPathMethodTest(){
        //given
        Methods method = Methods.POST;
        String path = "/create";

        //when
        ApiFunction function = PathMap.getPathMethod(method, path);

        //then
        assertEquals(function.getClass(), Registration.class);
    }

    @DisplayName("check reading file function")
    @Test
    void getReadFileTest(){
        //given
        Methods method = Methods.GET;
        String path = "/";

        //when
        ApiFunction function = PathMap.getPathMethod(method, path);

        //then
        assertEquals(function.getClass(), ReadFile.class);
    }

}