package handler;

import db.Database;
import model.Session;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SessionHandlerTest {

    SessionHandler sessionHandler;

    @BeforeEach
    void setUp() {
        sessionHandler = SessionHandler.getInstance();
    }

    @DisplayName("login: 성공한다면 세션을 생성한다.")
    @ParameterizedTest(name = "Test {index} => userId={0}, password={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void login(String userId, String password) {
        // given
        Map<String, String> field = new HashMap<>();
        field.put("userId", userId);
        field.put("password", password);
        field.put("name", "name");
        field.put("email", "email");
        Database.addUser(User.from(field));

        // when
        Optional<Session> session = sessionHandler.login(Map.of("userId", userId, "password", password));

        // then
        assertThat(session).isPresent();
    }

    @DisplayName("login: 실패하면 세션을 생성하지 않으며 빈 Optional을 반환한다.")
    @ParameterizedTest(name = "Test {index} => userId={0}, password={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void login_fail(String userId, String password) {
        // given
        Map<String, String> field = new HashMap<>();
        field.put("userId", userId);
        field.put("password", password);
        field.put("name", "name");
        field.put("email", "email");
        Database.addUser(User.from(field));

        // when
        Optional<Session> session = sessionHandler.login(Map.of("userId", userId, "password", "wrong_password"));

        // then
        assertThat(session).isEmpty();
    }

    @DisplayName("logout: 세션을 삭제한다.")
    @ParameterizedTest(name = "Test {index} => sessionId={0}, userId={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void logout(String sessionId, String userId) {
        // given
        Session session = new Session(sessionId, userId);
        Database.addSession(session);

        // when
        sessionHandler.logout(session.getSessionId());

        // then
        assertThat(Database.findSessionById(session.getSessionId())).isEmpty();
    }

    @DisplayName("parseSessionId: 세션 쿠키에서 세션 아이디를 추출한다.")
    @ParameterizedTest(name = "Test {index} => cookie={0}, expected={1}")
    @CsvSource({
            "sid=1234; userId=1234, 1234",
            "sid=abcd; userId=1234; name=1234, abcd",
            "sid=5678; userId=abcd; name=1234; email=1234, 5678",
            "userId=1234; sid=efgh; name=abcd, efgh",
            "name=abcd; sid=ijkl; email=1234, ijkl",
            "userId=abcd; name=1234; sid=mnop, mnop",
            "sid=qrst, qrst",
            "sid=uvwx; sid=1234, uvwx",
            "userId=abcd; name=1234; sid=5678; email=1234; sid=91011, 5678"
    })
    void parseSessionId(String cookie, String expected) {
        // when
        String sessionId = sessionHandler.parseSessionId(cookie).orElse(null);

        // then
        assertThat(sessionId).isEqualTo(expected);
    }

    @DisplayName("parseSessionId: 세션 쿠키가 sid를 포함하지 않으면 빈 Optional을 반환한다.")
    @ParameterizedTest(name = "Test {index} => cookie={0}")
    @ValueSource(strings = {
            "userId=1234",
            "name=1234",
            "email=1234",
            "userId=1234; name=1234",
            "userId=1234; email=1234",
            "name=1234; email=1234",
            "userId=1234; name=1234; email=1234",
    })
    void parseSessionId_no_sid(String cookie) {
        // when
        Optional<String> sessionId = sessionHandler.parseSessionId(cookie);

        // then
        assertThat(sessionId).isEmpty();
    }

    @DisplayName("parseSessionId: 세션 쿠키가 null이면 빈 Optional을 반환한다.")
    @Test
    void parseSessionId_null() {
        // when
        Optional<String> sessionId = sessionHandler.parseSessionId(null);

        // then
        assertThat(sessionId).isEmpty();
    }

    @DisplayName("parseSessionId: 세션 쿠키가 공백이라면 빈 Optional을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   ", "    "})
    void parseSessionId_empty(String blank) {
        // when
        Optional<String> sessionId = sessionHandler.parseSessionId(blank);

        // then
        assertThat(sessionId).isEmpty();
    }
}
