package webserver.http;

import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.api.Registration;
import webserver.http.enums.Methods;

import java.util.HashMap;
import java.util.Map;


/*
* class to find functions that matched with url data
* to add functions, must add path and function class to Pathmap
* */
public class PathHandler {
    private static Map<String, ApiFunction> PathMap = new HashMap<>(){
        {
            put("/registration", new Registration());
        }
    };

    //return ApiFunction implemented class
    public static ApiFunction getPath(HttpRequest request) {
        if(request.getMethod() == Methods.GET){
            if(request.getUri().getParamsMap().isEmpty()){ // parameter없음
                return new ReadFile();
            }else{
                ApiFunction function = PathMap.get(request.getUri().getPath());
                if(function == null){
                    return new ReadFile();
                }else return function;
            }
        }else{
            return new ReadFile();
        }
    }
}
