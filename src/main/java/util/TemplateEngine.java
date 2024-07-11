package util;

import java.io.*;
import java.util.Map;

import static util.Constants.*;

public class TemplateEngine {

    public static byte[] showAlert(String msg, String redirectUrl) {

        String alert = "<html>" +
                "<head>" +
                "<script type=\"text/javascript\">" +
                "alert('" + msg + "');" +
                "window.location.replace('" + redirectUrl + "');" +
                "</script>" +
                "</head>" +
                "</html>";

        return alert.getBytes();
    }

    public static byte[] getNotFoundPage() throws IOException {
        StringBuilder content = new StringBuilder();
        File file = new File(STATIC_PATH + PATH_ERROR + FILE_NOT_FOUND);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().getBytes();
    }

    public static byte[] renderTemplate(byte[] template, Map<String, String> data) {
        String rawTemplate = new String(template);
        for (String key : data.keySet()) {
            rawTemplate = rawTemplate.replace("{{" + key + "}}", data.get(key));
        }
        return rawTemplate.getBytes();
    }
}
