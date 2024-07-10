package handler;

import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Utils.ResponseWithStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static util.TemplateEngine.showAlert;
import static util.Utils.*;

public class GetHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);
    private static final String staticPath = getStaticPath();

    public static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(staticPath + requestUrl);

        HttpStatus httpStatus = responseWithStatus.status;
        byte[] body = responseWithStatus.body;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", getContentType(type));
        headers.put("Content-Length", String.valueOf(body.length));

        HttpResponse response = new HttpResponse(httpStatus, headers, body);
        dos.writeBytes(response.toString());
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public static void loginCheck(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        String userId = Session.getUser(sid);

        HttpStatus httpStatus = HttpStatus.FOUND;
        HashMap<String, String> headers = new HashMap<>();
        byte[] body = showAlert(userId, "http://localhost:8080");

        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", String.valueOf(body.length));
        HttpResponse response = new HttpResponse(httpStatus, headers, body);
        dos.writeBytes(response.toString());
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public static void logout(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String cookie = httpRequest.getHeaders("Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get("sid");

        Session.deleteSession(sid);

        HttpStatus httpStatus = HttpStatus.FOUND;
        HashMap<String, String> headers = new HashMap<>();
        byte[] body = new byte[0];

        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", "0");
        headers.put("Location", "/");
        HttpResponse response = new HttpResponse(httpStatus, headers, body);
        dos.writeBytes(response.toString());
        dos.write(body, 0, body.length);
        dos.flush();

    }
}
