package webserver.mapping;

import webserver.annotation.LoginCheck;
import webserver.annotation.processor.LoginCheckProcessor;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.mapping.mapper.NotFoundMapper;
import webserver.mapping.mapper.get.*;
import webserver.mapping.mapper.post.CreateArticleMapper;
import webserver.mapping.mapper.post.CreateUserMapper;
import webserver.mapping.mapper.post.LoginUserMapper;
import webserver.mapping.mapper.post.LogoutUserMapper;

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
        getHandlers.put("/", new HomeMapper());
        getHandlers.put("/main", new HomeMapper());
        getHandlers.put("/registration", new RegistrationFormMapper());
        getHandlers.put("/login", new LoginFormMapper());
        getHandlers.put("/article", new ArticleFormMapper());
        getHandlers.put("/user/list", new UserListMapper());

        postHandlers.put("/user/create", new CreateUserMapper());
        postHandlers.put("/user/login", new LoginUserMapper());
        postHandlers.put("/user/logout", new LogoutUserMapper());
        postHandlers.put("/article/create", new CreateArticleMapper());
    }

    public MyHttpResponse mapping(MyHttpRequest httpRequest) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        HttpMapper mapper = switch (method) {
            case "GET" -> getHandlers.get(path) == null ? new NotFoundMapper() : getHandlers.get(path);
            case "POST" -> postHandlers.get(path) == null ? new NotFoundMapper() : postHandlers.get(path);
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
