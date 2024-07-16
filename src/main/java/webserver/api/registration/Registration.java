package webserver.api.registration;

import db.Database;
import model.User;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.PageBuilder;
import webserver.util.ParamsParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
* Registration function
* */
public class Registration implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new Registration();

        return single_instance;
    }

    static final String idRegex = "^[a-zA-Z][a-zA-Z0-9]{3,11}$";
    static final String passwordRegex = "^[a-zA-Z0-9]{6,20}$";

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {

        String body = URLDecoder.decode(new String(request.getBody()),StandardCharsets.UTF_8) ;


        Map<String, String> params = ParamsParser.parseParams(body);

        String id =params.get("id");
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");

        if(id == null || id.isEmpty()
                || username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || email == null || email.isEmpty()){
            return new HttpResponse.ResponseBuilder(404).build();
        }

        Pattern idpattern = Pattern.compile(idRegex);
        Pattern passwordpattern = Pattern.compile(passwordRegex);
        Matcher idmatcher = idpattern.matcher(id);
        Matcher passwordmatcher = passwordpattern.matcher(password);

        if(!idmatcher.matches()){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("아이디는 영문자(대소문자)로 시작하고 길이는 4~12자여야 합니다."))
                    .build();
        }

        if(!passwordmatcher.matches()){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("비밀번호는 6~20자이어야 합니다"))
                    .build();
        }

        if(Database.findUserById(id) !=null){
            return new HttpResponse.ResponseBuilder(422)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildRegistrationFailedPage("아이디가 중복되었습니다"))
                    .build();
        }
        Database.addUser(new User(id, password, username, email));

        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/")
                .addheader("Content-Type", "text/html; charset=utf-8")
                .build();
    }
}
