package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import routehandler.core.trie.RouteTrie;
import routehandler.utils.Route;
import routehandler.utils.RouteRecord;

public class RouteHandleChain extends MiddlewareChain {
    private static Logger logger = LoggerFactory.getLogger(RouteHandleChain.class);
    private final RouteTrie trie;

    public RouteHandleChain() {
        super();
        this.trie = new RouteTrie();
    }

    public RouteHandleChain(Route... routes) {
        this();
        for (Route route : routes) {
            for (RouteRecord routeInfo : route.getAllRouteHandlerRecords(null)) {
                String pathname = routeInfo.pathname();
                HttpMethodType method = routeInfo.method();
                IRouteHandler handler = routeInfo.handler();
                this.trie.insert(pathname, method, handler);
            }
        }
    }

    public void addRouteHandler(String url, IRouteHandler routeHandler) {
        trie.insert(url, HttpMethodType.GET, routeHandler);
    }

    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        String pathname = req.getUrl().getPathname();
        HttpMethodType method = req.getMethod();

        try {
            IRouteHandler handler = trie.search(pathname,method);
            handler.handle(req, res);
        } catch (Exception e) {
            // 핸들러에 의해 처리될 수 없는 상황 다음으로 넘긴다.
            logger.error(e.getMessage());
        }

        next(req, res);
    }
}
