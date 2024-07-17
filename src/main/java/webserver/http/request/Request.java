package webserver.http.request;

import exception.NotExistException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Request 정보를 담는 클래스
 */
public class Request {

    private static final String CRLF = "\r\n";
    private static final char SPACE = ' ';
    public static final Logger logger = LoggerFactory.getLogger(Request.class);
    private final HttpMethod httpMethod;
    private final String path;
    private final String version = "HTTP/1.1";
    private final Map<String, String> parameter;
    private final Map<String, String> header;
    private final Map<String, String> cookie;
    private final byte[] body;

    private Request(Builder builder){
        this.httpMethod = builder.httpMethod;
        this.path = builder.path;
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.body = builder.body;
        this.cookie = builder.cookie;
    }

    public static class Builder {
        private HttpMethod httpMethod;
        private String path;
        private Map<String, String> parameter;
        private Map<String, String> header;
        private Map<String, String> cookie;
        private byte[] body;

        public Builder(HttpMethod httpMethod, String path){
            this.httpMethod = httpMethod;
            this.path = path;
            this.body = new byte[0];
            parameter = new LinkedHashMap<>();
            header = new LinkedHashMap<>();
            cookie = new HashMap<>();
        }

        public Builder parameter(Map<String, String> parameter){
            this.parameter = parameter;
            return this;
        }

        public Builder cookie(Map<String, String> cookie){
            this.cookie = cookie;
            return this;
        }

        public Builder header(Map<String, String> header){
            this.header = header;
            return this;
        }

        public Builder body(byte[] body){
            this.body = body;
            return this;
        }

        public Builder body(String body){
            this.body = body.getBytes();
            return this;
        }

        public Builder addParameter(String key, String value){
            parameter.put(key, value);
            return this;
        }

        public Builder addHeader(String key, String value){
            header.put(key, value);
            return this;
        }

        public Request build(){
            if(header.isEmpty()) return new Request(this);
            if(!header.containsKey("Cookie")) return new Request(this);
            this.cookie = parseCookie(header.get("Cookie"));
            return new Request(this);
        }

    }

    /**
     * key 를 통해 쿠키 값을 찾을 수 있는 메소드
     * @param key
     * @return
     */
    public String getCookieValue(String key){
        return this.cookie.get(key);
    }

    /**
     * http메소드를 반환하는 메소드
     * @return
     */
    public HttpMethod getMethod(){
        return this.httpMethod;
    }

    /**
     * 맵형 파라미터를 반환하는 메소드
     * @return
     */
    public Map<String, String> getParameter(){
        return this.parameter;
    }

    /**
     * body 를 반환하는 메소드
     * @return
     */
    public byte[] getBody(){
        return this.body;
    }

    /**
     * 요청 path 를 반환하는 메소드
     * @return
     */
    public String getPath(){
        return this.path;
    }

    /**
     * 요청 헤더를 반환하는 메소드
     * @return
     */
    public Map<String, String> getHeader(){
        return this.header;
    }

    /**
     * 확장자를 반환하는 메소드
     * @return
     */
    public String getExtension(){
        return parseExtension(path);
    }

    /**
     * key 에 대한 헤더 값을 반환하는 메소드
     * @param key
     * @return
     */
    public String getHeadValue(String key){
        return this.header.get(key);
    }

    /**
     * 로그인된 유저를 반환하는 메소드
     * @return
     */
    public Optional<User> getUser(){
        String sessionId = getSessionId();
        logger.debug(sessionId);
        if(!Session.isExist(sessionId)) return Optional.empty();
        return Optional.of(Session.get(getSessionId()));
    }

    /**
     * body를 제외한 request 부분을 파싱하는 메소드
     * @param request
     * @return
     * @throws IOException
     */
    public static Request parseRequestWithoutBody(String request) throws IOException {

        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");

        return new Builder(HttpMethod.fromMethodName(startLine[0]), parsePath(startLine[1]))
                .parameter(parseParameterMap(parseParameter(startLine[1])))
                .header(parseHeaderMap(request))
                .build();
    }
    public static Map<String, String> parseParameterMap(String parameterString){

        Map<String, String> parameterMap = new HashMap<>();
        String[] split = parameterString.split("&");

        for (String s : split) {
            String[] param = s.split("=");
            if(param.length<2) continue;
            parameterMap.put(param[0], param[1]);
        }
        return parameterMap;
    }

    /**
     * 로그인 상태인지 확인하는 메소드
     * @return
     */
    public boolean isLogin(){
        String sessionValue = getCookieValue("sid");
        if(sessionValue==null) return false;
        if(!Session.isExist(sessionValue)) return false;
        return true;
    }

    /**
     * 세션 ID 를 반환하는 메소드
     * @return
     */
    public String getSessionId(){
        return getCookieValue("sid");
    }

    private static String parsePath(String path){
        return path.split("\\?")[0];
    }

    private static String parseParameter(String path){
        String[] split = path.split("\\?");
        if(split.length>1) return path.split("\\?")[1];
        return "";
    }


    private static Map<String, String> parseCookie(String parameterCookieValue){
        Map<String, String> cookie = new HashMap<>();
        String[] cookieStrings = parameterCookieValue.split(";");

        for(int i=0; i<cookieStrings.length; i++){
            String cookieString = cookieStrings[i];
            String[] cookieStringSplit = cookieString.split("=");
            logger.debug(Arrays.toString(cookieStringSplit));
            cookie.put(removeSpace(cookieStringSplit[0]), removeSpace(cookieStringSplit[1]));
        }

        return cookie;
    }


    private static String removeSpace(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(c!=' ') sb.append(c);
        }
        return sb.toString();
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

    private static Map<String, String> parseHeaderMap(String request) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(request));
        Map<String, String> headers = new HashMap<>();
        br.readLine();

        while(true){
            String inputLine = br.readLine();
            if(inputLine!=null){
                if(inputLine.isEmpty()) break;
            }else break;
            headers.put(
                    parseHeaderName(inputLine),
                    parseHeaderValue(inputLine)
            );
        }

        return headers;
    }

    private static String parseHeaderName(String header){

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<header.length(); i++){
            char c = header.charAt(i);
            if(c==':') break;
            sb.append(c);
        }

        return sb.toString();
    }

    private static String parseHeaderValue(String header){

        StringBuilder sb = new StringBuilder();

        int i;
        for(i=0; header.charAt(i)!=':'; i++){}
        for(i++;i<header.length()&&header.charAt(i)==' '; i++){}
        for(; i<header.length(); i++){
            sb.append(header.charAt(i));
        }

        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(httpMethod.getMethodName()).append(SPACE).append(path);

        //parameter
        if(!parameter.isEmpty()) sb.append("?");

        for(Map.Entry<String, String> entry:parameter.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.setLength(sb.length()-1);

        sb.append(SPACE);
        sb.append(version).append(CRLF);

        //header
        for(Map.Entry<String, String> entry:header.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(CRLF);
        }

        sb.append(CRLF);

        //body
        if(body.length!=0){
            sb.append(new String(body)).append(CRLF);
        }

        return sb.toString();

    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        if(!this.httpMethod.equals(request.httpMethod)) return false;
        if(!this.path.equals(request.path)) return false;
        if(!this.parameter.equals(request.parameter)) return false;
        if(!this.header.equals(request.header)) return false;
        if(!Arrays.equals(this.body, request.body)) return false;
        if(!this.cookie.equals(request.cookie)) return false;
        return true;
    }

}
