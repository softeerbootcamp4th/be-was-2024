package util;

import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String getStaticPath() {
        return properties.getProperty("staticPath");
    }

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
            StringBuilder content = new StringBuilder();
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return new ResponseWithStatus(HttpStatus.OK, content.toString().getBytes());
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

    public static HashMap<String, String> cookieParsing(String cookies) {
        HashMap<String, String> parsedCookies = new HashMap<>();
        String[] splitCookies = cookies.split(";");
        for (String cookie : splitCookies) {
            cookie = cookie.strip();
            String[] nameAndValue = cookie.split("=");

            String name = nameAndValue[0];
            String value = nameAndValue[1];

            parsedCookies.put(name, value);
        }
        return parsedCookies;
    }
}
