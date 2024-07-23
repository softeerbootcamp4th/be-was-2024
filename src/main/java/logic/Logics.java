package logic;

import db.*;
import dto.HttpRequest;
import dto.HttpResponse;
import model.Article;
import model.Session;
import model.User;
import dto.enums.HttpMethod;
import dto.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ExtensionMapper;
import util.FileMapper;
import util.HttpRequestConverter;
import util.constant.StringConstants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static util.constant.StringConstants.*;

// 알맞은 HttpRequest 에 대해 로직을 처리하고 HttpResponse 를 반환
public class Logics {
    private static final Logger logger = LoggerFactory.getLogger(Logics.class);
    private static final Database<User, String> userDatabase = UserDB.getInstance();
    private static final Database<Session, String> sessionDatabase = SessionDB.getInstance();
    private static final Database<Article, Long> articleDatabase = ArticleDB.getInstance();

    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    public static HttpResponse index(HttpRequest httpRequest, String userId) throws IOException {

        String contentType = ExtensionMapper.getContentTypeFromRequestPath(httpRequest.getPath());
        byte[] body;
        try {
            body = FileMapper.getIndexPageByteConvertedFile(userId);
        } catch (IOException e) {
            return HttpResponse.clientError();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(StringConstants.HEADER_CONTENT_TYPE, contentType + SEMICOLON + HEADER_CHARSET_UTF_8);
        headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, body);
    }

    public static HttpResponse create(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }


        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());

        String userId = bodyToMap.get(USER_ID);
        String password = bodyToMap.get(PASSWORD);
        String name = bodyToMap.get(NAME);
        String email = bodyToMap.get(EMAIL);

        User user = new User(userId, password, name, email);
        userDatabase.save(user);

        return HttpResponse.redirectToMain();
        //TODO : body를 리턴하지 않아도 되는가
    }

    public static HttpResponse login(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }
        ///header
        Map<String, String> headers = new HashMap<>();

        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());
        String userId = bodyToMap.get(USER_ID);
        String password = bodyToMap.get(PASSWORD);

        User user = userDatabase.findById(userId).orElse(null);
        if (user == null) {
            return HttpResponse.clientError();
        }
        if (userId.equals(user.getUserId()) && password.equals(user.getPassword())) {
            headers.put("Location", "/index.html");
            logger.info("userId : " + userId + "Login successful");

            Session session = new Session(UUID.randomUUID().toString(), userId, EXPIRE_TIME);
            sessionDatabase.save(session);

            //TODO : TOHeaderString 메서드로 만들기
            headers.put("Set-Cookie", convertSessionIdToHeaderString(session.getSessionId()));

        } else {
            headers.put("Location", "/login_failed.html");
            logger.info("userId : " + userId + "Login failed");
        }
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);

    }

    public static HttpResponse logout(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }

        String sessionId = httpRequest.getSessionOrNull().orElse(null);
        if (sessionId != null) {
            Session session = sessionDatabase.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
            sessionDatabase.delete(session);
            logger.info(getLogoutString(session.getSessionId()));
        }

        ///header
        Map<String, String> headers = new HashMap<>();

        headers.put("Location", "/index.html");

        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
    }

    public static HttpResponse getUserList(HttpRequest httpRequest, String userId) throws IOException {
        if (userId == null) {
            return HttpResponse.redirectToMain();
        } else {
            List<Byte> body = new ArrayList<>();
            List<User> list = userDatabase.findAll().stream().toList();

            list.forEach(user -> {
                byte[] userBytes = user.toString().getBytes(StandardCharsets.UTF_8); // Use UTF-8 encoding
                for (byte b : userBytes) {
                    body.add(b);
                }
            });

            byte[] bodyArray = new byte[body.size()];
            for (int i = 0; i < body.size(); i++) {
                bodyArray[i] = body.get(i);
            }

            // Prepare headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Length", String.valueOf(bodyArray.length));
            headers.put("Content-Type", "text/plain; charset=UTF-8");

            return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, bodyArray);
        }
    }

    public static HttpResponse staticResponse(HttpRequest httpRequest, String userId) throws IOException {

        String contentType = ExtensionMapper.getContentTypeFromRequestPath(httpRequest.getPath());
        byte[] body;
        try {
            body = FileMapper.getByteConvertedFile(httpRequest.getPath(), userId);
        } catch (IOException e) {
            return HttpResponse.clientError();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(StringConstants.HEADER_CONTENT_TYPE, contentType + SEMICOLON + HEADER_CHARSET_UTF_8);
        headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, body);
    }

    public static HttpResponse createArticle(HttpRequest httpRequest, String userId) throws IOException {
        ///header
        Map<String, String> headers = new HashMap<>();

        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());
        String title = bodyToMap.get(TITLE);
        String content = bodyToMap.get(CONTENT);

        Article article = Article.of(title, content, Article.getArticleIndex());
        articleDatabase.save(article);

        headers.put("Location", "/index.html");

        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
    }

    public static HttpResponse readArticle(HttpRequest httpRequest) throws IOException {
        String contentType = "text/html";
        byte[] body;
        try {
            body = FileMapper.getArticlePageByteConvertedFile(httpRequest.getQueryParams());
        } catch (IOException e) {
            return HttpResponse.clientError();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(StringConstants.HEADER_CONTENT_TYPE, contentType + SEMICOLON + HEADER_CHARSET_UTF_8);
        headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.OK, headers, body);
    }
}
