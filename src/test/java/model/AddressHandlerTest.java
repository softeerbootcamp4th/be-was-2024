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

    @Test
    public void testCreateUserFromUrl_InvalidUrl() {
        String urlPath = "/user/create";
        User actualUser = createUserFromUrl(urlPath);
        assertNull(actualUser);
    }

    @Test
    public void testHandleUserCreation_ValidUrl() throws IOException {
        String urlPath = "/user/create?name=John&email=john@example.com&password=1234&username=john123";
        handleUserCreation(urlPath, dos);
        String expectedOutput = "HTTP/1.1 302 Found\r\nLocation: /index.html\r\n\r\n";
        String actualOutput = baos.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHandleUserCreation_InvalidUrl() throws IOException {
        String urlPath = "/user/create";
        handleUserCreation(urlPath, dos);
        String expectedOutput = "";
        String actualOutput = baos.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}
