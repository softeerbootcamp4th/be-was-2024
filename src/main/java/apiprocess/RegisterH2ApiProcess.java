package apiprocess;

import db.H2Database;
import enums.HttpCode;
import model.User;
import utils.Validation;
import webserver.Request;
import webserver.Response;

import java.sql.SQLException;
import java.util.Map;

public class RegisterH2ApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    H2Database repository = H2Database.getInstance();

    String name = request.getParameter("name");
    String userId = request.getParameter("userId");
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    User existedUser = repository.findUserById(userId);
    if(Validation.anyNull(name, userId, email, password) || existedUser != null) {
      response.setHttpCode(HttpCode.BAD_REQUEST);
      model.put("registerFail", "회원 가입에 실패하셨어요.");
      return "registration/index";
    }

    if(Validation.isEmail(email)) {
      response.setHttpCode(HttpCode.BAD_REQUEST);
      model.put("registerFail", "이메일 형식이 잘못되었어요");
      return "registration/index";
    }

    User user = new User(userId, password, name, email);
    response.setHttpCode(HttpCode.Found);
    response.setLocation("/");
    repository.addUser(user);

    return "/";
  }
}
