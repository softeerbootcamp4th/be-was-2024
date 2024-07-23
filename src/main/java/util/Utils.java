package util;

import http.HttpStatus;

import java.io.*;
import java.util.*;

import static util.Constants.REG_EQ;
import static util.Constants.REG_SMCLN;
import static util.TemplateEngine.getNotFoundPage;

public class Utils {
    public static class ResponseWithStatus {
        public HttpStatus status;
        public byte[] body;

        ResponseWithStatus(HttpStatus status, byte[] body) {
            this.status = status;
            this.body = body;
        }
    }
    // 이너 클래스 -> 반환하니까 이너클래스랑 잘 안맞는다, 빼는게 더 나아보임

    public static ResponseWithStatus getFileContent(String path) throws IOException {
        try {

            File file = new File(path);
            byte[] bytesArray = new byte[(int) file.length()];

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                fis.read(bytesArray);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseWithStatus(HttpStatus.OK, bytesArray);
        } catch (FileNotFoundException e) {
            return new ResponseWithStatus(HttpStatus.NOT_FOUND, getNotFoundPage());
        }
    }

    public static String getContentType(String type) {
        return switch (type) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico" -> "image/x-icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> "*/*"; // 이건 작동 안함 -> 406 에러 반환
        };
    }

    public static HashMap<String, String> cookieParsing(String cookies) {
        HashMap<String, String> parsedCookies = new HashMap<>();

        if (cookies == null) return parsedCookies;
        String[] splitCookies = cookies.split(REG_SMCLN);
        for (String cookie : splitCookies) {
            cookie = cookie.strip();
            String[] nameAndValue = cookie.split(REG_EQ);

            String name = nameAndValue[0];
            String value = nameAndValue[1];

            parsedCookies.put(name, value);
        }

        return parsedCookies;
    }

    public static String getBoundary(String Content_Type) {
        String[] parameters = Content_Type.split(";");
        for (String parameter : parameters) {
            if (!parameter.trim().startsWith("boundary")) continue;
            return "--" + parameter.split("=")[1].trim();
        }
        return null;
    }
}
