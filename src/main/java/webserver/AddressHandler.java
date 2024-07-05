package webserver;

import model.User;

import java.io.DataOutputStream;
import java.io.IOException;

import static db.Database.addUser;
import static model.User.createUserFromUrl;

public class AddressHandler {

    public static String getFilePath(String urlPath, DataOutputStream dos) throws IOException {
        String filePath = "src/main/resources/static" + urlPath;
        if (urlPath.equals("/registration")) {
            filePath = "src/main/resources/static/registration/index.html";
        }else if(urlPath.startsWith("/user/create")){
            handleUserCreation(urlPath, dos);
        }
        return filePath;
    }


    public static void handleUserCreation(String urlPath, DataOutputStream dos) throws IOException {
        User user = createUserFromUrl(urlPath);
        if (user != null) {
            addUser(user);
            String redirectUrl = "/index.html";
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        }
    }

}
