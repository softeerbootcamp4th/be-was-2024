package ApiProcess;

import webserver.Request;
import webserver.Response;

public class UserlistPageApiProcess implements ApiProcess {

  @Override
  public String process(Request request, Response response) {
    return "user_list";
  }
}
