package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.StatusCodeType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private DataOutputStream dos;
    private RequestResult requestResult;

    private static final String CRLF = "\r\n";

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
        dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getText() + CRLF);
        writeWithResponseHeader(responseHeader);
        dos.writeBytes(CRLF);
    }

    private void writeWithResponseHeader(HashMap<String, String> responseHeader) throws IOException {
        responseHeader.forEach((key, value) -> {
            try {
                dos.writeBytes(key + ": " + value + CRLF);
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
        });
    }

    public void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
