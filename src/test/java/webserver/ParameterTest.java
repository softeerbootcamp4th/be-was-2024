package webserver;

import org.junit.jupiter.api.Test;
import util.Parser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest {

    @Test
    void get() {

        //given
        Map<String, String> parameter = Parser.parseParameterMap("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");

        //when
        String userIdValue = parameter.get("userId");

        //then
        assertEquals("javajigi", userIdValue);

    }
}