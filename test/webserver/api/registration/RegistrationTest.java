package webserver.api.registration;

import model.user.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {
    String id;
    String username;
    String email, password;

    @BeforeEach
    void setUp() {
        id = "testid";
        username = "testusername";
        email = "testemail";
        password = "testpassword";
    }

    @DisplayName("test register function")
    @Test
    void register() throws IOException {
        //given
        UserDAO userDAO = new UserDAO();
        String body = "id=" + id + "&username=" + username + "&email=" + email + "&password=" + password;
        HttpRequest request = new HttpRequest.ReqeustBuilder("POST /registration HTTP/1.1")
                .addHeader("Content-Length", "34")
                .setBody(body.getBytes())
                .build();

        //when
        HttpResponse response  = Registration.getInstance().function(request);

        //then
        assertEquals(StatusCode.CODE302, response.getStatusCode());
        assertNotNull(userDAO.getUser(id));
        userDAO.deleteUser(id);

    }



}