package ApiProcess;

import webserver.Request;
import webserver.Response;

public interface ApiProcess {
    String process(Request request, Response response);
}
