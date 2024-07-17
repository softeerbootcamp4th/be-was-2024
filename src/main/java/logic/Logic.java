package logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.SessionDatabase;
import db.UserDatabase;
import dto.UserDTO;
import http.HttpRequest;
import http.HttpResponse;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static util.HttpStatus.*;
import static util.StringUtil.*;
import static util.StringUtil.Header.*;
import static util.StringUtil.Method.*;

public class Logic {
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String staticResourcePath = "src/main/resources/static";
    private static final String dynamicResourcePath = "src/main/resources/templates";

    public HttpResponse serve(HttpRequest request) throws JsonProcessingException {
        switch (request.getPath()) {
            case "/create":
                return registration(request);
            case "/login":
                return login(request);
            case "/login_fail":
                return loginFail(request);
            case "/user":
                return responseUser(request);
            case "/user/list/all":
                return responseAllUser(request);
            default:
                return serveResource(request, staticResourcePath+request.getViewPath());
        }
    }

    private HttpResponse responseAllUser(HttpRequest request) throws JsonProcessingException {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(SC_OK);
        Collection<User> all = UserDatabase.findAll();
        byte[] body = objectMapper.writeValueAsBytes(all);
        response.setBody(body);
        response.addHeader(CONTENT_TYPE, "application/json");
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        return response;
    }

    private HttpResponse responseUser(HttpRequest request) throws JsonProcessingException {
        String sessionId = request.getQueryParameterValue("sid");
        Optional<Session> session = SessionDatabase.getSession(sessionId);
        if (session.isPresent()) {
            Optional<User> userById = UserDatabase.findUserById(session.get().getUserId());
            if (userById.isPresent()) {
                HttpResponse response = new HttpResponse();
                User user = userById.get();
                UserDTO dto = new UserDTO(user, true);
                byte[] body = objectMapper.writeValueAsString(dto).getBytes();
                response.setBody(body);
                response.setStatusCode(SC_OK);
                response.addHeader(CONTENT_TYPE, "application/json");
                response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
                return response;
            }
        }
        return HttpResponse.error(SC_NOT_FOUND, "sid not found");
    }

    private HttpResponse loginFail(HttpRequest request){
        return serveResource(request, staticResourcePath + "/user/login_failed.html");
    }

    private HttpResponse serveResource(HttpRequest request, String resourcePath) {
        File file = new File(resourcePath);

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

    private HttpResponse registration(HttpRequest httpRequest){
        if(!httpRequest.getMethod().equals(POST)){
            return HttpResponse.error(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }

        byte[] body = httpRequest.getBody();
        if(body==null || body.length==0){
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

        UserDatabase.addUser(new User(userId, password, name, email));

        return HttpResponse.redirect(ROOT_PATH);
    }

    private HttpResponse login(HttpRequest httpRequest){
        if(httpRequest.getMethod().equals(GET)) {
            return serveResource(httpRequest, staticResourcePath+httpRequest.getViewPath());
        }
        if(!httpRequest.getMethod().equals(POST)) {
            return HttpResponse.error(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }

        byte[] body = httpRequest.getBody();
        if(body==null || body.length==0){
            return HttpResponse.error(SC_BAD_REQUEST, "Request body is empty");
        }

        String bodyString = new String(body, StandardCharsets.UTF_8);
        Map<String, String> params = new HashMap<>();

        String[] queries = bodyString.split(AMPERSAND);
        if(queries.length!=2) {
            return HttpResponse.error(SC_BAD_REQUEST, "Request body must contain 2 parameters.");
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
        if(userId==null || password==null){
            return HttpResponse.error(SC_BAD_REQUEST, "Missing required parameters");
        }

        Optional<User> findUser = UserDatabase.findUserById(userId);
        if(findUser.isEmpty()){
            return HttpResponse.redirect("/login_fail");
        }

        User user = findUser.get();
        if(!user.getPassword().equals(password)){
            return HttpResponse.redirect("/login_fail");
        }

        //create Session
        String newSessionId;
        do{
            newSessionId = UUID.randomUUID().toString();
        } while(SessionDatabase.getSession(newSessionId).isPresent());

        LocalDateTime sessionStartTime = LocalDateTime.now();
        Session loginSession = new Session(newSessionId, user.getUserId(), sessionStartTime, sessionStartTime.plusMinutes(30));

        SessionDatabase.addSession(newSessionId, loginSession);

        return HttpResponse.redirect(ROOT_PATH, loginSession);
    }
}
