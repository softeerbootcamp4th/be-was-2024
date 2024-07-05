package routehandler.core;

import http.MyHttpRequest;
import http.MyHttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 중간 경로를 지정하기 위한 라우트 핸들러. 변경될 수 있음
 */
public class ApiRouteHandler implements IRouteHandler{
    protected String routePrefix;
    protected List<IRouteHandler> routeHandlers;

    public ApiRouteHandler(String routePrefix) {
        this.routePrefix = routePrefix;
    }
    public ApiRouteHandler(String routePrefix, IRouteHandler... handlers) {
        this.routePrefix = routePrefix;
        routeHandlers = new ArrayList<>();

        for(IRouteHandler handler : handlers) {
            if(handler == null) continue;
            routeHandlers.add(handler);
        }
    }

    @Override
    public boolean canMatch(Object... args) {
        String pathname = (String)args[0];
        return pathname.startsWith(routePrefix);
    }

    @Override
    public  void handle(MyHttpRequest req, MyHttpResponse res) {
        String pathname = req.getUrl().getPathname();

        for(IRouteHandler handler : routeHandlers) {
            if(!handler.canMatch(pathname)) continue;

            handler.handle(req, res);
            break;
        }
    }
}
