package apiprocess;

import webserver.Request;
import webserver.Response;

import java.util.Map;
import java.util.regex.Pattern;

public class PostWithIdApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    String path = request.getParameter("path");
    return null;
  }
}
