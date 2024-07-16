package webserver.api;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.api.pagehandler.UserListPageHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.session.Session;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {

    @DisplayName("test userlist function")
    @Test
    void testUserList() throws IOException {
        //given
        User user = new User("a", "1", "test1", "");
        Database.addUser(user);
        String sessionId = Session.createSession(user);
        assertNotNull(Session.getSession(sessionId));

        HttpRequest request;
        request = new HttpRequest.ReqeustBuilder("GET /user HTTP/1.1")
                .addHeader("Cookie", "sid=" + sessionId)
                .build();

        //when
        HttpResponse response = new UserListPageHandler().function(request);


        //then
        assertEquals(StatusCode.CODE200, response.getStatusCode());
        assertNotNull(response.getBody());

    }

}