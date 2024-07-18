package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.RequestLineUtil;

class RequestLineUtilTest {
    RequestLineUtil requestLineUtil = new RequestLineUtil();
    String line = "GET /index.html HTTP/1.1";

    @Test
    @DisplayName("get url 테스트")
    void testGetURL() {
        String url = requestLineUtil.getURL(line);
        Assertions.assertThat(url).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("http 메소드 테스트")
    void testGetHttpMethod() {
        String method = requestLineUtil.getHttpMethod(line);
        Assertions.assertThat(method).isEqualTo("GET");
    }
}