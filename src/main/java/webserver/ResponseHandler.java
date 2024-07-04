package webserver;

import type.StatusCodeType;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    private DataOutputStream dos;
    private RequestResult requestResult;

    public ResponseHandler(DataOutputStream dos, RequestResult requestResult) {
        this.dos = dos;
        this.requestResult = requestResult;
    }

    public void write() throws IOException {
        writeHeader(requestResult.getStatusCode(), requestResult.getContent().length);
        writeBody(requestResult.getContent());
    }

    public void writeHeader(StatusCodeType statusCode, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getText() + "\r\n");
        switch (statusCode) {
            // [200, 300)
            case OK -> {
                dos.writeBytes("Content-Type: " + requestResult.getContentType() + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            }
            // [300, 400)
            case FOUND -> {
                dos.writeBytes("Location: ");
                dos.write(requestResult.getContent());
            }
            // [400, 500)
            case NOT_FOUND -> dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    public void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
