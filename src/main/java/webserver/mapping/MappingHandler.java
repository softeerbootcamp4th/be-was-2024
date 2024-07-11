package webserver.mapping;

import webserver.annotation.LoginCheck;
import webserver.annotation.processor.LoginCheckProcessor;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MappingHandler {
    private static final Map<String, HttpMapper> getHandlers = new HashMap<>();
    private static final Map<String, HttpMapper> postHandlers = new HashMap<>();

    private static MappingHandler instance = new MappingHandler();

    private MappingHandler() {
    }

    public static MappingHandler getInstance() {
        return instance;
    }

    static {
        getHandlers.put("/", new GETHomeMapper());
        getHandlers.put("/registration", new GETRegistrationFormMapper());
        getHandlers.put("/login", new GETLoginFormMapper());
        getHandlers.put("/user/list", new GETUserListMapper());

        postHandlers.put("/user/create", new POSTCreateUserMapper());
        postHandlers.put("/user/login", new POSTLoginUserMapper());
        postHandlers.put("/user/logout", new POSTLogoutUserMapper());
    }

    public MyHttpResponse mapping(MyHttpRequest httpRequest) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        HttpMapper mapper = switch (method) {
            case "GET" -> getHandlers.get(path);
            case "POST" -> postHandlers.get(path);
            default -> new NotFoundMapper();
        };

        // 어노테이션 확인 및 처리
        if (mapper.getClass().isAnnotationPresent(LoginCheck.class)) {
            LoginCheckProcessor loginCheckProcessor = new LoginCheckProcessor();
            if (!loginCheckProcessor.isUserLoggedIn(httpRequest)) {
                return loginCheckProcessor.toLoginPage();
            }
        }

        return mapper.handle(httpRequest);
    }
}
