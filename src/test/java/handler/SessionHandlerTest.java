package handler;

import db.Database;
import db.SessionDatabase;
import session.Session;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import session.SessionHandler;
import util.ConstantUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SessionHandlerTest {

    SessionHandler sessionHandler;

    @BeforeEach
    void setUp() {
        sessionHandler = SessionHandler.getInstance();
        Database.clearUsers();
    }

    private Map<String, String> createFields(String userId, String password) {
        return new HashMap<>
                (Map.of(ConstantUtil.USER_ID, userId, ConstantUtil.PASSWORD, password, ConstantUtil.NAME, "name", ConstantUtil.EMAIL, "email"));
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
        Map<String, String> fields = createFields(userId, password);
        Database.addUser(User.from(fields));

        // when
        Optional<Session> session = sessionHandler.login(Map.of(ConstantUtil.USER_ID, userId, ConstantUtil.PASSWORD, password));

        // then
        assertThat(session).isPresent();
        assertThat(session.toString()).hasToString(SessionDatabase.findSessionById(session.get().getSessionId()).toString());
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
        Map<String, String> fields = createFields(userId, password);
        Database.addUser(User.from(fields));

        // when
        Optional<Session> session = sessionHandler.login(Map.of(ConstantUtil.USER_ID, userId, ConstantUtil.PASSWORD, "wrong_password"));

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
        Session session = new Session(sessionId, userId, LocalDateTime.now());
        SessionDatabase.addSession(session);

        // when
        sessionHandler.logout(session.getSessionId());

        // then
        assertThat(SessionDatabase.findSessionById(session.getSessionId())).isEmpty();
    }

    @DisplayName("findSessionById: 세션을 찾는다.")
    @ParameterizedTest(name = "Test {index} => sessionId={0}, userId={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void findSessionById(String sessionId, String userId) {
        // given
        Session session = new Session(sessionId, userId, LocalDateTime.now(ZoneId.of("GMT")));
        SessionDatabase.addSession(session);

        // when
        Optional<Session> foundSession = sessionHandler.findSessionById(session.getSessionId());

        // then
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().toString()).hasToString(session.toString());
    }

    @DisplayName("findSessionById: 세션을 찾지 못하면 빈 Optional을 반환한다.")
    @Test
    void findSessionById_fail(){
        // when
        Optional<Session> foundSession = sessionHandler.findSessionById("1");

        // then
        assertThat(foundSession).isEmpty();
    }

    @DisplayName("validateSession: 세션이 유효하면 true를 반환한다.")
    @ParameterizedTest(name = "Test {index} => sessionId={0}, userId={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void validateSession(String sessionId, String userId) {
        // given
        Session session = new Session(sessionId, userId, LocalDateTime.now(ZoneId.of("GMT")));
        SessionDatabase.addSession(session);

        // when
        boolean isValid = sessionHandler.validateSession(session);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("validateSession: 세션이 만료되면 false를 반환한다.")
    @ParameterizedTest(name = "Test {index} => sessionId={0}, userId={1}")
    @CsvSource({
            "qdlqkwj, trekjel",
            "eaklajw123, admekvv",
            "892jlkdaw, adklwlkwad"
    })
    void validateSession_expired(String sessionId, String userId) {
        // given
        Session session = new Session(sessionId, userId, LocalDateTime.now(ZoneId.of("GMT")).minusMinutes(31));
        SessionDatabase.addSession(session);

        // when
        boolean isValid = sessionHandler.validateSession(session);

        // then
        assertThat(isValid).isFalse();
    }
}
