package ApiProcess;

import utils.AuthUtil;
import webserver.Request;
import webserver.Response;

public class HomepageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        if(AuthUtil.isLogin(request) != null) {
            return "index_login";
        } else {
            return "index";
        }
    }
}
