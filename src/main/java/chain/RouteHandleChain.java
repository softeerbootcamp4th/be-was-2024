package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import routehandler.core.IRouteHandler;
import routehandler.core.RouteHandlerMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteHandleChain extends MiddlewareChain {
    private final List<IRouteHandler> routeHandlers;

    public RouteHandleChain() {
        super();
        this.routeHandlers = new ArrayList<>();
    }

    public RouteHandleChain(IRouteHandler... routeHandlers) {
        this();
        this.routeHandlers.addAll(Arrays.asList(routeHandlers));
    }

    public void addRouteHandler(IRouteHandler routeHandler) {
        this.routeHandlers.add(routeHandler);
    }

    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        String pathname = req.getUrl().getPathname();
        for(IRouteHandler routeHandler : routeHandlers) {
            if(!routeHandler.canMatch(pathname)) continue;

            routeHandler.handle(req, res);
            if(res.getStatusInfo() != null) break;
        }

        next(req, res);
    }
}
