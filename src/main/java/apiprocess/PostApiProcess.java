package apiprocess;


import db.H2Database;
import enums.HttpCode;
import exception.SQLRuntimeException;
import model.Post;
import webserver.Request;
import webserver.Response;

import java.sql.SQLException;
import java.util.Map;

public class PostApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    H2Database repository = H2Database.getInstance();
    Post post = new Post(title, content);
    repository.addPost(post);
    response.setHttpCode(HttpCode.Found);
    response.setLocation("/");
    return null;
  }
}
