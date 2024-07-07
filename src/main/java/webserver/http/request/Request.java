package webserver.http.request;

import exception.NotExistException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Method method;
    private final String path;
    private final String version = "HTTP/1.1";
    private final Map<String, String> parameter;
    private final Map<String, String> header;

    private Request(Builder builder){
        this.method = builder.method;
        this.path = builder.path;
        this.parameter = builder.parameter;
        this.header = builder.header;
    }

    public static class Builder {
        private Method method;
        private String path;
        private Map<String, String> parameter;
        private Map<String, String> header;

        public Builder(Method method, String path){
            this.method = method;
            this.path = path;
            parameter = new HashMap<>();
            header = new HashMap<>();
        }

        public Builder parameter(Map<String, String> parameter){
            this.parameter = parameter;
            return this;
        }

        public Builder header(Map<String, String> header){
            this.header = header;
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

    public String getPath(){
        return this.path;
    }

    public String getExtension(){
        return parseExtension(path);
    }

    public String getParameterValue(String key){
        return this.parameter.get(key);
    }

    public static Request parseRequest(String request) throws IOException {

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

    private static Map<String, String> parseParameterMap(String pathWithOutParameters){

        Map<String, String> parameterMap = new HashMap<>();
        String[] split = pathWithOutParameters.split("&");

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
            System.out.println(inputLine);
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        if(!this.method.equals(request.method)) return false;
        if(!this.path.equals(request.path)) return false;
        if(!this.parameter.equals(request.parameter)) return false;
        if(!this.header.equals(request.header)) return false;
        return true;
    }

}
