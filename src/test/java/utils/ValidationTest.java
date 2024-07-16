package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationTest {

  @Test
  @DisplayName("모든 문자열이 정상적으로 들어왔을 때")
  void validCheckTest() {
    // given
    String a = "j";
    String b = "v";
    String c = "m";
    String d = null;
    String e = "";

    // when
    boolean result1 = Validation.anyNull(a, b, c);
    boolean result2 = Validation.anyNull(a, b, d);
    boolean result3 = Validation.anyNull(a, b, c, d);

    // then
    Assertions.assertThat(result1).isFalse();
    Assertions.assertThat(result2).isTrue();
    Assertions.assertThat(result3).isTrue();
  }

}