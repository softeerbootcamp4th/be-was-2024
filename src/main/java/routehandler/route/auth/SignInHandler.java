package routehandler.route.auth;

import config.AppConfig;
import db.Database;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import session.MySession;
import url.MyURL;
import utils.FileReadUtil;
import view.MyView;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SignInHandler implements IRouteHandler {
    private final static Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        String formData = new String(req.getBody(), StandardCharsets.UTF_8);
        Map<String, String> formParams = MyURL.parseParameter(formData);

        var userId = formParams.get("userId");
        var password = formParams.get("password");

        User user = Database.findUserById(userId);
        // 유저가 없거나, 비밀번호가 일치하지 않는 경우
        if(user == null || !user.getPassword().equals(password)) {
            res.setStatusInfo(HttpStatusType.UNAUTHORIZED);
//            res.send("/login/login_failed.html");
            MyView.render(req, res, "/login/login_failed", new HashMap<>());

            return;
        }

        // 로그인 성공. 세션 생성하고 쿠키에 SID 넣기
        String sessionId = MySession.createSession(user.getUserId());
        res.getCookies().put(AppConfig.SESSION_NAME, sessionId);

        res.redirect("/");
    }
}
