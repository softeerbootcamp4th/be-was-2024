package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import route.routes.NotFoundPageHandler;

/**
 * 어떤 상태 코드도 매칭되지 않았을 때 최종적으로 404 예외를 반환하는 체인
 */
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
