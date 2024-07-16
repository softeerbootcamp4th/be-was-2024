package ApiProcess;

import webserver.Request;
import webserver.Response;

public class LoginPageApiProcess implements ApiProcess {

  @Override
  public String process(Request request, Response response) {
    return "login/index";
  }
}
