package util;

import model.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpSender {
    /**
     * outputStream을 통해, HTTPResponse를 전송하는 로직
     *
     * @param out : outputStream
     */
    public void sendHttpResponse(OutputStream out, HttpResponse httpResponse) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            dos.writeBytes(httpResponse.getProtocolVersion() + " " + httpResponse.getHttpStatus().getHttpStatusCode()
                    + " " + httpResponse.getHttpStatus().getHttpStatusMessage() + "\r\n");
            for(Map.Entry<String,String> header : httpResponse.getHeaders().entrySet()) {
                dos.writeBytes(header.getKey() + ": " + header.getValue()+"\r\n");
            }
            dos.writeBytes("\r\n");

            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
