package handler;

import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import util.Utils.ResponseWithStatus;

import java.io.IOException;
import java.util.HashMap;

import static db.Session.isLogin;
import static util.TemplateEngine.showAlert;
import static util.Utils.*;

public class GetHandler {
    private static final String staticPath = getStaticPath();

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

    public static HttpResponse serveRootPage(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        if (isLogin(sid)) return serveStaticFile("/main/index.html");
        else return serveStaticFile("/index.html");
    }

    public static HttpResponse loginCheck(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");
        String userId = Session.getUser(sid);

        byte[] body = showAlert(userId, "http://localhost:8080");

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader("Content-Type", "text/html;charset=UTF-8")
                .addHeader("Content-Length", String.valueOf(body.length))
                .addBody(body);
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
}
