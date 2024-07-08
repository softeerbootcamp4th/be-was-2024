package routehandler.core;

import http.MyHttpRequest;
import http.MyHttpResponse;

@FunctionalInterface
public interface IRouteHandler {
    void handle(MyHttpRequest req, MyHttpResponse res);
}
