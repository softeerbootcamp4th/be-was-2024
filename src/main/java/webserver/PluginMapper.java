package webserver;

import webserver.http.request.HttpMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 패스, http 메소드에 대한 실행 메소드를 저장하는 클래스
 */
public class PluginMapper {

    // 플러그인을 저장할 리스트
    private Map<String, Method> pathMap = new HashMap<>();

    /**
     * 패스, http 메소드에 대한 실행 메소드를 추가하는 메소드
     * @param httpMethod
     * @param path
     * @param method
     */
    public void put(HttpMethod httpMethod, String path, Method method){
        pathMap.put(httpMethodAndPathToString(httpMethod, path), method);
    }

    /**
     * 패스, http 메소드에 대한 실행 메소드를 반환하는 메소드
     * @param httpMethod
     * @param path
     * @return
     */
    public Method get(HttpMethod httpMethod, String path){
        return pathMap.get(httpMethodAndPathToString(httpMethod, path));
    }

    private String httpMethodAndPathToString(HttpMethod httpMethod, String path){
        return httpMethod.getMethodName()+" "+path;
    }

    /**
     * 패스, http 메소드에 대한 실행 메소드가 존재하는지 확인하는 메소드
     * @param httpMethod
     * @param path
     * @return
     */
    public boolean isExist(HttpMethod httpMethod, String path){
        return pathMap.containsKey(httpMethodAndPathToString(httpMethod, path));
    }

    /**
     * 해당 패스로 등록된 다른 메소드가 있는지 여부를 반환하는 메소드
     * @param httpMethod
     * @param path
     * @return
     */
    public boolean isExistOnlyPath(HttpMethod httpMethod, String path){
        boolean isExist = isExist(httpMethod, path);
        if(isExist) return false;
        for(HttpMethod method :HttpMethod.values()){
            if(isExist(method, path)) isExist = true;
        }
        if(isExist) return true;
        return false;
    }

}
