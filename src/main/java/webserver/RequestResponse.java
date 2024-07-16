package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enumPackage.ContentType;
import webserver.enumPackage.HtmlTemplate;
import webserver.enumPackage.HttpStatus;
import webserver.enumPackage.HttpVersion;

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
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(), "Location: " + redirectPath + "\r\n", null);
    }

    public void setCookieAndRedirectPath(String sessionId, String redirectPath) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(),
                "Location: " + redirectPath + "\r\nSet-Cookie: sid=" + sessionId + "; Path=/\r\n", null);
    }

    public void resetCookieAndRedirectPath(String redirectPath) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.FOUND, ContentType.HTML.getMimeType(),
                "Location: " + redirectPath + "\r\nSet-Cookie: sid=; Path=/; Max-Age=0\r\n", null);
    }

    public void openPath(String url) throws IOException {
        if (url != null) {
            File file = new File(url);
            if (!file.exists()) {
                response404Header();
                return;
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String contentType = ContentType.fromExtension(getFileExtension(file));

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

            String contentType = ContentType.fromExtension(getFileExtension(file));

            response200Header(modifiedFileBody.length, contentType);
            responseBody(modifiedFileBody);
        }
    }

    public void openUserList() throws IOException {
        Map<String, User> users = Database.findAll();

        StringBuilder userListHtml = new StringBuilder();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userListHtml.append("<li> ID: ").append(entry.getKey())
                    .append(" & Name: ").append(entry.getValue().getName())
                    .append("</li>");
        }
        String userListContent = HtmlTemplate.USER_LIST.getTemplate().replace("{{userList}}", userListHtml.toString());
        byte[] fileBody = userListContent.getBytes("UTF-8");

        response200Header(fileBody.length, ContentType.HTML.getMimeType());
        responseBody(fileBody);
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.OK, contentType, "Content-Length: " + lengthOfBodyContent + "\r\n", null);
    }

    public void response404Header() throws IOException {
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.NOT_FOUND, ContentType.HTML.getMimeType(), null, "<h1>404 Not Found</h1>".getBytes("UTF-8"));
    }

    private void responseBody(byte[] body) throws IOException {
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    public void responseErrorPage(String html) throws IOException {
        byte[] responseBytes = html.getBytes("UTF-8");
        sendResponse(HttpVersion.HTTP_1_1, HttpStatus.OK, ContentType.HTML.getMimeType(), "Content-Length: " + responseBytes.length + "\r\n", responseBytes);
    }

    public void sendErrorPage(String errorMessage, String redirectUrl) throws IOException {
        String errorPage = HtmlTemplate.ERROR_PAGE.getTemplate()
                .replace("{{errorMessage}}", errorMessage)
                .replace("{{redirectUrl}}", redirectUrl);
        responseErrorPage(errorPage);
    }

    private void sendResponse(HttpVersion version, HttpStatus status, String contentType, String headers, byte[] body) throws IOException {
        dos.writeBytes(version.getVersion() + " " + status.toString() + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        if (headers != null) {
            dos.writeBytes(headers);
        }
        dos.writeBytes("\r\n");
        if (body != null) {
            dos.write(body);
        }
        dos.flush();
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return lastIndex == -1 ? "" : name.substring(lastIndex + 1);
    }
}
