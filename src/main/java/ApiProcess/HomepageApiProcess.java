package ApiProcess;

import utils.AuthUtil;
import webserver.Request;
import webserver.Response;

public class HomepageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        return "index";
    }
}
