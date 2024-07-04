package routehandler.route;

import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.ApiRouteHandler;
import utils.FileReadUtil;
import webserver.WebServer;

public class RegistrationRouter extends ApiRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    public RegistrationRouter(String pathPrefix) {
        super(pathPrefix);
    }

    @Override
    public void handle(MyHttpRequest request, MyHttpResponse response) {
        var url = request.getUrl();

        var userId = url.getParameter("userId");
        var password = url.getParameter("password");
        var name = url.getParameter("name");
        var email = url.getParameter("email");

        logger.debug("id: {}, password: {}", userId, password);

        if(userId != null && password != null && name != null && email != null) {
            var user = new User(userId, password, name, email);
            logger.debug("user: {}", user);
            response.redirect("/");
            return;
        }
        try {
            byte[] body = FileReadUtil.read(AppConfig.STATIC_RESOURCES_PATH + "/registration/index.html");
            response.setBody(body);
            response.setStatusInfo(HttpStatusType.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
