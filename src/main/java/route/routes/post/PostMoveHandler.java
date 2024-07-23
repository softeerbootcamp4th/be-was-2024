package route.routes.post;

import db.tables.PostTable;
import http.MyHttpRequest;
import http.MyHttpResponse;
import routehandler.core.IRouteHandler;

public class PostMoveHandler implements IRouteHandler {

    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        String curPostIdStr = req.getUrl().getParameter("postId");
        String direction = req.getUrl().getParameter("dir");

        if(curPostIdStr == null || direction == null) {
            res.redirect("/");
            return;
        }

        int postId;
        try {
            postId = Integer.parseInt(curPostIdStr);
        } catch (NumberFormatException e) {
            res.redirect("/");
            return;
        }


        Integer moveId = null;
        if(direction.equals("left")) {
            moveId = PostTable.findBeforeId(postId);
        } else if(direction.equals("right")) {
            moveId = PostTable.findAfterId(postId);
        }

        if(moveId == null) {
            res.redirect("/");
            return;
        }

        res.redirect("/?postId=" + moveId);
    }
}
