package routehandler.route;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileReadUtil;

public class RegistrationPageHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationPageHandler.class);

    public void handle(MyHttpRequest req, MyHttpResponse res) {
        try {
            byte[] body = FileReadUtil.read("/registration/index.html");
            res.setBody(body);
            res.setStatusInfo(HttpStatusType.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
