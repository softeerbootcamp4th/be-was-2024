package routehandler.utils;

import http.enums.HttpMethodType;
import routehandler.core.IRouteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 경로 - 핸들러 매핑 정보를 담고 있는 유틸리티 클래스
 */
public class Route {
    private final String pathSegment;
    private final Map<String, Route> childRoutes;
    private final Map<HttpMethodType, IRouteHandler> handlers;

    public Route(String pathSegment) {
        this.pathSegment = pathSegment;
        this.childRoutes = new HashMap<>();
        this.handlers = new HashMap<>();
    }

    public Route(String pathSegment, Route... childRoutes) {
        this(pathSegment);
        for (Route route : childRoutes) {
            this.childRoutes.put(route.pathSegment, route);
        }
    }

    public static Route at(String pathSegment) {
        return new Route(pathSegment);
    }

    public static Route at(String pathSegment, Route... childRoutes) {
        return new Route(pathSegment, childRoutes);
    }

    public Route registerHandler(HttpMethodType methodType, IRouteHandler handler) {
        this.handlers.put(methodType, handler);
        return this;
    }

    public Route GET(IRouteHandler handler) {
        return registerHandler(HttpMethodType.GET, handler);
    }

    public Route POST(IRouteHandler handler) {
        return registerHandler(HttpMethodType.POST, handler);
    }

    public Route PUT(IRouteHandler handler) {
        return registerHandler(HttpMethodType.PUT, handler);
    }

    public Route DELETE(IRouteHandler handler) {
        return registerHandler(HttpMethodType.DELETE, handler);
    }

    public Route PATCH(IRouteHandler handler) {
        return registerHandler(HttpMethodType.PATCH, handler);
    }

    public Route routes(Route... childRoutes) {
        for (Route route : childRoutes) {
            this.childRoutes.put(route.pathSegment, route);
        }
        return this;
    }

    public List<RouteRecord> getAllRouteHandlerRecords(String prefix) {
        // 경로를 재구성
        String currentPathname = RouteUtil.getPathname(prefix, pathSegment);

        List<RouteRecord> routeHandlers = new ArrayList<>();
        for (Route route : childRoutes.values()) {
            routeHandlers.addAll(route.getAllRouteHandlerRecords(currentPathname));
        }

        for (Map.Entry<HttpMethodType, IRouteHandler> entry : handlers.entrySet()) {
            routeHandlers.add(new RouteRecord(currentPathname, entry.getKey(), entry.getValue()));
        }

        return routeHandlers;
    }
}
