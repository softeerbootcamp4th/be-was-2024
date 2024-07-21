package webserver.mapping;

import webserver.annotation.LoginCheck;
import webserver.annotation.processor.LoginCheckProcessor;
import webserver.enums.HttpMethod;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.mapping.mapper.get.*;
import webserver.mapping.mapper.post.CreateArticleMapper;
import webserver.mapping.mapper.post.CreateUserMapper;
import webserver.mapping.mapper.post.LoginUserMapper;
import webserver.mapping.mapper.post.LogoutUserMapper;
import webserver.util.UrlTrie;

import java.io.IOException;
import java.sql.SQLException;

public class MappingHandler {
    private static final UrlTrie urlTrie = new UrlTrie();

    private static MappingHandler instance = new MappingHandler();

    private MappingHandler() {
    }

    public static MappingHandler getInstance() {
        return instance;
    }

    static {
        urlTrie.insert("/", HttpMethod.GET, new HomeMapper());
        urlTrie.insert("/main", HttpMethod.GET, new HomeMapper());
        urlTrie.insert("/registration", HttpMethod.GET, new RegistrationFormMapper());
        urlTrie.insert("/login", HttpMethod.GET, new LoginFormMapper());
        urlTrie.insert("/user/list", HttpMethod.GET, new UserListMapper());
        urlTrie.insert("/article", HttpMethod.GET, new ArticleFormMapper());
        urlTrie.insert("/article/{articleId}", HttpMethod.GET, new ArticleDetailMapper());

        urlTrie.insert("/user/create", HttpMethod.POST, new CreateUserMapper());
        urlTrie.insert("/user/login", HttpMethod.POST, new LoginUserMapper());
        urlTrie.insert("/user/logout", HttpMethod.POST, new LogoutUserMapper());
        urlTrie.insert("/article/create", HttpMethod.POST, new CreateArticleMapper());
    }

    public MyHttpResponse mapping(MyHttpRequest httpRequest) throws IOException, SQLException {
        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        HttpMapper mapper = urlTrie.search(path, method);

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
