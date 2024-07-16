package webserver;

import apiprocess.*;

import enums.HttpMethod;
import model.ApiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ApiPair;

import java.util.ArrayList;
import java.util.List;

public class ApiProcessManager {
    private List<ApiInfo> apiProcessStore;
    private static class LazyHolder {
        private static ApiProcessManager instance = new ApiProcessManager();
    }

    public static ApiProcessManager getInstance() {
        return LazyHolder.instance;
    }

    private ApiProcessManager() {
        apiProcessStore = new ArrayList<>();
        apiProcessStore.add(new ApiInfo("/", HttpMethod.GET, new HomepageApiProcess()));
        apiProcessStore.add(new ApiInfo("/registration", HttpMethod.GET, new RegisterpageApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/create", HttpMethod.POST, new RegisterApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/login", HttpMethod.POST, new LoginApiProcess()));
        apiProcessStore.add(new ApiInfo("/login", HttpMethod.GET, new LoginPageApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/logout", HttpMethod.POST, new LogoutApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/list", HttpMethod.GET, new UserlistPageApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/write", HttpMethod.GET, new WritePageApiProcess()));
        apiProcessStore.add(new ApiInfo("/user/write", HttpMethod.POST, new PostApiProcess()));
    }

    public ApiProcess get(String path, HttpMethod method) {
        int qmLoc = path.indexOf("/");
        String apiPath = path.substring(0, qmLoc);
        String lastPath = path.substring(qmLoc + 1);

        if(apiPath.isEmpty() && lastPath.contains(".")) {
            return new StaticApiProcess();
        }

        boolean isApiPathExist = false;

        for(ApiInfo apiInfo: apiProcessStore) {
            if(apiInfo.isApiPathNotSame(path)) {
                continue;
            }
            isApiPathExist = true;
            if(apiInfo.isMethodSame(method)) {
                return apiInfo.getApiProcess();
            }
        }

        return isApiPathExist ? new MethodNotAllowedApiProcess() : new NotFoundApiProcess();
    }
}
