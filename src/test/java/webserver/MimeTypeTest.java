package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MimeTypeTest {
    @Test
    void ordinalTest() {
        Assertions.assertThat(MimeType.CSS.ordinal()).isEqualTo(1);
    }
}