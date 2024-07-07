package webserver.http.request;

import exception.NotExistException;

import java.util.HashMap;
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
        return parseExtension(path);
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

    public static Request parseRequest(String request){

        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");

        return new Request(
                parsePath(startLine[1])
                ,parseParameterMap(parseParameter(startLine[1]))
        );

    }

    private static String parsePath(String path){
        return path.split("\\?")[0];
    }

    private static String parseParameter(String path){
        String[] split = path.split("\\?");
        if(split.length>1) return path.split("\\?")[1];
        return "";
    }

    private static Map<String, String> parseParameterMap(String pathWithOutParameters){

        Map<String, String> parameterMap = new HashMap<>();
        String[] split = pathWithOutParameters.split("&");

        for (String s : split) {
            String[] param = s.split("=");
            if(param.length<2) continue;
            parameterMap.put(param[0], param[1]);
        }

        return parameterMap;

    }

    private static String parseExtension(String path){
        String extension;
        try {
            extension = path.split("\\.")[1];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new NotExistException();
        }
        return extension;
    }


}
