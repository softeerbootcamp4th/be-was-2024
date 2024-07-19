package chain;

import chain.core.MiddlewareChain;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReadUtil;

/**
 * 사용자가 업로드한 자원을 찾을 때 사용하는 체인
 */
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
