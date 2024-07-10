package webserver.http;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static plugin.UserPluginTest.createTestUser;

class SessionTest {

    @Test
    @DisplayName("서버는 세션 아이디에 해당하는 User 정보에 접근할 수 있어야 한다.")
    public void testGetUserWithSessionId(){
        User user = createTestUser();
        String sessionId = Session.save(user);

        User findUser = Session.get(sessionId);

        assertEquals(findUser, user);
    }

}