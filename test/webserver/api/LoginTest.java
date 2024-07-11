package webserver.api;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.login.LoginHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @DisplayName("Login check test")
    @Test
    void testLogin() throws IOException {
        //given
        String userid = "testid";
        String password = "testpassword";
        String name = "testname";
        String email = "testemail";
        Database.addUser(new User(userid, password, name, email));
        StringBuilder bodyString = new StringBuilder().append("id=").append(userid).append("&password=").append(password);
        byte[] body = bodyString.toString().getBytes("UTF-8");
        HttpRequest request;
        request = new HttpRequest.ReqeustBuilder("POST /login HTTP/1.1")
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .build();

        //when
        HttpResponse response = new LoginHandler().function(request);

        //then
        assertEquals(StatusCode.CODE200, response.getStatusCode());
        assertNotNull(response.getHeadersMap().get("Set-Cookie"));
    }


}