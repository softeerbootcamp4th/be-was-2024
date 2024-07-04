package webserver;

import type.StatusCodeType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ResponseHandler {
    private DataOutputStream dos;
    private RequestResult requestResult;

    public ResponseHandler(DataOutputStream dos, RequestResult requestResult) {
        this.dos = dos;
        this.requestResult = requestResult;
    }

    public void write() throws IOException {
        HashMap<String, String> responseHeader = requestResult.getResponseHeader();
        writeHeader(requestResult.getStatusCode(), responseHeader);
        writeBody(requestResult.getBodyContent());
    }

    public void writeHeader(StatusCodeType statusCode, HashMap<String, String> responseHeader) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getText() + "\r\n");
        switch (statusCode) {
            // [200, 300)
            case OK -> write200Header(responseHeader);
            // [300, 400)
            case FOUND -> write302Header(responseHeader);
            // [400, 500)
            case BAD_REQUEST -> write400Header(responseHeader);
            case NOT_FOUND -> write404Header(responseHeader);
        }
        dos.writeBytes("\r\n");
    }

    private void write200Header(HashMap<String, String> responseHeader) throws IOException {
        dos.writeBytes("Content-Type: " + responseHeader.get("Content-Type") + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + responseHeader.get("Content-Length") + "\r\n");
    }

    private void write302Header(HashMap<String, String> responseHeader) throws IOException {
        dos.writeBytes("Location: " + responseHeader.get("Location") + "\r\n");
    }

    private void write400Header(HashMap<String, String> responseHeader) throws IOException {
        dos.writeBytes("Content-Type: " + responseHeader.get("Content-Type") + ";charset=utf-8\r\n");
    }

    private void write404Header(HashMap<String, String> responseHeader) throws IOException {
        dos.writeBytes("Content-Length: " + responseHeader.get("Content-Length") + "\r\n");
    }

    public void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
