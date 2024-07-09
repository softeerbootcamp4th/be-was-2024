package webserver.api;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;


/*
* interface of all functions of WAS
* all server functions should implement this interface
* */
public interface RequestHandler {
    HttpResponse function(HttpRequest request) throws IOException;
}
