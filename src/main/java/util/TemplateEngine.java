package util;

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


}
