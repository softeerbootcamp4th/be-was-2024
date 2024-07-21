package dto;

import constant.FileExtensionType;
import constant.HttpStatus;
import cookie.Cookie;
import cookie.SessionCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";

    private HttpStatus status;
    private Map<String, List<String>> headers;
    private List<Cookie> cookies;
    private byte[] body;

    public HttpResponse(){
        headers = new HashMap<>();
        cookies = new ArrayList<>();
    }

    // client에 HttpResponse 응답
    public void sendHttpResponse(DataOutputStream dos) throws IOException {

        makeHttpResponse(dos);
        dos.flush();
    }

    public void setRedirect(String url){
        setHttpStatus(HttpStatus.FOUND);
        addHeader(LOCATION, url);
    }

    public void setErrorResponse(HttpStatus errorStatus){
        setHttpStatus(HttpStatus.NOT_FOUND);
        addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
        addHeader(CONTENT_LENGTH, String.valueOf(ERROR_MESSAGE_404.length()));
        setBody(ERROR_MESSAGE_404.getBytes());
    }
    // HttpResponse header 생성
    private void makeHttpResponse(DataOutputStream dos) throws IOException {

        // status line 생성
        dos.writeBytes(HTTP_VERSION + " " + status.getStatusCode() + " " + status.getMessage() + CRLF);
        // header 생성
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValueList = entry.getValue();

            if (!headerValueList.isEmpty()) {
                // 첫 번째 헤더 값은 headerName: headerValue 형식으로 작성
                dos.writeBytes(headerName + ": " + headerValueList.get(0));

                // 나머지 값들은 ;로 이어붙임
                for (int i = 1; i < headerValueList.size(); i++) {
                    dos.writeBytes(";" + headerValueList.get(i));
                }
                dos.writeBytes(CRLF);
            }
        }
        for(Cookie cookie : cookies){
            dos.writeBytes(cookie.getCookieString());
        }
        dos.writeBytes(CRLF);

        // response body 생성
        dos.write(body, 0, body.length);

    }

    public void setHttpStatus(HttpStatus status) {
        this.status = status;
    }

    public void addHeader(String headerName, String headerValue) {

        if(!headers.containsKey(headerName)){
            headers.put(headerName, new ArrayList<>());
        }
        headers.get(headerName).add(headerValue);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setCookie(Cookie cookie){
        this.cookies.add(cookie);
    }


}
