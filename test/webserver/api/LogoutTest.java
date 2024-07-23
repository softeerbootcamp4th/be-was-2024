package webserver.api;

import model.user.User;
import model.user.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.logout.LogoutHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.session.SessionDAO;

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
        SessionDAO sessionDAO = new SessionDAO();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.insertUser(userid, name, email, password);
        String sessionId = sessionDAO.insertSession(user.getUserId());

        assertNotNull(sessionDAO.findSession(sessionId));

        HttpRequest request;
        request = new HttpRequest.ReqeustBuilder("GET /logout HTTP/1.1")
                .addHeader("Cookie", "sid=" + sessionId)
                .build();

        //when
        HttpResponse response = new LogoutHandler().function(request);

        //then
        assertEquals(StatusCode.CODE302, response.getStatusCode());
        assertNull(sessionDAO.findSession(sessionId));
        userDAO.deleteUser(userid);
    }

}