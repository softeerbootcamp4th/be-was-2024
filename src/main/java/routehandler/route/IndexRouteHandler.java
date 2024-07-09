package routehandler.route;

import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileReadUtil;
import webserver.WebServer;

public class IndexRouteHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(IndexRouteHandler.class);
    private final String routePrefix;

    public IndexRouteHandler(String routePrefix) {
        this.routePrefix = routePrefix;
    }

    @Override
    public boolean canMatch(Object... args) {
        String path = (String) args[0];
        return path.equals(routePrefix);
    }

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        try {
            byte[] body = FileReadUtil.read(AppConfig.STATIC_RESOURCES_PATH + "/index.html");
            res.setBody(body);

            res.setStatusInfo(HttpStatusType.OK);
        } catch(Exception e) {
            logger.error("error on read index.html");
            logger.error(e.getMessage());
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
