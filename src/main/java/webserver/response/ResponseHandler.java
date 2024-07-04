package webserver.response;

import webserver.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public static void response(OutputStream outputStream, HttpRequest request) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        HttpResponse httpResponse = new HttpResponse(request);
        dos.write(httpResponse.toByte(request.getPath().getExtension()));
    }

}
