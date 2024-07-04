package ApiProcess;

import webserver.Request;
import webserver.Response;

public class RegisterpageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        return "/registration/index.html";
    }
}
