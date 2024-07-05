package ApiProcess;

import webserver.Request;
import webserver.Response;

/**
 * Api 로직 처리를 위한 인터페이스
 */
public interface ApiProcess {
    String process(Request request, Response response);
}
