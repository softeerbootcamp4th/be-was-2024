package util;

import http.HttpResponse;
import http.HttpStatus;

import java.io.*;
import java.util.Map;

import static util.Constants.*;

public class TemplateEngine {

    public static HttpResponse showAlert(String msg, String redirectUrl) {
        String alert = "<html>" +
                "<head>" +
                "<script type=\"text/javascript\">" +
                "alert('" + msg + "');" +
                "window.location.replace('" + redirectUrl + "');" +
                "</script>" +
                "</head>" +
                "</html>";
        return new HttpResponse()
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addStatus(HttpStatus.BAD_REQUEST)
                .addBody(alert.getBytes());
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
