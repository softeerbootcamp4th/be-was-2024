package http.utils;

import http.MyHttpResponse;
import http.enums.HttpStatusType;

import java.nio.charset.StandardCharsets;

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

        // 빈 라인
        builder.append("\r\n");

        // body
        if(response.getBody() != null)
            builder.append(new String(response.getBody()));
        return builder.toString().getBytes();
    }
}
