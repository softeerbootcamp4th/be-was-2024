package apiprocess;

import db.H2Database;
import webserver.Request;
import webserver.Response;

import java.util.Map;

public class UserlistPageApiProcess implements ApiProcess {

  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    H2Database repository = H2Database.getInstance();
    model.put("users", repository.findAll());
    return "user_list";
  }
}
