package logic;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.HttpStatus.*;
import static util.StringUtil.*;
import static util.StringUtil.Header.*;
import static util.StringUtil.Method.*;

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
            return HttpResponse.error(SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    private static HttpResponse registration(HttpRequest httpRequest){
        if(!httpRequest.getMethod().equals(POST)){
            return HttpResponse.error(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
        //TODO body to string and parsing
        byte[] body = httpRequest.getBody();
        if(body==null){
            return HttpResponse.error(SC_BAD_REQUEST, "Request body is empty");
        }

        String bodyString = new String(body, StandardCharsets.UTF_8);
        Map<String, String> params = new HashMap<>();

        String[] queries = bodyString.split(AMPERSAND);
        if(queries.length!=4) {
            return HttpResponse.error(SC_BAD_REQUEST, "Request body must contain 4 parameters.");
        }

        for(String query : queries){
            int index = query.indexOf(EQUALS);
            if(index==-1){
                return HttpResponse.error(SC_BAD_REQUEST, "Request body parameter must be formatted like (key=value)");
            }
            String key = URLDecoder.decode(query.substring(0, index), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(query.substring(index + 1), StandardCharsets.UTF_8);
            params.put(key, value);
        }

        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        if(userId==null || password==null || name==null || email==null){
            return HttpResponse.error(SC_BAD_REQUEST, "Missing required parameters");
        }

        Database.addUser(new User(userId, password, name, email));

        return HttpResponse.redirect(ROOT_PATH);
    }
}
