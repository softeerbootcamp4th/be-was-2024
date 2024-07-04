package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class Parameter {

    private final Map<String, String> parameters;

    public Parameter(String parameter){
        parameters = parseParameter(parameter);
    }

    private Map<String, String> parseParameter(String parameter){

        Map<String, String> parameterMap = new HashMap<>();
        String[] parameters = parameter.split("&");

        for (String s : parameters) {
            String[] param = s.split("=");
            parameterMap.put(param[0], param[1]);
        }

        return parameterMap;

    }

    public String get(String parameterKey){
        return this.parameters.get(parameterKey);
    }

}
