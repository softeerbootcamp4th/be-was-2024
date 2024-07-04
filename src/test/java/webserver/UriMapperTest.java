package webserver;

import db.Database;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UriMapperTest {
    @Test
    @DisplayName("/create 테스트")
    public void testCreate() {
        //given
        UriMapper mapper = new UriMapper();

        //when
        String mappedUri = UriMapper.mapUri("/create?userId=abc12&email=aa@naver.com&name=이찬호&password=1234");

        //then
        assertThat(Database.findUserById("abc12")).isNotNull();
        assertThat(mappedUri).isEqualTo("redirect:/index.html");
    }

    @Test
    @DisplayName("/index.html 테스트")
    public void testIndexPage() {
        //given
        UriMapper mapper = new UriMapper();

        //when
        String mappedUri = UriMapper.mapUri("/index.html");

        //then
        assertThat(mappedUri).isEqualTo("src/main/resources/static/index.html");
    }

    @Test
    @DisplayName("/registration.html 테스트")
    public void testRegistrationPage() {
        //given

        //when
        String mappedUri = UriMapper.mapUri("/registration.html");

        //then
        assertThat(mappedUri).isEqualTo("src/main/resources/static/registration/index.html");
    }
}
