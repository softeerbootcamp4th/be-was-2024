package util;

import http.HttpStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static class ResponseWithStatus {
        public HttpStatus status;
        public byte[] body;

        ResponseWithStatus(HttpStatus status, byte[] body) {
            this.status = status;
            this.body = body;
        }
    }

    public static ResponseWithStatus getFileContent(String path) throws IOException {
        try {
            StringBuilder file = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String fileLine = br.readLine();

            while (fileLine != null) {
                file.append(fileLine);
                fileLine = br.readLine();
            }
            return new ResponseWithStatus(HttpStatus.OK, file.toString().getBytes());
        } catch (FileNotFoundException e) {
            String notFound = "<h1>Page Not Found</h1>";
            return new ResponseWithStatus(HttpStatus.NOT_FOUND, notFound.getBytes());
        }
    }

    public static String getContentType(String type) {
        return switch (type) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico" -> "image/vnd.microsoft.icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> "*/*"; // 이건 작동 안함 -> 406 에러 반환
        };
    }
}
