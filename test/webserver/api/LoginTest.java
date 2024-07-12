package webserver.api;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.login.LoginHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.session.Session;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {
    String userid;
    String password ;
    String name;
    String email;

    @BeforeEach
    void setUp() {
        //given
        userid = "testid";
        password = "testpassword";
        name = "testname";
        email = "testemail";
        Database.addUser(new User(userid, password, name, email));
    }

    @DisplayName("Login blankspace check test")
    @Test
    void testLogin() throws IOException {
        //given
        byte[] body = ("id=" + userid + "   &password=" + password).getBytes("UTF-8");
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
        String sid = response.getHeadersMap().get("Set-Cookie").split(";")[0].split("=")[1];
        assertNotNull(Session.getSession(sid));
    }


}