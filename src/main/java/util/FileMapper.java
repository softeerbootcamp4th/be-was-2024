package util;

import db.UserDatabase;
import model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static util.constant.StringConstants.*;


public class FileMapper {

    public static byte[] getByteConvertedFile(String path,String userId) throws IOException {
        User user = UserDatabase.findUserById(userId).orElse(null);
        File file = new File(RESOURCE_PATH + path);
        InputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();


        String htmlContent = new String(allBytes, StandardCharsets.UTF_8);

        // 유저가 로그인 된 상태인 경우와 안된 상태 분리
        System.out.println("userId@@@@@@@@@@@@@@@@ = " + userId);
        if (user!=null) {
            htmlContent = htmlContent.replace(DYNAMIC_CONTENT_IS_LOGIN, makeDynamicContentIsLoginContentWithName(user.getName()));
        }
        else{
            htmlContent = htmlContent.replace(DYNAMIC_CONTENT_IS_NOT_LOGIN, DYNAMIC_CONTENT_IS_NOT_LOGIN_CONTENT);

        }

        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }
}
