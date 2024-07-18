package apiprocess;

import auth.Session;
import enums.HttpCode;
import auth.Cookie;
import webserver.Request;
import webserver.Response;

import java.util.Map;

public class LogoutApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    Cookie cookie = new Cookie(Session.SESSION_ID, "");
    response.addCookie(cookie.maxAge(0));
    response.setHttpCode(HttpCode.Found);
    response.setLocation("/");
    return null;
  }
}
