package chain;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import chain.core.MiddlewareChain;
import routehandler.route.StaticResourceHandler;

public class StaticResourceChain extends MiddlewareChain {
    private final StaticResourceHandler resourceHandler;

    public StaticResourceChain() {
        this.resourceHandler = new StaticResourceHandler();
    }

    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        this.resourceHandler.handle(req, res);
        HttpStatusType type = res.getStatusInfo();

        // 정상적으로 처리한 경우
        if(type != null && type != HttpStatusType.UNSUPPORTED_MEDIA_TYPE) return;
        // 정상적으로 처리하지 못한 경우. 응답 정보를 초기화하고, 뒤에 매칭되는 부분 있는지 검사
        res.setStatusInfo(null);
        res.setBody(new byte[0]);

        next(req, res);
    }
}
