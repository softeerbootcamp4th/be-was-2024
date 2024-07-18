package webserver;

import apiprocess.ApiProcess;
import apiprocess.NotFoundApiProcess;
import apiprocess.HomepageApiProcess;
import apiprocess.RegisterpageApiProcess;
import apiprocess.StaticApiProcess;

import enums.HttpMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiProcessManagerTest {

    ApiProcessManager apiProcessManager = ApiProcessManager.getInstance();

    @Test
    @DisplayName("서버가 처리할 수 없는 api 경로인 경우")
    void notFoundTest() {

        // when
        ApiProcess apiProcess = apiProcessManager.get("/notfound", HttpMethod.GET);

        // then
        assertThat(apiProcess).isInstanceOf(NotFoundApiProcess.class);
    }

    @Test
    @DisplayName("홈, 회원가입 페이지를 처리하는 로직 테스트")
    void pageApiTest() {
        // given

        // when
        ApiProcess homePageApiProcess = apiProcessManager.get("/", HttpMethod.GET);
        ApiProcess registerPageApiProcess = apiProcessManager.get("/registration", HttpMethod.GET);

        // then
        assertThat(homePageApiProcess).isInstanceOf(HomepageApiProcess.class);
        assertThat(registerPageApiProcess).isInstanceOf(RegisterpageApiProcess.class);
    }

    @Test
    @DisplayName("정적 파일을 처리하는 로직 테스트")
    void StaticApiTest() {
        // given

        // when
        ApiProcess apiProcess = apiProcessManager.get("/main.css", HttpMethod.GET);

        // then
        assertThat(apiProcess).isInstanceOf(StaticApiProcess.class);
    }
}