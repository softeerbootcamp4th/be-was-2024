package webserver.http;

import org.junit.jupiter.api.Test;
import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;

import static org.junit.jupiter.api.Assertions.*;

class PathMapTest {

    @Test
    void getPathMethodTest(){
        Methods method = Methods.POST;
        String path = "/create";
        ApiFunction function = PathMap.getPathMethod(method, path);
        assertEquals(function.getClass(), Registration.class);
    }

    @Test
    void getReadFileTest(){
        Methods method = Methods.GET;
        String path = "/";
        ApiFunction function = PathMap.getPathMethod(method, path);
        assertEquals(function.getClass(), ReadFile.class);
    }

}