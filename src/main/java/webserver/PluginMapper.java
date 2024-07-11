package webserver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PluginMapper {

    // 플러그인을 저장할 리스트
    private Map<String, Method> pathMap = new HashMap<>();

    public void put(webserver.http.request.Method httpMethod, String path, Method method){
        pathMap.put(httpMethodAndPathToString(httpMethod, path), method);
    }

    public Method get(webserver.http.request.Method httpMethod, String path){
        return pathMap.get(httpMethodAndPathToString(httpMethod, path));
    }

    private String httpMethodAndPathToString(webserver.http.request.Method httpMethod, String path){
        return httpMethod.getMethodName()+" "+path;
    }

}
