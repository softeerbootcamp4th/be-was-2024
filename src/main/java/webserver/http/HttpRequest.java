package webserver.http;

import webserver.http.enums.Methods;
import webserver.http.url.Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * HttpRequest 정보를 모두 담고있는 class
 *
*/
public class HttpRequest {
    /**
     * request 의 method
     * @see Methods
     */
    private Methods method;

    /**
     * request의 url 정보
     * @see Url
     */
    private Url url;

    /**
     * request의 body byte
     */
    private byte[] body;

    /**
     * protocol 이름
     */
    private String protocol;

    /**
     * request의 헤더 map
     */
    private Map<String, String> headers;

    /**
     * request의 쿠키 map
     */
    private Map<String, String> cookies;

    /**
     * request의 path variable map <br>
     * 추가는 path routing과정에서 진행
     */
    private Map<String, String> pathVariables;

    /**
     * request의 세션 id
     */
    private String sessionid;

    /**
     * method 반환
     * @return Method enum class
     * @see Methods
     */
    public Methods getMethod() {
        return method;
    }

    /**
     * url 반환
     * @return url class
     * @see Url
     */
    public Url getUrl() {
        return url;
    }

    /**
     * body byte 반환
     * @return body byte
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * protocol 반환
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * header 반환
     * @return header map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * cookie 반환
     * @return cookie map
     */
    public Map<String, String> getCookie(String key) {return cookies;}

    /**
     * session id 반환
     * @return session id
     * @see webserver.session.SessionDAO
     */
    public String getSessionid() {return sessionid;}

    /**
     * path variable 반환
     * @return path variable map
     */
    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    /**
     * path variable에 key와 value 추가
     * @param key 추가할 key
     * @param value 추가할 value
     */
    public void addPathVariable(String key, String value) {
        this.pathVariables.put(key, value);
    }

    /**
     * request header 내용들에 대해서 반환
     */
    public String printRequest(){
        return "method: " + method.getMethod() + "\n" +
                "url: " + url.getPath() + "\n" +
                "url params: " + url.getParamsMap().toString() + "\n" +
                "protocol: " + protocol + "\n" +
                "headers: " + headers.toString() + "\n";
    }

    /**
     * 생성자 클래스 <br>
     * builder를 이용하여 생성해야함
     */
    private HttpRequest(RequestBuilder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.protocol = builder.protocol;
        this.body = builder.body;
        this.headers = builder.headers;
        this.cookies = builder.cookies;
        this.sessionid = builder.sessionid;
        this.pathVariables = new HashMap<>();
    }

    public static class RequestBuilder {
        private Methods method;
        private Url url;
        private byte[] body;
        private String protocol;
        private Map<String, String> headers= new HashMap<>();
        private Map<String, String> cookies= new HashMap<>();
        private String sessionid;

        public RequestBuilder(String startline) throws IOException {
            String[] split = startline.split(" ");
            if(split.length != 3){
                throw new IOException("Invalid request line: " + startline);
            }
            method =  Methods.valueOfMethod(split[0]);
            if(method == null){ throw new IOException("Invalid request line: " + startline); }
            url = new Url(split[1]);
            protocol = split[2];
        }

        public RequestBuilder addHeader(String key, String value){
            headers.put(key.toLowerCase(), value);
            return this;
        }

        public RequestBuilder setBody(byte[] body) {
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