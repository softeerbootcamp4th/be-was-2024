package chain;

import chain.core.MiddlewareChain;
import config.AppConfig;
import db.Database;
import http.MyHttpRequest;
import http.MyHttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.MySession;

public class UserTestChain extends MiddlewareChain {
    private static Logger logger = LoggerFactory.getLogger(UserTestChain.class);
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

        User user = Database.findUserById(userId);
        logger.debug("user is {}", user);

        next(req,res);
    }
}
