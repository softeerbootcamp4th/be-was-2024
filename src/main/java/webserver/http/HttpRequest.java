package webserver.http;

import webserver.http.enums.Methods;
import webserver.http.url.Url;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
    private byte[] body;
    private String protocol;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private String sessionid;

    public Methods getMethod() {
        return method;
    }

    public Url getUrl() {
        return url;
    }

    public byte[] getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookie(String key) {return cookies;}

    public String getSessionid() {return sessionid;}

    public String printRequest(){
        return "method: " + method.getMethod() + "\n" +
                "url: " + url.getPath() + "\n" +
                "url params: " + url.getParamsMap().toString() + "\n" +
                "protocol: " + protocol + "\n" +
                "headers: " + headers.toString() + "\n";
    }


    private HttpRequest(ReqeustBuilder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.protocol = builder.protocol;
        this.body = builder.body;
        this.headers = builder.headers;
        this.cookies = builder.cookies;
        this.sessionid = builder.sessionid;
    }

    public static class ReqeustBuilder{
        private Methods method;
        private Url url;
        private byte[] body;
        private String protocol;
        private Map<String, String> headers= new HashMap<>();
        private Map<String, String> cookies= new HashMap<>();
        private String sessionid;

        public ReqeustBuilder(String startline) throws IOException {
            String[] split = startline.split(" ");
            if(split.length != 3){
                throw new IOException("Invalid request line: " + startline);
            }
            method =  Methods.valueOfMethod(split[0]);
            if(method == null){ throw new IOException("Invalid request line: " + startline); }
            url = new Url(split[1]);
            protocol = split[2];
        }

        public ReqeustBuilder addHeader(String key, String value){
            headers.put(key.toLowerCase(), value);
            return this;
        }

        public ReqeustBuilder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        private void setCookies (){
            if(headers.containsKey("cookie")){
                String[] split = headers.get("cookie").trim().split(";");
                for(String cookie : split){
                    String[] keyValue = cookie.trim().split("=");
                    cookies.put(keyValue[0].trim(), keyValue[1].trim());
                    if(Objects.equals(keyValue[0].trim(), "sid")){
                        sessionid = keyValue[1].trim();
                    }
                }
            }
        }

        public HttpRequest build(){
            setCookies();
            return new HttpRequest(this);
        }
    }
}