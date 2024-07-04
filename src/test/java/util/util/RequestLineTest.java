package util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class RequestLineTest {


    @DisplayName("RequestLine을 분석해서 path를 파싱하는 테스트입니다.")
    @Test
    void path테스트()
    {
        //given
        String url ="GET /index.html HTTP/1.1";

        //when
        RequestLine requestLine = new RequestLine(url);

        //then
        assertThat(requestLine.getPath()).isEqualTo("src/main/resources/static/index.html");


    }

    @DisplayName("RequestLine을 분석해서 method를 파싱하는 테스트입니다")
    @Test
    void method테스트() {
        //given
        String url ="GET /index.html HTTP/1.1";

        //when
        RequestLine requestLine = new RequestLine(url);

        //then
        assertThat(requestLine.getMethod()).isEqualTo("GET");
    }

}