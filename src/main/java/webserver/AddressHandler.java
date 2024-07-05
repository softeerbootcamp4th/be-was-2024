package webserver;

import model.User;

import java.io.DataOutputStream;
import java.io.IOException;

import static db.Database.addUser;

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

    public static User createUserFromUrl(String urlPath) {
        int parameterIndex = urlPath.indexOf("?");
        if (parameterIndex != -1) {
            String[] userInfo = urlPath.substring(parameterIndex + 1).split("&");
            return new User(
                    userInfo[0].substring(userInfo[0].indexOf("=") + 1),
                    userInfo[1].substring(userInfo[1].indexOf("=") + 1),
                    userInfo[2].substring(userInfo[2].indexOf("=") + 1),
                    userInfo[3].substring(userInfo[3].indexOf("=") + 1)
            );
        }
        return null;
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
