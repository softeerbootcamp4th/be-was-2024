package chain;

import chain.core.MiddlewareChain;
import config.AppConfig;
import db.UserTable;
import http.MyHttpRequest;
import http.MyHttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.MySession;

public class UserSessionChain extends MiddlewareChain {
    private static final Logger logger = LoggerFactory.getLogger(UserSessionChain.class);
    @Override
    public void act(MyHttpRequest req, MyHttpResponse res) {
        String sessionId = req.getCookies().getValue(AppConfig.SESSION_NAME);
        if (sessionId == null) {
            logger.debug("no cookie");
            next(req, res);
            return;
        }

        String userId = (String)MySession.getSession(sessionId);
        if(userId == null) {
            logger.debug("no session");
            next(req, res);
            return;
        }

        User user = UserTable.findUserById(userId);
        req.setStoreData(AppConfig.USER, user);
        logger.debug("user is {}", user);

        next(req,res);
    }
}
