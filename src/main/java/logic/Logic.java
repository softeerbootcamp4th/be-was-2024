package logic;

import db.Database;
import exception.QueryParameterNotFoundException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Logic {
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);

    private static final String staticResourcePath = "src/main/resources/static";

    public static HttpResponse serve(HttpRequest request){
        switch (request.getPath()) {
            case "/create":
                return registration(request);
            default:
                return defaultLogic(request);
        }
    }

    private static HttpResponse defaultLogic(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        File file = new File(staticResourcePath + request.getViewPath());

        if (!file.exists()) {
            return HttpResponse.error(HttpStatus.SC_NOT_FOUND, "Page Not Found!");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = fis.readAllBytes();
            response.setBody(body);
            response.setStatusCode(HttpStatus.SC_OK);
            response.addHeader("Content-Type", request.getContentType());
            response.addHeader("Content-Length", String.valueOf(body.length));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponse.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Server Error!");
        }

        return response;
    }

    private static HttpResponse registration(HttpRequest httpRequest){
        HttpResponse response = new HttpResponse();
        try{
            String userId = httpRequest.getQueryParameterValue("userId");
            String password = httpRequest.getQueryParameterValue("password");
            String name = httpRequest.getQueryParameterValue("name");
            String email = httpRequest.getQueryParameterValue("email");

            Database.addUser(new User(userId, password, name, email));

            return HttpResponse.redirect("/index.html");
        } catch (QueryParameterNotFoundException qe){
            // /registration/index.html form의 내용은 전부 required 이므로 유저가 임의로 url을 변경하여 접근했을때이다.
            logger.debug(qe.getMessage());
            return HttpResponse.error(HttpStatus.SC_BAD_REQUEST, "Invalid Access");
        }
    }
}
