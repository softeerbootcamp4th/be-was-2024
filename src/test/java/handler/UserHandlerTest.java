package handler;

import exception.ModelException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserHandlerTest {

    UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = UserHandler.getInstance();
    }

    @DisplayName("requestBody로부터 User 객체를 생성하고 필드값을 확인한다.")
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
        User user = userHandler.create(params);

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @DisplayName("누락된 키가 존재할 때 예외가 발생해야 한다.")
    @Test
    void createWithInvalidParams() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        params.put("password", "1234");
        params.put("name", "name");

        // when & then
        assertThatThrownBy(() -> userHandler.create(params))
                .isInstanceOf(ModelException.class)
                .hasMessage(StringUtil.INVALID_LOGIN);
    }

    @DisplayName("키 중 빈 값이 존재할 때 예외가 발생해야 한다.")
    @Test
    void createWithEmptyFields() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("userId", "userId");
        params.put("password", "password");
        params.put("name", "name");
        params.put("email", "");

        // when & then
        assertThatThrownBy(() -> userHandler.create(params))
                .isInstanceOf(ModelException.class)
                .hasMessage(StringUtil.INVALID_LOGIN);
    }
}
