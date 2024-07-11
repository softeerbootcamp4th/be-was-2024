package db;

import auth.Session;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

  Session seseion = Session.getInstance();

  @AfterEach
  void afterEach() {
    seseion.clear();
  }
  @Test
  @DisplayName("유저가 제대로 저장되는지 확인")
  void storeTest() {
    // given
    User user = new User("minjun05", "1234", "minjun", "alswnssl0528@naver.com");

    // when
    String uuid = UUID.randomUUID().toString();
    seseion.insert(uuid, user);

    // then
    User sessionUser = seseion.get(uuid);
    assertThat(sessionUser).isNotNull();
    assertThat(sessionUser.getUserId()).isEqualTo("minjun05");
    assertThat(sessionUser.getPassword()).isEqualTo("1234");
    assertThat(sessionUser.getName()).isEqualTo("minjun");
    assertThat(sessionUser.getEmail()).isEqualTo("alswnssl0528@naver.com");
  }

}