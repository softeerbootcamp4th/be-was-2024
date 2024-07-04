package webserver;

import ApiProcess.ApiProcess;
import ApiProcess.HomepageApiProcess;
import ApiProcess.RegisterpageApiProcess;
import ApiProcess.RegisterApiProcess;
import ApiProcess.StaticApiProcess;
import ApiProcess.NotFoundApiProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiProcessManager {
    private static Logger logger = LoggerFactory.getLogger(ApiProcessManager.class);
    private Map<String, ApiProcess> apiProcessStore;

    public ApiProcessManager() {
       apiProcessStore = new ConcurrentHashMap<String, ApiProcess>(){{
           put("/", new HomepageApiProcess());
           put("/registration", new RegisterpageApiProcess());
           put("/user/create", new RegisterApiProcess());
       }};
    }

    public ApiProcess getApiProcess(String path) {
        int qmLoc = path.indexOf("/");
        String apiPath = path.substring(0, qmLoc);
        File file = new File(RequestHandler.DEFALUT_PATH + path);

        logger.debug(apiPath);

        if(apiPath.isEmpty() && file.isFile()) {
           return new StaticApiProcess();
        }

        return apiProcessStore.getOrDefault(path, new NotFoundApiProcess());
    }
}
