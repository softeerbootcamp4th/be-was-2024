package util;

import java.nio.charset.StandardCharsets;

public class TemplateEngine {

    public static byte[] showAlert(String msg, String redirectUrl) {
        StringBuilder alert = new StringBuilder();

        alert.append("<html>");
        alert.append("<head>");
        alert.append("<script type=\"text/javascript\">");
        alert.append("alert('").append(msg).append("');");
        alert.append("window.location.replace('").append(redirectUrl).append("');");
        alert.append("</script>");
        alert.append("</head>");
        alert.append("</html>");

        return alert.toString().getBytes(StandardCharsets.UTF_8);
    }
}
