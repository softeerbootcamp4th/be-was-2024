package webserver;

import ApiProcess.ApiProcess;
import ApiProcess.NotFoundApiProcess;
import ApiProcess.HomepageApiProcess;
import ApiProcess.RegisterpageApiProcess;
import ApiProcess.StaticApiProcess;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiProcessManagerTest {

    @Test
    void notFoundTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess apiProcess = apiProcessManager.getApiProcess("/notfound");

        // then
        assertThat(apiProcess).isInstanceOf(NotFoundApiProcess.class);
    }

    @Test
    void pageApiTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess homePageApiProcess = apiProcessManager.getApiProcess("/");
        ApiProcess registerPageApiProcess = apiProcessManager.getApiProcess("/registration");

        // then
        assertThat(homePageApiProcess).isInstanceOf(HomepageApiProcess.class);
        assertThat(registerPageApiProcess).isInstanceOf(RegisterpageApiProcess.class);
    }

    @Test
    void StaticApiTest() {
        // given
        ApiProcessManager apiProcessManager = new ApiProcessManager();

        // when
        ApiProcess apiProcess = apiProcessManager.getApiProcess("/main.css");

        // then
        assertThat(apiProcess).isInstanceOf(StaticApiProcess.class);
    }
}