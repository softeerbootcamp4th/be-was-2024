package webserver.api;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public class Unauthorized implements FunctionHandler {

    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new Unauthorized();
        return single_instance;
    }

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/login")
                .addheader("Content-Type", "text/html; charset=utf-8")
                .build();
    }
}
