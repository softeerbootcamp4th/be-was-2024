package http.utils;

import http.MyHttpResponse;
import http.enums.HttpStatusType;

import java.nio.charset.StandardCharsets;

public class HttpResponseMessageBuildUtil {
    public static byte[] build(MyHttpResponse response) {
        StringBuilder builder = new StringBuilder();

        // startLine
        HttpStatusType statusType = response.getStatusType();
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
            builder.append(new String(response.getBody(), StandardCharsets.UTF_8));
        return builder.toString().getBytes();
    }
}
