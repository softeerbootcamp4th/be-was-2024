package webserver.http.request;

import exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    private static final String CRLF = "\r\n";
    private static final char SPACE = ' ';
    public static final Logger logger = LoggerFactory.getLogger(Request.class);
    private final Method method;
    private final String path;
    private final String version = "HTTP/1.1";
    private final Map<String, String> parameter;
    private final Map<String, String> header;
    private final byte[] body;

    private Request(Builder builder){
        this.method = builder.method;
        this.path = builder.path;
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.body = builder.body;
    }

    public static class Builder {
        private Method method;
        private String path;
        private Map<String, String> parameter;
        private Map<String, String> header;
        private byte[] body;

        public Builder(Method method, String path){
            this.method = method;
            this.path = path;
            this.body = new byte[0];
            parameter = new LinkedHashMap<>();
            header = new LinkedHashMap<>();
        }

        public Builder parameter(Map<String, String> parameter){
            this.parameter = parameter;
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
            return new Request(this);
        }

    }

    public Method getMethod(){
        return this.method;
    }

    public Map<String, String> getParameter(){
        return this.parameter;
    }

    public byte[] getBody(){
        return this.body;
    }

    public String getPath(){
        return this.path;
    }

    public Map<String, String> getHeader(){
        return this.header;
    }

    public String getExtension(){
        return parseExtension(path);
    }

    public String getParameterValue(String key){
        return this.parameter.get(key);
    }

    public String getHeadValue(String key){
        return this.header.get(key);
    }

    public static Request parseRequestWithoutBody(String request) throws IOException {

        String[] inputLines = request.split("\n");
        String[] startLine = inputLines[0].split(" ");

        return new Builder(Method.fromMethodName(startLine[0]), parsePath(startLine[1]))
                .parameter(parseParameterMap(parseParameter(startLine[1])))
                .header(parseHeaderMap(request))
                .build();
    }

    private static String parsePath(String path){
        return path.split("\\?")[0];
    }

    private static String parseParameter(String path){
        String[] split = path.split("\\?");
        if(split.length>1) return path.split("\\?")[1];
        return "";
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
        StringBuilder sb = new StringBuilder(header.split(" ")[0]);
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private static String parseHeaderValue(String header){
        return header.split(" ")[1];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(method.getMethodName()).append(SPACE).append(path);

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
        if(!this.method.equals(request.method)) return false;
        if(!this.path.equals(request.path)) return false;
        if(!this.parameter.equals(request.parameter)) return false;
        if(!this.header.equals(request.header)) return false;
        if(!Arrays.equals(this.body, request.body)) return false;
        return true;
    }

}
