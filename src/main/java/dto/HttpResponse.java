package dto;

import constant.FileExtensionType;
import constant.HttpResponseAttribute;
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

/**
 * client에게 응답할 HttpResponse 정보를 저장하는 클래스
 */
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

    /**
     * HttpResponse를 client에게 응답한다.
     *
     * @param dos : client에게 응답할 떄 사용하는 OutputStream 객체
     * @throws IOException : I/O 작업 시 발생
     */
    public void sendHttpResponse(DataOutputStream dos) throws IOException {

        makeHttpResponse(dos);
        dos.flush();
    }

    /**
     * redirect 응답 정보를 저장한다.
     *
     * @param url : redirect url
     */
    public void setRedirect(String url){
        setHttpStatus(HttpStatus.FOUND);
        addHeader(HttpResponseAttribute.LOCATION.getValue(), url);
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

    /**
     * HttpResponse의 헤더 값을 설정한다.
     *
     * @param headerName : 헤더 속성 이름
     * @param headerValue : 헤더 값
     */
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
