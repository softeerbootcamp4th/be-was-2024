package routehandler.route;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import routehandler.core.IRouteHandler;

/**
 * 어떤 경로도 탐색하지 못했을 때 404 상태 코드를 반환하는 핸들러
 */
public class NotFoundRouteHandler implements IRouteHandler {
    @Override
    public boolean canMatch(Object... args) {
        // 최후에 항상 매칭된다.
        return true;
    }

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.NOT_FOUND);
        res.setBody("<h1>Resource Not Found</h1>");
    }
}
