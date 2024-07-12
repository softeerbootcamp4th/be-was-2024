package route.routes;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import routehandler.core.IRouteHandler;
import view.MyView;

import java.util.HashMap;

public class IndexPageHandler implements IRouteHandler {
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.OK);
//        res.send("/index.html");
        MyView.render(req, res, "/index", new HashMap<>());
    }
}
