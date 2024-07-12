package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RequestResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponse.class);
    private HttpRequest httpRequest;
    private DataOutputStream dos;

    public RequestResponse(HttpRequest httpRequest, DataOutputStream dos) {
        this.httpRequest = httpRequest;
        this.dos = dos;
    }

    public void redirectPath(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void setCookieAndRedirectPath(String sessionId, String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("Set-Cookie: sid=" + sessionId + "; Path=/\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    // 로그아웃 할 때 빈 세션으로 반환하게 하기 dos.writeBytes("Set-Cookie: sid=" + sessionId + "; Path=/; Max-Age=0\r\n");
    public void resetCookieAndRedirectPath(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectPath + "\r\n");
        dos.writeBytes("Set-Cookie: sid=; Path=/; Max-Age=0\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void openPath(String url) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String contentType = FileHandler.determineContentType(file.getName());

            response200Header(fileBody.length, contentType);
            responseBody(fileBody);
        }
    }

    public void openPathWithUsername(String url, String username) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String content = new String(fileBody);
            content = content.replace("{{username}}", username);
            byte[] modifiedFileBody = content.getBytes();

            String contentType = FileHandler.determineContentType(file.getName());

            response200Header(modifiedFileBody.length, contentType);
            responseBody(modifiedFileBody);
        }
    }

    public void openUserList() throws IOException {
        Map<String, User> users = Database.findAll();

        StringBuilder userListHtml = new StringBuilder();
        userListHtml.append("<html><head><title>User List</title></head><body>");
        userListHtml.append("<h1>User List</h1>");
        userListHtml.append("<ul>");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userListHtml.append("<li> ID: ").append(entry.getKey())
                    .append(" & Name: ").append(entry.getValue().getName())
                    .append(" & email: ").append(entry.getValue().getEmail().replace("%40", "@"))
                    .append("</li>");
        }
        userListHtml.append("</ul>");
        userListHtml.append("</body></html>");
        byte[] fileBody = userListHtml.toString().getBytes("UTF-8");

        response200Header(fileBody.length, "text/html");
        responseBody(fileBody);
    }


    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void response404Header() throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes("<h1>404 Not Found</h1>");
        dos.flush();
    }

    private void responseBody(byte[] body) throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    public void responseErrorPage(String html) throws IOException {
        byte[] responseBytes = html.getBytes("UTF-8");
        dos.write(("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + responseBytes.length + "\r\n" +
                "\r\n").getBytes("UTF-8"));
        dos.write(responseBytes);
        dos.flush();
    }

    public void sendErrorPage(String errorMessage, String redirectUrl) throws IOException {
        String errorPage = "<html><head><title>Error</title></head><body>" +
                "<h1>Error</h1>" +
                "<p>" + errorMessage + "</p>" +
                "<script>alert('" + errorMessage + "');" +
                "window.location.href = '" + redirectUrl + "';" +
                "</script>" +
                "</body></html>";
        responseErrorPage(errorPage);
    }
}
