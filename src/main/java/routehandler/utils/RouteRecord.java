package routehandler.utils;

import http.enums.HttpMethodType;
import routehandler.core.IRouteHandler;

public record RouteRecord(String pathname, HttpMethodType method, IRouteHandler handler) { }
