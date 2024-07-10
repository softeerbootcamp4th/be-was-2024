package handler;

import db.Session;
import processer.UserProcessor;
import util.exception.CustomException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static util.TemplateEngine.showAlert;

public class PostHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);

    static void createUser(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split("&");

        HttpStatus httpStatus;
        HashMap<String, String> headers = new HashMap<>();
        byte[] responseBody = new byte[0];
        try {
            String userId = bodyTokens[0].split("=", 2)[1];
            String name = bodyTokens[1].split("=")[1];
            String password = bodyTokens[2].split("=")[1];
            String email = bodyTokens[3].split("=")[1];

            UserProcessor.createUser(userId, name, password, email);

            httpStatus = HttpStatus.FOUND;
            headers.put("Location", "/");
        } catch (CustomException e) {
            httpStatus = e.getHttpStatus();
            responseBody = showAlert(e.getMessage(), "http://localhost:8080/registration");
        } catch (ArrayIndexOutOfBoundsException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBody = showAlert("모든 필드를 입력하세요.", "http://localhost:8080/registration");
        }

        headers.put("Content-Length", String.valueOf(responseBody.length));
        headers.put("Content-Type", "text/html;charset=UTF-8");

        HttpResponse response = new HttpResponse(httpStatus, headers, responseBody);
        dos.writeBytes(response.toString());
        dos.write(responseBody, 0, responseBody.length);
        dos.flush();
    }

    static void loginUser(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split("&");

        HttpStatus httpStatus;
        HashMap<String, String> headers = new HashMap<>();
        byte[] responseBody = new byte[0];

        try {
            String userId = bodyTokens[0].split("=")[1];
            String password = bodyTokens[1].split("=")[1];

            UserProcessor.loginUser(userId, password);
            String sid = Session.createSession(userId);

            httpStatus = HttpStatus.FOUND;
            headers.put("Location", "/");
            headers.put("Set-Cookie", "sid=" + sid + "; Path=/");
        } catch (CustomException e) {
            httpStatus = e.getHttpStatus();
            responseBody = showAlert(e.getMessage(), "http://localhost:8080/login");
        } catch (ArrayIndexOutOfBoundsException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBody = showAlert("모든 필드를 입력하세요.", "http://localhost:8080/login");
        }

        headers.put("Content-Length", String.valueOf(responseBody.length));
        headers.put("Content-Type", "text/html;charset=UTF-8");

        HttpResponse response = new HttpResponse(httpStatus, headers, responseBody);
        dos.writeBytes(response.toString());
        dos.write(responseBody, 0, responseBody.length);
        dos.flush();
    }
}
