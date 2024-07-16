package routehandler.route;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import utils.FileReadUtil;

public class IndexPageHandler implements IRouteHandler {
    private static final Logger logger = LoggerFactory.getLogger(IndexPageHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        try {
            byte[] body = FileReadUtil.read("/index.html");
            res.setBody(body);

            res.setStatusInfo(HttpStatusType.OK);
        } catch(Exception e) {
            logger.error("error on read index.html");
            logger.error(e.getMessage());
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
