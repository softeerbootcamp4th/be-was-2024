package util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class RequestObjectTest {


    @DisplayName("RequestLine을 분석해서 path를 파싱하는 테스트입니다.")
    @Test
    void path테스트()
    {
        //given
        String line ="GET /index.html HTTP/1.1";

        //when
        RequestObject requestLine = new RequestObject(line);

        //then
        assertThat(requestLine.getPath()).isEqualTo("/index.html");


    }

    @DisplayName("RequestLine을 분석해서 method를 파싱하는 테스트입니다")
    @Test
    void method테스트() {
        //given
        String line ="GET /index.html HTTP/1.1";

        //when
        RequestObject requestLine = new RequestObject(line);

        //then
        assertThat(requestLine.getMethod()).isEqualTo("GET");
    }

    @DisplayName("RequestLine을 분석해서 param를 파싱하는 테스트입니다")
    @Test
    void param테스트() {
        //given
        String line ="GET /user/create?userId=1&name=2&password=3&email=4 HTTP/1.1";
        Map<String,String> params = new HashMap<String,String>();

        //when
        RequestObject requestLine = new RequestObject(line);
        params=requestLine.getParams();

        //then
        assertThat(params.get("userId")).isEqualTo("1");
        assertThat(params.get("name")).isEqualTo("2");
        assertThat(params.get("password")).isEqualTo("3");
        assertThat(params.get("email")).isEqualTo("4");
    }

}