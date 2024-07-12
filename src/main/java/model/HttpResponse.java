package model;

import model.enums.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HttpResponse {
    public String getProtocolVersion() {
        return protocolVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    private String protocolVersion;
    private HttpStatus httpStatus;
    private List<String> headers;
    private byte[] body;

    public HttpResponse(String protocolVersion, HttpStatus httpStatus, List<String> headers, byte[] body) {
        this.protocolVersion = protocolVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    /**
     * outputStream을 통해, HTTPResponse를 전송하는 로직
     *
     * @param out : outputStream
     */
    public void sendHttpResponse(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            dos.writeBytes(protocolVersion + " " + httpStatus.getHttpStatusCode() + " " + httpStatus.getHttpStatusMessage() + " \r\n");
            for (String header : headers) {
                dos.writeBytes(header);
            }

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
