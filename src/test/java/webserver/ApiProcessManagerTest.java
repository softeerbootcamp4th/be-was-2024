package webserver;

import ApiProcess.ApiProcess;
import ApiProcess.NotFoundApiProcess;
import ApiProcess.HomepageApiProcess;
import ApiProcess.RegisterpageApiProcess;
import ApiProcess.StaticApiProcess;

import enums.HttpMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiProcessManagerTest {

    @Test
    @DisplayName("서버가 처리할 수 없는 api 경로인 경우")
    void notFoundTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess apiProcess = apiProcessManager.getApiProcess("/notfound", HttpMethod.GET);

        // then
        assertThat(apiProcess).isInstanceOf(NotFoundApiProcess.class);
    }

    @Test
    @DisplayName("홈, 회원가입 페이지를 처리하는 로직 테스트")
    void pageApiTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess homePageApiProcess = apiProcessManager.getApiProcess("/", HttpMethod.GET);
        ApiProcess registerPageApiProcess = apiProcessManager.getApiProcess("/registration", HttpMethod.GET);

        // then
        assertThat(homePageApiProcess).isInstanceOf(HomepageApiProcess.class);
        assertThat(registerPageApiProcess).isInstanceOf(RegisterpageApiProcess.class);
    }

    @Test
    @DisplayName("정적 파일을 처리하는 로직 테스트")
    void StaticApiTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess apiProcess = apiProcessManager.getApiProcess("/main.css", HttpMethod.GET);

        // then
        assertThat(apiProcess).isInstanceOf(StaticApiProcess.class);
    }
}