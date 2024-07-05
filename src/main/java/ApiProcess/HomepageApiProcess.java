package ApiProcess;

import webserver.Request;
import webserver.Response;

@GetMethod(apiPath = "/")
public class HomepageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        return "/index";
    }
}
