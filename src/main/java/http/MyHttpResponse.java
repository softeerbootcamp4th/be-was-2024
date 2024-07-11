package http;

import config.AppConfig;
import http.cookie.MyCookies;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReadUtil;
import view.ViewBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyHttpResponse {
    private static Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);

    private final String version;
    private HttpStatusType statusInfo;
    private final MyHttpHeaders headers;
    private final MyCookies cookies;

    private byte[] body;

    public MyHttpResponse(String version) {
        this.version = version;
        this.headers = new MyHttpHeaders();
        this.body = new byte[0];
        this.cookies = new MyCookies();
    }

    /**
     * 쿠키 모음 객체를 반환한다.
     * @return
     */
    public MyCookies getCookies() {
        return cookies;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatusType getStatusType() {
        return statusInfo;
    }

    /**
     * 헤더 객체를 반환한다. 쿠키를 설정하고 싶은 경우, 헤더 대신 getCookies를 사용.
     * @return
     */
    public MyHttpHeaders getHeaders() {
        return headers;
    }

    /**
     * 응답 바디 부분을 반환한다. (직접 변경은 권장 X)
     * @return 바이트로 구성된 바디 부분
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * 응답 상태를 지정한다
     * @param statusInfo 응답 상태
     */
    public void setStatusInfo(HttpStatusType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public HttpStatusType getStatusInfo() {
        return statusInfo;
    }

    /**
     * body에 데이터를 쓴다. Content-Length 헤더를 함께 업데이트한다.
     * @param body 작성할 byte 데이터
     */
    public void setBody(byte[] body) {
        this.body = body != null ? body : new byte[0];
        getHeaders().putHeader("Content-Length", String.valueOf(this.body.length));
    }

    /**
     * <pre>
     * body에 데이터를 쓴다. Content-Length 헤더를 함께 업데이트한다.
     * 문자열을 바이트로 변환하여 body에 작성한다.
     * </pre>
     * @param body String 타입의 바디
     */
    public void setBody(String body) {
        byte[] bodyBytes = body.getBytes();
        this.setBody(bodyBytes);
    }

    /**
     * location 경로로 redirect 한다.
     * @param location redirect 할 경로
     */
    public void redirect(String location) {
        setStatusInfo(HttpStatusType.FOUND);
        getHeaders().putHeader("Location", location);
    }

    /**
     * 정적 데이터를 클라이언트 측에 보낸다
     * @param path 정적 데이터의 경로
     */
    public void send(String path) {
        try {
            byte[] target = FileReadUtil.read(AppConfig.STATIC_BASE_PATH + path);
            setBody(target);
        } catch (Exception e) {
            // send 메서드가 실패하는 것은 서버 내부적인 문제가 발생했거나 파일이 없는 경우.
            // send의 파라미터는 완전히 개발자가 관리하는 영역이므로,
            // 문제 발생 시 INTERNAL_SERVER_ERROR로 취급한다.
            logger.error(e.getMessage());
            // 내부 오류가 발생했으므로 기존 메시지 내용 초기화
            setBody(new byte[0]);
            setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }
}
