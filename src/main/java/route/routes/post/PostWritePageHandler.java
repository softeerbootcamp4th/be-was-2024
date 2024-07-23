package route.routes.post;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import routehandler.core.IRouteHandler;
import view.MyView;

public class PostWritePageHandler implements IRouteHandler {
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.OK);
        MyView.render(req, res, "/posts/write");
    }
}
