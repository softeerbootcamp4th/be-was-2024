package webserver.mapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class RedirectHandler {

    public static void redirectPath(String redirectPath, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("\r\n");
    }
}
