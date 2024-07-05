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
        // 로직 처리를 위한 구체 클래스를 설정한다. (RequestMapping에서 추상화 된 객체를 사용하기 위해 인터페이스 사용)
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
