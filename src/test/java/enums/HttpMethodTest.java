package enums;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTest {

    @Test
    void getMethodTest() {
        // given
        String methodName = "GET";

        // when
        HttpMethod method = HttpMethod.from(methodName);

        //then
        Assertions.assertThat(method).isEqualTo(HttpMethod.GET);
    }

}