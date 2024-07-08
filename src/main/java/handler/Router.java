package handler;

import http.HttpMethod;
import http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static handler.GetHandler.getRouter;

public class Router {
    public static void requestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        HttpMethod method = httpRequest.getHttpMethod();
        String requestUrl = httpRequest.getRequestUrl();

        switch (method){
            case GET -> getRouter(requestUrl, out);
        }
    }
}
