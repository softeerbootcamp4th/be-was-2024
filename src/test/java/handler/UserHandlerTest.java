package handler;

import db.Database;
import exception.ModelException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.ConstantUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserHandlerTest {

    UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = UserHandler.getInstance();
        Database.clearUsers();
    }

    @DisplayName("create: requestBody로부터 User 객체를 생성하고 필드값을 확인한다.")
    @ParameterizedTest(name = "userId: {0}, password: {1}, name: {2}, email: {3}")
    @CsvSource({
            "user1, pass1, John Doe, john.doe@example.com",
            "user2, pass2, Jane Smith, jane.smith@example.com",
            "user3, pass3, Alice Johnson, alice.johnson@example.com",
            "user4, pass4, Bob Brown, bob.brown@example.com",
            "user5, pass5, Charlie Davis, charlie.davis@example.com"
    })
    void create(String userId, String password, String name, String email) {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", password);
        params.put("name", name);
        params.put("email", email);

        // when
        User user = userHandler.create(params).get();

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @DisplayName("create: requestBody로부터 User 객체를 생성할 때 email이 유효하지 않으면 예외가 발생한다.")
    @ParameterizedTest(name = "userId: {0}, password: {1}, name: {2}, email: {3}")
    @CsvSource({
            "user1, pass1, John Doe, john.doeexample.com",
            "user2, pass2, Jane Smith, jane.smith@examplecom",
            "user3, pass3, Alice Johnson, alice.johnsonexample",
    })
    void createWithWrongEmail(String userId, String password, String name, String email) {
        // given
        Map<String, String> params = new HashMap<>(Map.of("userId", userId, "password", password, "name", name, "email", email));

        // when & then
        assertThatThrownBy(() -> userHandler.create(params))
                .isInstanceOf(ModelException.class)
                .hasMessage(ConstantUtil.INVALID_SIGNUP);
    }

    @DisplayName("create: 누락된 키가 존재할 때 예외가 발생해야 한다.")
    @Test
    void createWithInvalidParams() {
        // given
        Map<String, String> params = new HashMap<>(Map.of("userId", "userId", "password", "password", "name", "name"));

        // when & then
        assertThatThrownBy(() -> userHandler.create(params))
                .isInstanceOf(ModelException.class)
                .hasMessage(ConstantUtil.INVALID_SIGNUP);
    }

    @DisplayName("create: 키 중 빈 값이 존재할 때 예외가 발생해야 한다.")
    @Test
    void createWithEmptyFields() {
        // given
        Map<String, String> params = new HashMap<>(Map.of("userId", "userId", "password", "password", "name", "name", "email", ""));

        // when & then
        assertThatThrownBy(() -> userHandler.create(params))
                .isInstanceOf(ModelException.class)
                .hasMessage(ConstantUtil.INVALID_SIGNUP);
    }

    @DisplayName("findById: userId로 User를 찾아서 반환한다")
    @ParameterizedTest(name = "userId: {0}, password: {1}, name: {2}, email: {3}")
    @CsvSource({
            "user1, pass1, John Doe, john.doeexample.com",
            "user2, pass2, Jane Smith, jane.smith@examplecom",
            "user3, pass3, Alice Johnson, alice.johnsonexample",
    })
    void findById(String userId, String password, String name, String email){
        // given
        Database.addUser(User.from(Map.of("userId", userId, "password", password, "name", name, "email", email)));

        // when
        User user = userHandler.findById(userId).get();

        // then
        assertThat(user)
                .extracting(User::getUserId, User::getPassword, User::getName, User::getEmail)
                .containsExactly(userId, password, name, email);
    }

    @DisplayName("findById: userId로 User를 찾지 못하면 빈 Optional을 반환한다.")
    @Test
    void findByIdWithEmptyUser() {
        // when
        assertThat(userHandler.findById(" ")).isEmpty();
    }

    @DisplayName("findAll: 모든 User를 반환한다.")
    @Test
    void findAll() {
        // given
        Database.addUser(User.from(Map.of("userId", "user1", "password", "pass1", "name", "John Doe", "email", "email1")));
        Database.addUser(User.from(Map.of("userId", "user2", "password", "pass2", "name", "Jane Smith", "email", "email2")));

        // when
        List<User> users = userHandler.findAll();

        // then
        assertThat(users).hasSize(2);
        assertThat(users.get(0))
                .extracting(User::getUserId, User::getPassword, User::getName, User::getEmail)
                .containsExactly("user1", "pass1", "John Doe", "email1");
        assertThat(users.get(1))
                .extracting(User::getUserId, User::getPassword, User::getName, User::getEmail)
                .containsExactly("user2", "pass2", "Jane Smith", "email2");
    }

    @DisplayName("findAll: User가 없으면 빈 Collection을 반환한다.")
    @Test
    void findAllWithEmptyUsers() {
        // when
        List<User> users = userHandler.findAll();

        // then
        assertThat(users).isEmpty();
    }
}
