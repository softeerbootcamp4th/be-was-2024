package http.utils;

import http.enums.HttpMethodType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTypeUtilTest {
    @Test
    @DisplayName("정상적인 GET 문자열 입력 시 대응되는 HttpMethodType.GET 반환")
    void getHttpMethodType_returnHttpMethodType_GET() {
        String input = "GET";

        HttpMethodType httpMethodType = HttpMethodTypeUtil.getHttpMethodType(input);

        Assertions.assertThat(httpMethodType).isEqualTo(HttpMethodType.GET);
    }

    @Test
    @DisplayName("GET 스펠링에 소문자가 포함되도 정상적으로 HttpMethod.GET 반환")
    void getHttpMethodType_returnHttpMethodType_GET_EvenIfSpellingIncludesLowercase() {
        String input = "get";

        HttpMethodType httpMethodType = HttpMethodTypeUtil.getHttpMethodType(input);

        Assertions.assertThat(httpMethodType).isEqualTo(HttpMethodType.GET);
    }

    @Test
    @DisplayName("Http Method에 없는 문자열 입력 시 예외 반환")
    void getHttpMethodType_throwExceptionIfInputIsInvalidHttpMethod() {
        String input = "invalid";

        Assertions.assertThatThrownBy(
                () -> HttpMethodTypeUtil.getHttpMethodType(input)
        );
    }

    @Test
    @DisplayName("null 입력 시에도 예외 반환")
    void getHttpMethodType_throwExceptionIfInputIsNull() {
        String input = null;

        Assertions.assertThatThrownBy(
                () -> HttpMethodTypeUtil.getHttpMethodType(input)
        );
    }
}