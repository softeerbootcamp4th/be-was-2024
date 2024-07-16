package webserver.util;

import java.util.HashMap;
import java.util.Map;

public class ParamsParser {
    public static Map<String, String> parseParams(String params) {

        Map<String, String> paramsMap = new HashMap<>();
        if (params != null) {
            for(String line : params.split("&")){
                String[] param = line.split("=");
                paramsMap.put(param[0], param[1]);
            }
        }
        return paramsMap;
    }
}
