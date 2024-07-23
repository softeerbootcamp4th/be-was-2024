package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpMethodType;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import routehandler.core.exception.NoMatchedMethodException;
import routehandler.core.exception.NoMatchedRouteException;
import routehandler.core.trie.RouteTrie;
import routehandler.utils.Route;
import routehandler.utils.RouteRecord;

/**
 * 유저가 요청한 경로에 대한 라우팅을 처리하는 체인.
 */
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
            IRouteHandler handler = trie.search(pathname,method, req);
            handler.handle(req, res);
        } catch (NoMatchedMethodException e) {
            res.setStatusInfo(HttpStatusType.METHOD_NOT_ALLOWED);
            res.setBody("method not allowed");
            // 핸들러에 의해 처리될 수 없는 상황 다음으로 넘긴다.
            logger.error(e.getMessage());
        } catch (NoMatchedRouteException e) {
            res.setStatusInfo(HttpStatusType.NOT_FOUND);
            res.setBody("not found");
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        next(req, res);
    }
}
