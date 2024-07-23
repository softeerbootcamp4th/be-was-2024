package apiprocess;

import auth.Cookie;
import auth.Session;
import db.Database;
import db.H2Database;
import enums.HttpCode;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class LoginH2ApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    // fetch session
    Session session = Session.getInstance();

    // login information
    String userId = request.getParameter("userId");
    String password = request.getParameter("password");

    // find user
    H2Database repository = H2Database.getInstance();
    User loginUser;
    loginUser = repository.findUserById(userId);

    // 존재하지 않는 회원이거나 패스워드가 틀린 경우
    if(loginUser == null || isNotCorrectPassword(loginUser.getPassword(), password)) {
      model.put("loginFail", "로그인 실패 하셨어요");
      response.setHttpCode(HttpCode.UNAUTHORIZED);
      return "index";
    }
    // 로그인 성공 시
    String uuid = UUID.randomUUID().toString();
    Cookie cookie = new Cookie(Session.SESSION_ID, uuid);
    session.insert(uuid, loginUser);
    response.addCookie(cookie);
    response.setHttpCode(HttpCode.Found);
    response.setLocation("/");
    model.put("users", repository.findAll());

    return null;
  }

  private boolean isNotCorrectPassword(String inputPassword, String storedPassword) {
    return !inputPassword.equals(storedPassword);
  }
}
