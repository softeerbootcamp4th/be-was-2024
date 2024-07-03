package routehandler;

import http.MyHttpRequest;
import http.MyHttpResponse;

public interface IRouteHandler {
    boolean canMatch(Object ...args);
    void handle(MyHttpRequest request, MyHttpResponse response);
}
