package util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UrlMapperTest {

    @Test
    void testMapUrl() {
        UrlMapper mapper = new UrlMapper();

        assertThat(mapper.mapUrl("/")).isEqualTo("/index.html");
        assertThat(mapper.mapUrl("/register.html")).isEqualTo("/registration/index.html");
        assertThat(mapper.mapUrl("/login.html")).isEqualTo("/login/index.html");
        assertThat(mapper.mapUrl("/unmapped.html")).isEqualTo("/unmapped.html");
    }
}