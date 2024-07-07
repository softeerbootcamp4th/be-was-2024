package webserver;

import db.Database;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;

public class AddressHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static String getFilePath(String urlPath, DataOutputStream dos) throws IOException {
        String filePath = staticResourceDir + urlPath;
        if (urlPath.equals("/registration")) {
            filePath = staticResourceDir + "/registration/index.html";
        } else if (urlPath.startsWith("/user/create")) {
            handleUserCreation(urlPath);
            redirectPath("/index.html", dos);
        }
        return filePath;
    }

    public static void redirectPath(String redirectPath, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void handleUserCreation(String urlPath){
        User user = User.createUser(urlPath);
        if (user != null) {
            Database.addUser(user);
        }
    }

}
