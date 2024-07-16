package webserver.api;

import model.user.User;
import model.user.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.pagehandler.UserListPageHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.session.SessionDAO;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {

    @DisplayName("test userlist function")
    @Test
    void testUserList() throws IOException {
        UserDAO userDAO = new UserDAO();
        SessionDAO sessionDAO = new SessionDAO();
        //given
        User user = userDAO.insertUser("testuser", "1", "test1", "asdfasdasdff");
        String sessionId = sessionDAO.insertSession(user.getUserId());
        assertNotNull(sessionDAO.findSession(sessionId));

        HttpRequest request;
        request = new HttpRequest.ReqeustBuilder("GET /user HTTP/1.1")
                .addHeader("Cookie", "sid=" + sessionId)
                .build();

        //when
        HttpResponse response = new UserListPageHandler().function(request);


        //then
        sessionDAO.deleteSession(sessionId);
        userDAO.deleteUser(user.getUserId());
        assertEquals(StatusCode.CODE200, response.getStatusCode());
        assertNotNull(response.getBody());

    }

}