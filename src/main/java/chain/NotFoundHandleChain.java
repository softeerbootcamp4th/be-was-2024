package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import routehandler.route.NotFoundRouteHandler;

public class NotFoundHandleChain extends MiddlewareChain {
    private final NotFoundRouteHandler handler;

    public NotFoundHandleChain() {
        this.handler = new NotFoundRouteHandler();
    }

    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        if(res.getStatusInfo() == null) handler.handle(req, res);
    }
}
