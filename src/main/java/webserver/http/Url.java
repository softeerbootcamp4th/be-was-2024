package webserver.http;

import java.util.HashMap;
import java.util.Map;

/*
* class to contain url data
* path : path data of url (path data between doamin and parameters)
* paramsMap : map of parameters of url
* */
public class Url {
    String path;
    Map<String, String> paramsMap = new HashMap<>();

    public Url(String url) {
        if(url.contains("?")){
            String paramline = url.substring(url.indexOf("?")+1);
            url = url.substring(0, url.indexOf("?"));
            for(String line : paramline.split("&")){
                String[] params = line.split("=");
                paramsMap.put(params[0], params[1]);
            }
        }
        path = url;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }
}
