package apiprocess;
import db.H2Database;
import model.Post;
import webserver.Request;
import webserver.Response;

import java.util.Collection;
import java.util.Map;

public class HomepageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response, Map<String, Object> model) {
        H2Database repository = H2Database.getInstance();
        Collection<Post> allPost = repository.findAllPost();
        model.put("allPost", allPost);
        model.put("postId", 1);
        return "index";
    }
}
