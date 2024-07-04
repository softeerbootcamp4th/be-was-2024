package webserver.response;

import webserver.request.Path;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public static void response(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        dos.write(httpResponse.toByte());
    }

}
