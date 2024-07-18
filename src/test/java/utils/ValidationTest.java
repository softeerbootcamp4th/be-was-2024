package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(result1).isFalse();
    assertThat(result2).isTrue();
    assertThat(result3).isTrue();
  }

  @Test
  @DisplayName("이메일 형식 확인 테스트 - 알맞은 형식")
  void emailPatternCheckTest() {
    // given
    String email = "alswnssl0528@naver.com" ;
    String emailWithUnderBar = "violet_prog@gmail.com";

    // when
    boolean emailCheck = Validation.isEmail(email);
    boolean emailWithUnderBarCheck = Validation.isEmail(emailWithUnderBar);

    // then
    assertThat(emailCheck).isTrue();
    assertThat(emailWithUnderBarCheck).isTrue();
  }

  @Test
  @DisplayName("이메일 형식 확인 테스트 - 알맞지 않은 형식")
  void emailPatternNotCorrectTest() {
    // given
    String notCorrectEmail1 = "alswnssl0528!navercom";
    String notCorrectEmail2 = "minjun@34dfsadf";
    String notCorrectEmail3 = "minjun@@34dfsadf.com";

    // when
    boolean check1 = Validation.isEmail(notCorrectEmail1);
    boolean check2 = Validation.isEmail(notCorrectEmail2);
    boolean check3 = Validation.isEmail(notCorrectEmail3);

    // then
    assertThat(check1).isFalse();
    assertThat(check2).isFalse();
    assertThat(check3).isFalse();
  }

}