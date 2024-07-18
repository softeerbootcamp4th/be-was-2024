package route.routes;

import db.tables.PostTable;
import db.tables.UserTable;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.Post;
import model.User;
import routehandler.core.IRouteHandler;
import view.MyView;

import java.util.HashMap;
import java.util.Map;

public class IndexPageHandler implements IRouteHandler {
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        res.setStatusInfo(HttpStatusType.OK);
        Post post;
        String postId = req.getUrl().getParameter("postId");

        if (postId == null) post = PostTable.findLastCreated();
        else {
            post = PostTable.findById(Integer.parseInt(postId));
            if (post == null) post = PostTable.findLastCreated();
        }
        PostTable.fetchImageLinks(post);
        User postUser = UserTable.findUserById(post.getUserId());

        Map<String, Object> model = new HashMap<>();
        model.put("authorName", postUser.getName());
        model.put("post", post);
        model.put("imageLinks", post.getImageLinks());

        res.setStatusInfo(HttpStatusType.OK);
        MyView.render(req, res, "/index", model);
    }
}
