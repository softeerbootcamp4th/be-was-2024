package http.utils;

import http.enums.MIMEType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MimeTypeUtilTest {
    @Test
    @DisplayName("enum에 포함된 'html' 문자열 전달 시 MIMEType.html 반환")
    void getMimeType_returnMIMEType_html() {
        String input = "html";

        MIMEType httpMethodType = MimeTypeUtil.getMimeType(input);

        Assertions.assertThat(httpMethodType).isEqualTo(MIMEType.html);
    }

    @Test
    @DisplayName("스펠링에 대문자가 포함되도 정상적으로 MIMEType.html 반환")
    void getMimeType_returnMIMEType_html_EvenIfSpellingIncludesUppercase() {
        String input = "HtmL";

        MIMEType httpMethodType = MimeTypeUtil.getMimeType(input);

        Assertions.assertThat(httpMethodType).isEqualTo(MIMEType.html);
    }

    @Test
    @DisplayName("MIMEType에 등록되어 있지 않은 문자열 입력 시 예외 반환")
    void getMimeType_throwExceptionIfInputIsInvalidHttpMethod() {
        String input = "invalid";

        Assertions.assertThatThrownBy(
                () -> MimeTypeUtil.getMimeType(input)
        );
    }

    @Test
    @DisplayName("null 입력 시에도 예외 반환")
    void getMimeType_throwExceptionIfInputIsNull() {
        String input = null;

        Assertions.assertThatThrownBy(
                () -> MimeTypeUtil.getMimeType(input)
        );
    }
}