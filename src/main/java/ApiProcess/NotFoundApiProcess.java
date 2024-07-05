package ApiProcess;

import webserver.Request;
import webserver.Response;

public class NotFoundApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        return "/error/404";
    }
}
