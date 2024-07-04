package webserver.http;

import webserver.http.enums.Methods;

import java.util.HashMap;
import java.util.Map;


/*
* HttpRequest 정보를 모두 담고있는 class
*
* method : method of request
* url : url data of request
* body : body of request
* protocol : protocol of request
* headers : header map of request
* */

public class HttpRequest {
    private Methods method;
    private Url url; //하나의 클래스로 분리를 하면
    private String body;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();


    /*
    * initialized by first line of request
    * initialize method, url, protocol
    * */
    public HttpRequest(String startline) {
        String[] split = startline.split(" ");
        method =  Methods.valueOfMethod(split[0]);
        url = new Url(split[1]);
        protocol = split[2];
    }


    //add header one by one
    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public Methods getMethod() {
        return method;
    }

    public Url getUri() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}