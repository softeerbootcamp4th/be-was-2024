package webserver.api;

import db.Database;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;



/*
* Registration function
* */
public class Registration implements ApiFunction {
    @Override
    public HttpResponse funcion(HttpRequest request) {

        String id = request.getUri().getParamsMap().get("id");
        String username = request.getUri().getParamsMap().get("username");
        String password = request.getUri().getParamsMap().get("password");

        if(id == null || id == ""
                || username == null || username.equals("")
                || password == null || password.equals("")){
            return new HttpResponse(404);
        }
        Database.addUser(new User(id, password, username, ""));

        HttpResponse response = new HttpResponse(302);
        response.addHeaders("Location", "http://localhost:8080/");
        response.addHeaders("Content-Type", "text/html; charset=utf-8");
        return response;
    }
}
