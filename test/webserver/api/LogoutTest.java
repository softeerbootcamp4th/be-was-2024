package webserver.api;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.logout.LogoutHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.session.Session;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LogoutTest {

    @DisplayName("test logout function")
    @Test
    void testLogout() throws IOException {
        //given
        String userid = "testid";
        String password = "testpassword";
        String name = "testname";
        String email = "testemail";
        User user = new User(userid, password, name, email);
        Database.addUser(user);
        String sessionId = Session.createSession(user);

        assertNotNull(Session.getSession(sessionId));

        HttpRequest request;
        request = new HttpRequest.ReqeustBuilder("GET /logout HTTP/1.1")
                .addHeader("Cookie", "sid=" + sessionId)
                .build();

        //when
        HttpResponse response = new LogoutHandler().function(request);

        //then
        assertEquals(StatusCode.CODE302, response.getStatusCode());
        assertNull(Session.getSession(sessionId));
    }

}