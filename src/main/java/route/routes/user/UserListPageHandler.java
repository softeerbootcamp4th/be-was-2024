package route.routes.user;

import db.Database;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import routehandler.core.IRouteHandler;
import view.MyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListPageHandler implements IRouteHandler {
    @Override
    public void handle(MyHttpRequest req, MyHttpResponse res) {
        if(req.getStoreData("user") == null) {
            res.redirect("/login");
            return;
        }

        res.setStatusInfo(HttpStatusType.OK);
        Map<String, Object> items = new HashMap<>();

        List<User> users = Database.findAll().stream().toList();
        items.put("users", users);
        MyView.render(req,res,"/user/list",items);
    }
}
