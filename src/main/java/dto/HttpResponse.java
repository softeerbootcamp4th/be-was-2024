package dto;

import constant.FileExtensionType;
import constant.HttpStatus;

import javax.xml.crypto.Data;
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
    private final Map<String, List<String>> headers = new HashMap<>();
    private byte[] body;

    // client에 HttpResponse 응답
    public void sendHttpResponse(DataOutputStream dos) throws IOException {

        makeHttpResponse(dos);
        dos.flush();
    }

    // HttpResponse header 생성
    private void makeHttpResponse(DataOutputStream dos) throws IOException {

        // status line 생성
        dos.writeBytes(HTTP_VERSION + " " + status.getStatusCode() + " " + status.getMessage() + " " + CRLF);

        // header 생성
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValueList = entry.getValue();

            for (String headerValue : headerValueList) {
                dos.writeBytes(headerName + ": " + headerValue + ";");
            }
            dos.writeBytes(CRLF);
        }
        dos.writeBytes(CRLF);

        // response body 생성
        dos.write(body, 0, body.length);

    }

    public void setHttpStatus(HttpStatus status) {
        this.status = status;
    }

    public void addHeader(String headerName, String headerValue) {
        List<String> headerValueList = headers.get(headerName);
        if (headerValueList == null) {
            headerValueList = new ArrayList<>();
        }
        headerValueList.add(headerValue);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
