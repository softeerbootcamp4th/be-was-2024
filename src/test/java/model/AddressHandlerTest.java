package model;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static model.User.createUserFromUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static webserver.AddressHandler.*;

public class AddressHandlerTest {

    private DataOutputStream dos;
    private ByteArrayOutputStream baos;

    @BeforeEach
    public void setUp() {
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
    }

    @Test
    public void testGetFilePath_Registration() throws IOException {
        String urlPath = "/registration";
        String expectedFilePath = "src/main/resources/static/registration/index.html";
        String actualFilePath = getFilePath(urlPath, dos);
        assertEquals(expectedFilePath, actualFilePath);
    }

    @Test
    public void testGetFilePath_Other() throws IOException {
        String urlPath = "/index.html";
        String expectedFilePath = "src/main/resources/static/index.html";
        String actualFilePath = getFilePath(urlPath, dos);
        assertEquals(expectedFilePath, actualFilePath);
    }

    //직접 요청 생략
//    @Test
//    public void testCreateUserFromUrl_ValidUrl() {
//        String urlPath = "/user/create?name=John&email=john@example.com&password=1234&username=john123";
//        User actualUser = createUserFromUrl(urlPath);
//        User expectedUser = new User("John", "john@example.com", "1234", "john123");
//        assertEquals(expectedUser, actualUser);
//    }

    @Test
    public void testCreateUserFromUrl_InvalidUrl() {
        String urlPath = "/user/create";
        User actualUser = createUserFromUrl(urlPath);
        assertNull(actualUser);
    }

    //직접 요청 생략
//    @Test
//    public void testHandleUserCreation_ValidUrl() throws IOException {
//        String urlPath = "/user/create?name=John&email=john@example.com&password=1234&username=john123";
//        handleUserCreation(urlPath);
//        String expectedOutput = "HTTP/1.1 302 Found\r\nLocation: /index.html\r\n\r\n";
//        String actualOutput = baos.toString();
//        assertEquals(expectedOutput, actualOutput);
//    }

    @Test
    public void testHandleUserCreation_InvalidUrl() throws IOException {
        String urlPath = "/user/create";
        handleUserCreation(urlPath);
        String expectedOutput = "";
        String actualOutput = baos.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testRedirectPath() throws IOException {
        String redirectPath = "/home";
        redirectPath(redirectPath, dos);
        String expectedOutput = "HTTP/1.1 302 Found\r\nLocation: /home\r\n\r\n";
        String actualOutput = baos.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}
