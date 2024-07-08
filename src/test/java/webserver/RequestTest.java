package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RequestTest {
    @Test
    @DisplayName("GET과 query가 포함된 path가 들어왔을 때 isQueryString()이 정상적으로 동작하는지 테스트")
    void isQueryStringTest() {
        // given
        String qeury = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        // when
        Request request = new Request(qeury);

        // then
        assertThat(request.isQueryString()).isEqualTo(true);
    }

    @Test
    @DisplayName("GET과 qeury가 포함된 path가 들어왓을 때 path와 query string이 정상적으로 분리되는지 테스트")
    void splitPathAndQeuryStringTest() {
        // given
        String qeury = "GET /user/create?userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        // when
        Request request = new Request(qeury);

        // then
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getQueryString()).isEqualTo("userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com");
    }

}