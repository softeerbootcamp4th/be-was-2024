package webserver.api.registration;

import db.Database;
import model.User;
import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/*
* Registration function
* */
public class Registration implements ApiFunction {
    @Override
    public HttpResponse function(HttpRequest request) {

        Map<String, String> params = new HashMap<>();

        String body = new String(request.getBody(), StandardCharsets.UTF_8);
        for(String line : body.split("&")){
            String[] param = line.split("=");
            params.put(param[0], param[1]);
        }

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
        Database.addUser(new User(id, password, username, email));

        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/")
                .addheader("Content-Type", "text/html; charset=utf-8")
                .build();
    }
}
