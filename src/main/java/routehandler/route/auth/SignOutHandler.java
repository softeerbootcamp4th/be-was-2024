package routehandler.route.auth;

import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import routehandler.core.IRouteHandler;

public class SignOutHandler implements IRouteHandler {

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.getCookies().expire(AppConfig.SESSION_NAME);
        res.redirect("/");
    }
}