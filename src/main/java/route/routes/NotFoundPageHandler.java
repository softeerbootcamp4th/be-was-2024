package route.routes;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import routehandler.core.IRouteHandler;

/**
 * 어떤 경로도 탐색하지 못했을 때 404 상태 코드를 반환하는 핸들러
 */
public class NotFoundPageHandler implements IRouteHandler {

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.NOT_FOUND);
        res.setBody("<h1>Resource Not Found</h1>");
    }
}
