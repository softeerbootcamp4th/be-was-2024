package webserver.response;

import webserver.request.Path;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.Utils.*;

public class HttpResponse {

    private int status;
    private byte[] body;
    private Map<String, String> headers;

    public HttpResponse(int status, Path path) throws IOException {
        this.status = status;
        this.body = getFile(path.get());
        this.headers = new HashMap<>();
    }

    public byte[] toByte(String extension, String redirection){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.writeBytes(("HTTP/1.1 "+status+" OK \r\n").getBytes());
        outputStream.writeBytes(("Content-Type: "+getContentType(extension)+";charset=utf-8\r\n").getBytes());
        outputStream.writeBytes(("Content-Length: " + body.length + "\r\n").getBytes());
        if(status==302) outputStream.writeBytes(("Location: "+redirection).getBytes());
        outputStream.writeBytes("\r\n".getBytes());
        outputStream.write(body, 0, body.length);

        return outputStream.toByteArray();

    }

}
