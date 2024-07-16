package routehandler.route.auth;

import db.Database;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import url.MyURL;
import utils.FileReadUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SignUpHandler implements IRouteHandler {
    private final static Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        String formData = new String(req.getBody(), StandardCharsets.UTF_8);
        Map<String, String> formParams = MyURL.parseParameter(formData);

        var userId = formParams.get("userId");
        var password = formParams.get("password");
        var name = formParams.get("name");
        var email = formParams.get("email");

        if(!validateUser(userId, password, name, email)) {
            res.setStatusInfo(HttpStatusType.BAD_REQUEST);
            res.setBody("user info is not valid");
            return;
        };

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug("success to create user {}", user);

        res.redirect("/");
    }

    private boolean validateUser(String userid, String password, String name, String email) {
        return userid != null
                && password != null
                && name != null
                && email != null;
    }

}
