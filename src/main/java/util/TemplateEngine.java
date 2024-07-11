package util;

import java.util.Map;

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

    public static byte[] renderTemplate(byte[] template, Map<String, String> data) {
        String rawTemplate = new String(template);
        for (String key : data.keySet()) {
            rawTemplate = rawTemplate.replace("{{" + key + "}}", data.get(key));
        }
        System.out.println("rawTemplate = " + rawTemplate);
        return rawTemplate.getBytes();
    }

}
