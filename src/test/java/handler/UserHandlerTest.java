package handler;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserHandlerTest {

    UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = UserHandler.getInstance();
    }

    @DisplayName("requestParams를 받아 User 객체를 생성하고 필드값을 확인한다.")
    @Test
    void createUser() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        params.put("password", "1234");
        params.put("name", "name");
        params.put("email", "abc@naver.com");

        // when
        User user = userHandler.create(params);

        // then
        assertThat(user.getUserId()).isEqualTo("1");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getEmail()).isEqualTo("abc@naver.com");
    }
}
