package handler;

import db.ArticleDatabase;
import db.UserDatabase;
import db.SessionDatabase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.Article;
import model.User;
import util.TemplateEngine;
import util.Utils.ResponseWithStatus;

import java.io.IOException;
import java.util.*;

import static db.SessionDatabase.getUser;
import static db.SessionDatabase.isLogin;
import static util.Constants.*;
import static util.Utils.*;

public class GetHandler {
    private static final ArticleDatabase articleDatabase = new ArticleDatabase();
    private static final UserDatabase userDatabase = new UserDatabase();

    public static HttpResponse serveStaticFile(String requestUrl) throws IOException {
        String[] tokens = requestUrl.split(REG_DOT);
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(STATIC_PATH + requestUrl);

        byte[] body = responseWithStatus.body;

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader(CONTENT_TYPE, getContentType(type))
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse serveDynamicFile(String requestUrl, Map<String, String> data) throws IOException {
        String[] tokens = requestUrl.split(REG_DOT);
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(STATIC_PATH + requestUrl);
        byte[] body = TemplateEngine.renderTemplate(responseWithStatus.body, data);

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader(CONTENT_TYPE, getContentType(type))
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse serveRootPage(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        Map<String, String> data = new HashMap<>();
        if (!isLogin(sid)) {
            data.put(USER_NAME, "");
            data.put("msg", "");
            data.put("LoginOrArticle", "/login");
            data.put("loginButton", "로그인");
            data.put("RegistrationOrLogout", "/registration");
            data.put("registrationButton", "회원가입");
        } else {
            String userId = getUser(sid);
            Optional<User> user = userDatabase.findUserById(userId);
            data.put(USER_NAME, user.get().getName());
            data.put("msg", "님, 환영합니다.");
            data.put("LoginOrArticle", "/article");
            data.put("loginButton", "글쓰기");
            data.put("RegistrationOrLogout", "/logout");
            data.put("registrationButton", "로그아웃");
        }
        return serveDynamicFile(FILE_INDEX, data);

    }

    public static HttpResponse logout(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        SessionDatabase.deleteSession(sid);

        byte[] body = new byte[0];

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader(LOCATION, PATH_ROOT)
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse getUserList(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        if (!isLogin(sid)) {
            return new HttpResponse()
                    .addStatus(HttpStatus.FOUND)
                    .addHeader(LOCATION, PATH_ROOT)
                    .addHeader(CONTENT_TYPE, TEXT_HTML)
                    .addBody(new byte[0]);
        } else {
            Collection<User> users = userDatabase.findAll();

            StringBuilder userList = new StringBuilder();
            for (User user : users) {
                userList.append(TABLE_ROW_START);
                userList.append(TABLE_DATA_START).append(user.getUserId()).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(user.getName()).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(user.getEmail()).append(TABLE_DATA_END);
                userList.append(TABLE_ROW_END);
            }

            Map<String, String> data = new HashMap<>();
            data.put(USER_LIST, userList.toString());
            data.put(USER_NAME, userDatabase.findUserById(SessionDatabase.getUser(sid)).get().getName());
            return serveDynamicFile(PATH_USER + FILE_USER_LIST, data);
        }
    }

    public static HttpResponse postArticle(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);
        String userId = getUser(sid);

        if (!isLogin(sid)) {
            return serveStaticFile(PATH_LOGIN + FILE_INDEX);
        } else {
            HashMap<String, String> data = new HashMap<>();
            data.put("userName", userDatabase.findUserById(userId).get().getName());
            data.put("msg", "님, 환영합니다.");

            return serveDynamicFile(PATH_ARTICLE + FILE_INDEX, data);
        }
    }

    public static HttpResponse getAllArticles(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        HashMap<String, String> data = new HashMap<>();
        if (!isLogin(sid)) {
            data.put(USER_NAME, "");
            data.put("msg", "");
            data.put("LoginOrArticle", "/login");
            data.put("loginButton", "로그인");
            data.put("RegistrationOrLogout", "/registration");
            data.put("registrationButton", "회원가입");
        } else {
            String userId = getUser(sid);
            Optional<User> user = userDatabase.findUserById(userId);
            data.put(USER_NAME, user.get().getName());
            data.put("msg", "님, 환영합니다.");
            data.put("LoginOrArticle", "/article");
            data.put("loginButton", "글쓰기");
            data.put("RegistrationOrLogout", "/logout");
            data.put("registrationButton", "로그아웃");
        }

        List<Article> articles = articleDatabase.getAllArticles();
        StringBuilder articleList = new StringBuilder();

        for (Article article : articles) {
            articleList.append(TABLE_ROW_START);
            articleList.append(TABLE_DATA_START).append(article.getArticleId()).append(TABLE_DATA_END);
            articleList.append(TABLE_DATA_START).append(userDatabase.findUserById(article.getUserId()).get().getName()).append(TABLE_DATA_END);
            articleList.append(TABLE_DATA_START).append(article.getText()).append(TABLE_DATA_END);
            articleList.append(TABLE_ROW_END);
        }

        data.put("articleList", articleList.toString());

        return serveDynamicFile(PATH_ARTICLE + FILE_ARTICLE_LIST, data);
    }
}
