package webserver.http.request;

import exception.NotExistException;
import util.Parser;

import java.util.Map;

public class Request {

    private String path;
    private final Map<String, String> parameter;

    public Request(String path, Map<String, String> parameter){
        this.path = path;
        this.parameter = parameter;
    }

    public String getPath(){
        return this.path;
    }

    public String getExtension(){
        return Parser.parseExtension(path);
    }

    public String getParameterValue(String key){
        return this.parameter.get(key);
    }

    public boolean isStatic(){

        try {
            getExtension();
        }catch (NotExistException e){
            return false;
        }

        return true;
    }

}
