package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public static void response(OutputStream outputStream, String request, String extension) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        HttpResponse httpResponse = new HttpResponse(request);
        dos.write(httpResponse.toByte(extension));
    }

}
