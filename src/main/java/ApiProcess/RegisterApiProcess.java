package ApiProcess;

import db.Database;
import enums.HttpCode;
import model.User;
import webserver.Request;
import webserver.Response;

@GetMethod(apiPath = "/user/create")
public class RegisterApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        String name = request.getParameter("name");
        String userId = request.getParameter("userId");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User(userId, password, name, email);
        Database.addUser(user);
        response.setHttpCode(HttpCode.Found);
        return "/";
    }
}
