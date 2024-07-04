package webserver.response;

import webserver.request.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static util.Utils.*;

public class HttpResponse {

    private byte[] body;

    public HttpResponse(HttpRequest request) throws IOException {
        this.body = getFile(request.getPath().get());
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
