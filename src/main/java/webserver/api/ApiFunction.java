package webserver.api;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;


/*
* interface of all functions of WAS
* all api and static file call should implement this interface
* */
public interface ApiFunction {
    HttpResponse function(HttpRequest request);
}
