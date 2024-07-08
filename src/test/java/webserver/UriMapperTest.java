package webserver;

import data.HttpRequestMessage;
import db.Database;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class UriMapperTest {
//    @Test
//    @DisplayName("/create 테스트")
//    public void testCreate() {
//        //given
//        HashMap<String, String> queryParam = new HashMap<>();
//        queryParam.put("userId","abc");
//        queryParam.put("password","123");
//        queryParam.put("email","abc@gmail.com");
//        queryParam.put("name","abc");
//
//        //when
//        String mappedUri = UriMapper.mapUri();
//
//        //then
//        assertThat(mappedUri).isEqualTo("redirect:/index.html");
//    }

//    @Test
//    @DisplayName("/index.html 테스트")
//    public void testIndexPage() {
//        //given
//
//        //when
//        String mappedUri = UriMapper.mapUri("/index.html");
//
//        //then
//        assertThat(mappedUri).isEqualTo("src/main/resources/static/index.html");
//    }
//
//    @Test
//    @DisplayName("/registration.html 테스트")
//    public void testRegistrationPage() {
//        //given
//
//        //when
//        String mappedUri = UriMapper.mapUri("/registration.html");
//
//        //then
//        assertThat(mappedUri).isEqualTo("src/main/resources/static/registration/index.html");
//    }
}
