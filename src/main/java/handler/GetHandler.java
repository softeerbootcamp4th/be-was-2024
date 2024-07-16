package handler;

import db.Database;
import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import util.TemplateEngine;
import util.Utils.ResponseWithStatus;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static db.Session.getUser;
import static db.Session.isLogin;
import static util.Constants.*;
import static util.Utils.*;

public class GetHandler {

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
            data.put("loginHref", "/login");
            data.put("loginButton", "로그인");
            data.put("registrationHref", "/registration");
            data.put("registrationButton", "회원가입");

        } else {

            String userId = getUser(sid);
            User user = Database.findUserById(userId);
            data.put(USER_NAME, user.getName());
            data.put("msg", "님, 환영합니다.");
            data.put("loginHref", "/article");
            data.put("loginButton", "글쓰기");
            data.put("registrationHref", "/logout");
            data.put("registrationButton", "로그아웃");

        }
        return serveDynamicFile(FILE_INDEX, data);

    }

    public static HttpResponse logout(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        Session.deleteSession(sid);

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
            Collection<User> users = Database.findAll();

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
            data.put(USER_NAME, Database.findUserById(Session.getUser(sid)).getName());
            return serveDynamicFile(PATH_USER + FILE_LIST, data);
        }
    }
}
