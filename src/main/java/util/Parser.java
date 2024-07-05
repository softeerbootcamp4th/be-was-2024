package util;

import exception.NotExistException;
import webserver.http.request.Request;

import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static String parsePath(String path){
        return path.split("\\?")[0];
    }

    public static String parseParameter(String path){
        String[] split = path.split("\\?");
        if(split.length>1) return path.split("\\?")[1];
        return "";
    }

    public static Map<String, String> parseParameterMap(String pathWithOutParameters){

        Map<String, String> parameterMap = new HashMap<>();
        String[] split = pathWithOutParameters.split("&");

        for (String s : split) {
            String[] param = s.split("=");
            if(param.length<2) continue;
            parameterMap.put(param[0], param[1]);
        }

        return parameterMap;

    }

    public static Request parseRequest(String request){

        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");

        return new Request(
                parsePath(startLine[1])
                ,parseParameterMap(parseParameter(startLine[1]))
        );

    }

    public static String parseExtension(String path){
        String extension;
        try {
            extension = path.split("\\.")[1];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new NotExistException();
        }
        return extension;
    }

}
