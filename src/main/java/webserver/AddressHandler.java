package webserver;

import db.Database;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;

public class AddressHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static String getFilePath(HttpRequest request, DataOutputStream dos) throws IOException {
        String method = request.getMethod();
        String urlPath = request.getUrl();
        byte[] body = request.getBody();
        String filePath = staticResourceDir + urlPath;
        if (urlPath.equals("/registration")) {
            filePath = staticResourceDir + "/registration/index.html";
        } else if (urlPath.startsWith("/user/create")) {
            User.createUser((new String(body, "UTF-8")));
            redirectPath("/index.html", dos);
        }
        return filePath;
    }

    public static void redirectPath(String redirectPath, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("\r\n");
    }

}
