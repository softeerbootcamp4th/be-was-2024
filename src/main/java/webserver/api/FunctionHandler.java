package webserver.api;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;


/**
 * WAS 의 모든 기능에 대한 interface
 * <p>
 *     all server functions should implement this interface
 * </p>
*/
public interface FunctionHandler {
    HttpResponse function(HttpRequest request) throws IOException;
}
