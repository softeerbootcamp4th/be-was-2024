package handler;

import db.ArticleDatabase;
import db.UserDatabase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.Article;
import model.User;
import util.TemplateEngine;
import util.Utils.ResponseWithStatus;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

import static db.SessionDatabase.*;
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
            data.put(USER_NAME, URLDecoder.decode(user.get().getName(), "UTF-8"));
            data.put("msg", "님, 환영합니다.");
            data.put("LoginOrArticle", "/article");
            data.put("loginButton", "글쓰기");
            data.put("RegistrationOrLogout", "/logout");
            data.put("registrationButton", "로그아웃");
        }

        data.put("article", getAllArticles());
        return serveDynamicFile(FILE_INDEX, data);

    }

    public static HttpResponse logout(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        deleteSession(sid);

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
                    .addHeader(LOCATION, PATH_LOGIN)
                    .addHeader(CONTENT_TYPE, TEXT_HTML)
                    .addBody(new byte[0]);
        } else {
            Collection<User> users = userDatabase.findAll();

            StringBuilder userList = new StringBuilder();
            for (User user : users) {
                userList.append(TABLE_ROW_START);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getUserId(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getName(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getEmail(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_ROW_END);
            }

            Map<String, String> data = new HashMap<>();
            data.put(USER_LIST, userList.toString());
            data.put(USER_NAME, userDatabase.findUserById(getUser(sid)).get().getName());
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
            data.put("userName", URLDecoder.decode(userDatabase.findUserById(userId).get().getName(), "UTF-8"));
            data.put("msg", "님, 환영합니다.");

            return serveDynamicFile(PATH_ARTICLE + FILE_INDEX, data);
        }
    }

    public static String getAllArticles() throws IOException {
        StringBuilder articles = new StringBuilder();
        List<Article> allArticles = articleDatabase.getAllArticles();
        for (Article article : allArticles) {
            HashMap<String, String> data = new HashMap<>();
            String userName = article.getUserName();
            String text = article.getText();
            String pic = Base64.getEncoder().encodeToString(article.getPic());

            data.put("userName", URLDecoder.decode(userName, "UTF-8"));
            data.put("text", text);
            data.put("base64Image", pic);

            byte[] bytes = TemplateEngine.renderTemplate(getFileContent(STATIC_PATH + "/article.html").body, data);

            articles.append(new String(bytes));
        }

        return articles.toString();
    }
}
