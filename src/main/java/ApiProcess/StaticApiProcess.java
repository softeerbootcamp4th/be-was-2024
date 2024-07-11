package ApiProcess;

import webserver.Request;
import webserver.Response;

public class StaticApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response) {
        return request.getPath();
    }
}