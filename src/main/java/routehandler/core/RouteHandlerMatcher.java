package routehandler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteHandlerMatcher {
    List<IRouteHandler> routeHandlers;

    public RouteHandlerMatcher() {
        this.routeHandlers = new ArrayList<>();
    }

    public RouteHandlerMatcher(IRouteHandler... routeHandlers) {
        this();
        this.routeHandlers.addAll(Arrays.asList(routeHandlers));
    }

    public IRouteHandler getMatchedRouteHandler(String url) {
        IRouteHandler routeHandler = null;
        for (IRouteHandler handler : routeHandlers) {
            if(!handler.canMatch(url)) continue;

            routeHandler = handler;
            break;
        }

        return routeHandler;
    }
}
