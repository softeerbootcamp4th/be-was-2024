package ApiProcess;

import db.Database;
import enums.HttpCode;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Validation;
import webserver.Request;
import webserver.Response;

public class RegisterApiProcess implements ApiProcess {
    private static final Logger logger = LoggerFactory.getLogger(RegisterApiProcess.class);
    @Override
    public String process(Request request, Response response) {
        String name = request.getParameter("name");
        String userId = request.getParameter("userId");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(Validation.anyNull(name, userId, email, password)) {
            response.setHttpCode(HttpCode.BAD_REQUEST);
            return "error/400";
        }

        logger.debug("name = {}, userId= {}, email = {}, password = {}", name, userId, email, password);
        User user = new User(userId, password, name, email);
        response.setHttpCode(HttpCode.Found);
        response.setLocation("/");
        Database.addUser(user);
        return "/";
    }
}
