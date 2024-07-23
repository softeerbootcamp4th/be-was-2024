package webserver.http;

import webserver.http.enums.StatusCode;
import webserver.http.response.PageBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * response에 대한 클래스
 */
public class HttpResponse{
    /**
     * response status code
     */
    private StatusCode statusCode;
    /**
     * header들에 대한 map
     */
    private Map<String, String> headers;
    /**
     * response의 body
     */
    private byte[] body;

    /**
     * header map을 기반으로 header 문자열을 반환
     * @return header string
     */
    public String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append(statusCode.getStartline());
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            header.append(entry.getKey());
            header.append(": ");
            header.append(entry.getValue());
            header.append("\r\n");
        }
        header.append("\r\n");
        return header.toString();
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getHeadersMap() {
        return headers;
    }

    /**
     * HttpResponse class에 대한 생성자
     * <p>
     *     builder를 이용하여 response를 생성한다.
     * </p>
     */
    private HttpResponse(ResponseBuilder responseBuilder) {
        this.statusCode = responseBuilder.statusCode;
        this.headers = responseBuilder.headers;
        this.body = responseBuilder.body;
    }


    /**
     * HttpResponse에 대한 builder class
     */
    public static class ResponseBuilder{
        private StatusCode statusCode;
        private byte[] body;
        private Map<String, String> headers = new HashMap<>();

        public ResponseBuilder(int statusCode){
            this.statusCode = StatusCode.valueOfCode(statusCode);
            if(this.statusCode == null) this.statusCode = StatusCode.CODE404;
        }

        public ResponseBuilder addheader(String key, String value){
            headers.put(key, value);
            return this;
        }

        public ResponseBuilder setBody(byte[] body){
            this.body = body;
            return this.addheader("Content-Length", String.valueOf(body.length));
        }

        public HttpResponse build(){
            return new HttpResponse(this);
        }
    }
}
