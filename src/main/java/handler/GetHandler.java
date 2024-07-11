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
import static util.Utils.*;

public class GetHandler {

    public static HttpResponse serveStaticFile(String requestUrl) throws IOException {
        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(staticPath + requestUrl);

        byte[] body = responseWithStatus.body;

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader("Content-Type", getContentType(type))
                .addHeader("Content-Length", String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse serveDynamicFile(String requestUrl, Map<String, String> data) throws IOException {
        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(staticPath + requestUrl);
        byte[] body = TemplateEngine.renderTemplate(responseWithStatus.body, data);

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader("Content-Type", getContentType(type))
                .addHeader("Content-Length", String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse serveRootPage(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        if (!isLogin(sid)) return serveStaticFile("/index.html");

        String userId = getUser(sid);
        User user = Database.findUserById(userId);
        Map<String, String> data = new HashMap<>();
        data.put("userName", user.getName());

        return serveDynamicFile("/main/index.html", data);
    }

    public static HttpResponse logout(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        Session.deleteSession(sid);

        byte[] body = new byte[0];

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader("Location", "/")
                .addHeader("Content-Type", "text/html;charset=UTF-8")
                .addHeader("Content-Length", String.valueOf(body.length))
                .addBody(body);
    }

    public static HttpResponse getUserList(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        if (!isLogin(sid)) {
            return new HttpResponse()
                    .addStatus(HttpStatus.FOUND)
                    .addHeader("Location", "/")
                    .addHeader("Content-Type", "text/html")
                    .addBody(new byte[0]);
        }
        else{
            Collection<User> users = Database.findAll();

            StringBuilder userList = new StringBuilder();
            for (User user : users) {
                userList.append("<tr>");
                userList.append("<td>").append(user.getUserId()).append("</td>");
                userList.append("<td>").append(user.getName()).append("</td>");
                userList.append("<td>").append(user.getEmail()).append("</td>");
                userList.append("</tr>");
            }

            Map<String, String> data = new HashMap<>();
            data.put("userList", userList.toString());
            data.put("userName", Database.findUserById(Session.getUser(sid)).getName());
            return serveDynamicFile("/user/userList.html", data);
        }
    }
}
