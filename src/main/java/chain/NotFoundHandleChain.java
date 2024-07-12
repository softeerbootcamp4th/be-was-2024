package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import route.routes.NotFoundPageHandler;

public class NotFoundHandleChain extends MiddlewareChain {
    private final NotFoundPageHandler handler;

    public NotFoundHandleChain() {
        this.handler = new NotFoundPageHandler();
    }

    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        if(res.getStatusInfo() == null) handler.handle(req, res);
    }
}
