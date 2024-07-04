package webserver.response;

import webserver.request.Path;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public static void response(int status, OutputStream outputStream, Path path, String redirection) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        HttpResponse httpResponse = new HttpResponse(status, path);
        dos.write(httpResponse.toByte(path.getExtension(), redirection));
    }

}
