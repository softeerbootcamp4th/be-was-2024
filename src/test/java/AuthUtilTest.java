import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AuthUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthUtilTest {

  private static final Logger logger = LoggerFactory.getLogger(AuthUtilTest.class);

  @Test
  void test() {
    Method[] declaredMethods = AuthUtil.class.getDeclaredMethods();
    List<String> methods = new ArrayList<>();

    for(Method method: declaredMethods) {
      String name = method.getName();
      methods.add(name);
      Class<?>[] parameterTypes = method.getParameterTypes();
      for(Class c: parameterTypes) {
        logger.debug(c.getName());
      }
    }

    assertThat(methods).contains("isLogin");
  }
}
