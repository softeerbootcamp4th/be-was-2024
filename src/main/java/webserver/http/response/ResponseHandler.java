package webserver.http.response;

import db.Database;
import model.User;
import webserver.http.request.Request;

import java.io.IOException;

import static util.Utils.getFile;

public class ResponseHandler {

    public static Response response(Request request) throws IOException {
        Response response = new Response.Builder(Status.NOT_FOUND).build();

        switch(request.getPath()) {
            case "/registration" ->
                response = new Response.Builder(Status.SEE_OTHER)
                        .addHeader("Location", "/registration/index.html")
                        .build();

            case "/create" -> {
                User user = new User(
                        request.getParameterValue("userId"),
                        request.getParameterValue("password"),
                        request.getParameterValue("name"),
                        request.getParameterValue("email")
                );
                Database.addUser(user);
            }

            default ->
                response = new Response.Builder(Status.OK)
                        .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                        .body(getFile(request.getPath()))
                        .build();
        }

        return response;
    }

    private static String getContentType(String extension){
        return switch (extension){
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico", "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> throw new IllegalStateException("Unexpected value: " + extension);
        };
    }

}
