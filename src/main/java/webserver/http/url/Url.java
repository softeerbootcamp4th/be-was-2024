package webserver.http.url;

import java.util.HashMap;
import java.util.Map;

/**
 * http request의 url
 * <p>
 *     생성자에서 url의 path와 parameter를 구분하여 저장한다
 * </p>
 */
public class Url {
    String path;
    Map<String, String> paramsMap = new HashMap<>();

    /**
     * url의 생성자
     * @param url request 의 url
     */
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

    /**
     * url path의 getter
     */
    public String getPath() {
        return path;
    }

    /**
     * url parameter map의 getter
     */
    public Map<String, String> getParamsMap() {
        return paramsMap;
    }
}
