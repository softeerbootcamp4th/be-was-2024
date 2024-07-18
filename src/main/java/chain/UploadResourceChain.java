package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReadUtil;

public class UploadResourceChain extends MiddlewareChain {
    private static final Logger logger = LoggerFactory.getLogger(UploadResourceChain.class);
    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        String pathname = req.getUrl().getPathname();

        try {
            byte[] data = FileReadUtil.readFromLocal(pathname);
            res.setBody(data);
            res.setStatusInfo(HttpStatusType.OK);
            return;
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        next(req, res);
    }
}
