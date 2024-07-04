package webserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static util.Utils.*;

public class HttpResponse {

    private byte[] body;

    public HttpResponse(String request) throws IOException {
        this.body = getFile(getUrl(request));
    }

    public byte[] toByte(String extension){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.writeBytes("HTTP/1.1 200 OK \r\n".getBytes());
        outputStream.writeBytes(("Content-Type: "+getContentType(extension)+";charset=utf-8\r\n").getBytes());
        outputStream.writeBytes(("Content-Length: " + body.length + "\r\n").getBytes());
        outputStream.writeBytes("\r\n".getBytes());
        outputStream.write(body, 0, body.length);

        return outputStream.toByteArray();

    }

}
