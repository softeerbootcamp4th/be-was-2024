package model;

import model.enums.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HttpResponse {
    private String protocolVersion;
    private HttpStatus httpStatus;
    private List<String> headers;
    private byte[] body;

    // 생성자로 사용해도 되는가, 컨버컨버터로 사용해여  하눈거
    public HttpResponse(String protocolVersion, HttpStatus httpStatus, List<String> headers, byte[] body) {
        this.protocolVersion = protocolVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


    public void sendHttpResponse(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);

        //header
        try {
            dos.writeBytes(protocolVersion + " " + httpStatus.getHttpStatusCode() +" "+ httpStatus.getHttpStatusMessage() + " \r\n");
            for (String header : headers) {
                dos.writeBytes(header);
            }
        } catch (IOException e) {
//            logger.error(e.getMessage());
        }

        //body
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
//            logger.error(e.getMessage());
        }
    }


}
