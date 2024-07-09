package config;

import chain.RouteHandleChain;
import routehandler.route.IndexRouteHandler;
import routehandler.route.RegistrationPageHandler;
import routehandler.route.auth.SignUpHandler;
import routehandler.utils.Route;

public class RouteConfig {
    public static RouteHandleChain routeHandleChain() {
        return new RouteHandleChain(
            Route.at("/registration")
                .GET(new RegistrationPageHandler()),
            Route.at("/signup")
                .POST(new SignUpHandler()),
            Route.at("/")
                .GET(new IndexRouteHandler())
                .POST(new IndexRouteHandler())
        );
    }
}
