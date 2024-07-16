package routehandler.route.auth;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileReadUtil;

public class LoginPageHandler implements IRouteHandler {
    private final static Logger logger = LoggerFactory.getLogger(LoginPageHandler.class);
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        try {
            byte[] html = FileReadUtil.read("/login/index.html");
            res.setStatusInfo(HttpStatusType.OK);
            res.setBody(html);
            return;
        } catch (Exception e) {
            logger.error(e.getMessage());
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
