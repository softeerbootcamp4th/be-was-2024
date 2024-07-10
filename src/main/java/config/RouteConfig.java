package config;

import chain.RouteHandleChain;
import routehandler.route.IndexPageHandler;
import routehandler.route.registration.RegistrationPageHandler;
import routehandler.route.auth.LoginPageHandler;
import routehandler.route.auth.SignInHandler;
import routehandler.route.auth.SignUpHandler;
import routehandler.utils.Route;

public class RouteConfig {
    public static RouteHandleChain routeHandleChain() {
        return new RouteHandleChain(
            Route.at("/registration").GET(new RegistrationPageHandler()),
            Route.at("/auth")
            .routes(
                Route.at("/signup").POST(new SignUpHandler()),
                Route.at("/signin").POST(new SignInHandler())
                ),
            Route.at("/login")
                .GET(new LoginPageHandler()),
            Route.at("/")
                .GET(new IndexPageHandler())
        );
    }
}
