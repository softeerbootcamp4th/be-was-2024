package apiprocess;

import utils.AuthUtil;
import webserver.Request;
import webserver.Response;

import java.util.Map;

public class WritePageApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    if(AuthUtil.isLogin(request) != null) {
      return "article/index";
    } else {
      return "login/index";
    }
  }
}
