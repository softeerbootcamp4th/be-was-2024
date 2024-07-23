package apiprocess;

import webserver.Request;
import webserver.Response;

import java.util.Map;

public class MethodNotAllowedApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    return "error/405";
  }
}
