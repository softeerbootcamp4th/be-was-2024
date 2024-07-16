package route.routes.auth;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import view.MyView;

import java.util.HashMap;

public class LoginPageHandler implements IRouteHandler {
    private final static Logger logger = LoggerFactory.getLogger(LoginPageHandler.class);
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.OK);
        MyView.render(req, res, "/login/index", new HashMap<>());
//        res.send("/login/index.html");
    }
}
