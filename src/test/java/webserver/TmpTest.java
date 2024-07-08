package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TmpTest {
  @Test
  void test() {
    String k = "asdfasdf\r\n asdfasdfasdfasd\r\n sdfasdfasdf \r\n\r\n";
    String[] split = k.split("\r\n");
    Assertions.assertThat(split.length).isEqualTo(4);
  }
}
