package apiprocess;

import webserver.Request;
import webserver.Response;

import java.util.Map;

/**
 * Api 로직 처리를 위한 인터페이스
 */
public interface ApiProcess {
    String process(Request request, Response response, Map<String, Object> model);
}
