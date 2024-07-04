package url;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyURLTest {
    @Test
    @DisplayName("정상적인 파라미터 문자열이 들어오면 파라미터 맵 반환. 공백도 인식")
    void parseParameter_ReturnParamMapIfInputValid() {
        String input = "id=hello&password=world&sort=&token=hedfmod=";
        String expectedToId = "hello";
        String expectedToPassword = "world";
        String expectedToSort = "";
        String expectedToToken = "hedfmod=";

        Map<String, String> params = MyURL.parseParameter(input);

        Assertions.assertThat(params.get("id")).isEqualTo(expectedToId);
        Assertions.assertThat(params.get("password")).isEqualTo(expectedToPassword);
        Assertions.assertThat(params.get("sort")).isEqualTo(expectedToSort);
        Assertions.assertThat(params.get("token")).isEqualTo(expectedToToken);
    }

    @Test
    @DisplayName("정상적인 url 보낼 시 각 파트 분리해서 반환")
    void getUrlEachPart_returnEachPartsIfValid() {
        String input = "http://test.com:80/hello/world?token=hedfmod=";
        String expectedToProtocol = "http";
        String expectedToDomain = "test.com";
        String expectedToPort = "80";
        String expectedToPathName = "/hello/world";
        String expectedToQueryString = "token=hedfmod=";

        String[] parts = MyURL.getUrlEachPart(input);

        Assertions.assertThat(parts[0]).isEqualTo(expectedToProtocol);
        Assertions.assertThat(parts[1]).isEqualTo(expectedToDomain);
        Assertions.assertThat(parts[2]).isEqualTo(expectedToPort);
        Assertions.assertThat(parts[3]).isEqualTo(expectedToPathName);
        Assertions.assertThat(parts[4]).isEqualTo(expectedToQueryString);
    }

    @Test
    @DisplayName("없는 파트는 null 반환")
    void getUrlEachPart_returnEachParts_nullForEmptyPart() {
        String input1 = "test.com/hello/world";
        String expectedToDomain1 = "test.com";
        String expectedToPathName1 = "/hello/world";
        String[] parts1 = MyURL.getUrlEachPart(input1);

        Assertions.assertThat(parts1[0]).isNull();
        Assertions.assertThat(parts1[1]).isEqualTo(expectedToDomain1);
        Assertions.assertThat(parts1[2]).isNull();
        Assertions.assertThat(parts1[3]).isEqualTo(expectedToPathName1);
        Assertions.assertThat(parts1[4]).isNull();

        // domain도 없는 경우
        String input2 = "/hello/world?token=hedfmod=";
        String expectedToPathName2 = "/hello/world";
        String expectedToQueryString2 = "token=hedfmod=";
        String[] parts2 = MyURL.getUrlEachPart(input2);

        Assertions.assertThat(parts2[0]).isNull();
        Assertions.assertThat(parts2[1]).isNull();
        Assertions.assertThat(parts2[2]).isNull();
        Assertions.assertThat(parts2[3]).isEqualTo(expectedToPathName2);
        Assertions.assertThat(parts2[4]).isEqualTo(expectedToQueryString2);
    }

    @Test
    @DisplayName("static 파일의 경우 pathname으로 얻을 수 있음")
    void getUrlEachPart_CanGetStaticUrlByPathname() {
        String input = "/index.html";
        String expectedToPathname = "/index.html";

        // pathname은 3번째
        String[] parts = MyURL.getUrlEachPart(input);

        Assertions.assertThat(parts[3]).isEqualTo(expectedToPathname);
    }
}