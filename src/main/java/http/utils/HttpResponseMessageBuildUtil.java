package http.utils;

import http.MyHttpResponse;
import http.cookie.MyCookie;
import http.cookie.MyCookies;
import http.enums.HttpStatusType;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * http response 메시지로부터 http response message 를 빌드하는 유틸 기능
 */
public class HttpResponseMessageBuildUtil {
    public static byte[] build(MyHttpResponse response) {
        StringBuilder builder = new StringBuilder();

        // startLine
        String version = response.getVersion();
        HttpStatusType statusType = response.getStatusType();
        // 둘 중 하나만 없어도 라인 작성 불가
        if(version == null || statusType == null) throw new RuntimeException("required argument not exist");

        int code = statusType.getStatusCode();
        String message = statusType.getMessage();
        String startLine = response.getVersion() + " " + code + " " + message + "\r\n";

        builder.append(startLine);

        // header
        for(var entries: response.getHeaders().getHeaders().entrySet()) {
            String header = entries.getKey();
            String value = entries.getValue();

            String headerLine = header + ": " + value + "\r\n";
            builder.append(headerLine);
        }

        // header - cookies
        List<String> cookieInfos = getResponseCookieInfos(response.getCookies());
        for(var cookieInfo: cookieInfos) {
            builder.append("Set-Cookie: ").append(cookieInfo);
        }

        // 빈 라인
        builder.append("\r\n");

        // 헤더 바이트 획득
        byte[] startLineAndHeaderBytes = builder.toString().getBytes(StandardCharsets.UTF_8);

        // 헤더 + 바디를 모두 포함할 수 있는 바이트 배열 생성, 반환

        byte[] httpResMessageBytes = new byte[startLineAndHeaderBytes.length + response.getBody().length];

        System.arraycopy(startLineAndHeaderBytes, 0, httpResMessageBytes, 0, startLineAndHeaderBytes.length);
        System.arraycopy(response.getBody(), 0, httpResMessageBytes, startLineAndHeaderBytes.length, response.getBody().length);

        return httpResMessageBytes;
    }

    public static List<String> getResponseCookieInfos(MyCookies cookies) {
        List<MyCookie> cookieList = cookies.getCookies();
        return cookieList.stream().map(MyCookie::toString).toList();
    }
}
