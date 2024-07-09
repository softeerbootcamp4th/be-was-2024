package logic;

import db.Database;
import exception.QueryParameterNotFoundException;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static util.StringUtil.Header.*;
import static util.HttpStatus.*;

public class Logic {
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);

    private static final String staticResourcePath = "src/main/resources/static";

    public static HttpResponse serve(HttpRequest request){
        switch (request.getPath()) {
            case "/create":
                return registration(request);
            default:
                return serveStaticResource(request);
        }
    }

    private static HttpResponse serveStaticResource(HttpRequest request) {
        File file = new File(staticResourcePath + request.getViewPath());

        if (!file.exists()) {
            return HttpResponse.error(SC_NOT_FOUND, "Page Not Found!");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            HttpResponse response = new HttpResponse();
            byte[] body = fis.readAllBytes();
            response.setBody(body);
            response.setStatusCode(SC_OK);
            response.addHeader(CONTENT_TYPE, request.getContentType());
            response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponse.error(SC_INTERNAL_SERVER_ERROR, "Server Error!");
        }
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
            // /registration/index.html form의 내용은 전부 required 이므로 파라미터를 찾지 못하는건 유저가 임의로 url을 변경하여 접근했을때이다.
            logger.debug(qe.getMessage());
            return HttpResponse.error(SC_BAD_REQUEST, "Invalid Access");
        }
    }
}
