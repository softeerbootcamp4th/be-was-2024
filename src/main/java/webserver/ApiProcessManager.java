package webserver;

import ApiProcess.*;

import enums.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ApiPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiProcessManager {
    private static Logger logger = LoggerFactory.getLogger(ApiProcessManager.class);
    private static Map<ApiPair, ApiProcess> apiProcessStore = new ConcurrentHashMap<ApiPair, ApiProcess>();

    public ApiProcessManager() {
        // 로직 처리를 위한 구체 클래스를 설정한다. (RequestMapping에서 추상화 된 객체를 사용하기 위해 인터페이스 사용)
       apiProcessStore = new ConcurrentHashMap<ApiPair, ApiProcess>(){{
           put(new ApiPair("/", HttpMethod.GET), new HomepageApiProcess());
           put(new ApiPair("/registration", HttpMethod.GET), new RegisterpageApiProcess());
           put(new ApiPair("/user/create", HttpMethod.POST), new RegisterApiProcess());
           put(new ApiPair("/user/login", HttpMethod.POST), new LoginApiProcess());
           put(new ApiPair("/login", HttpMethod.GET), new LoginPageApiProcess());
           put(new ApiPair("/user/logout", HttpMethod.POST), new LogoutApiProcess());
       }};
    }

    public ApiProcess getApiProcess(String path, HttpMethod method) {
        int qmLoc = path.indexOf("/");
        String apiPath = path.substring(0, qmLoc);
        String lastPath = path.substring(qmLoc + 1);

        if(apiPath.isEmpty() && lastPath.contains(".")) {
           return new StaticApiProcess();
        }

        return apiProcessStore.getOrDefault(new ApiPair(path, method), new NotFoundApiProcess());
    }
}
