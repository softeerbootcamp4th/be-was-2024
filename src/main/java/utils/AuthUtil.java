package utils;

import auth.Session;
import model.User;
import webserver.Request;

public class AuthUtil {

  public static User isLogin(Request request) {
    String sessionId = request.getCookie(Session.SESSION_ID);
    if(sessionId == null) return null;
    Session session = Session.getInstance();
    return session.get(sessionId);
  }
}
