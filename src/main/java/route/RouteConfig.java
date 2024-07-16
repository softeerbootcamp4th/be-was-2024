package route;

import chain.RouteHandleChain;
import route.routes.IndexPageHandler;
import route.routes.auth.SignOutHandler;
import route.routes.registration.RegistrationPageHandler;
import route.routes.auth.LoginPageHandler;
import route.routes.auth.SignInHandler;
import route.routes.auth.SignUpHandler;
import route.routes.user.UserListPageHandler;
import routehandler.utils.Route;

public class RouteConfig {
    public static RouteHandleChain routeHandleChain() {
        return new RouteHandleChain(
            Route.at("/registration").GET(new RegistrationPageHandler()),
            Route.at("/auth")
            .routes(
                Route.at("/signup").POST(new SignUpHandler()),
                Route.at("/signin").POST(new SignInHandler()),
                Route.at("/signout").POST(new SignOutHandler())
            ),
            Route.at("/user")
            .routes(
                Route.at("/list").GET(new UserListPageHandler())
            ),
            Route.at("/login")
                .GET(new LoginPageHandler()),
            Route.at("/")
                .GET(new IndexPageHandler())
        );
    }
}
