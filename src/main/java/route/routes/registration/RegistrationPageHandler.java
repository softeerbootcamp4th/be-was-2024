package route.routes.registration;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;

public class RegistrationPageHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationPageHandler.class);

    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.OK);
        res.send("/registration/index.html");
    }
}
