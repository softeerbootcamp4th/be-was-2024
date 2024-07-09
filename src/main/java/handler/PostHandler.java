package handler;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class PostHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);

    static void createUser(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split("&");

        String userId = bodyTokens[0].split("=")[1];
        String name = bodyTokens[1].split("=")[1];
        String password = bodyTokens[2].split("=")[1];
        String email = bodyTokens[3].split("=")[1];

        Database.addUser(new User(userId, name, password, email));

        HttpStatus httpStatus = HttpStatus.FOUND;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Location", "/");
        headers.put("Content-Type", "text/html;charset=UTF-8");
        headers.put("Content-Length", "0");

        HttpResponse response = new HttpResponse(httpStatus, headers);
        dos.writeBytes(response.toString());
        dos.flush();
    }
}
