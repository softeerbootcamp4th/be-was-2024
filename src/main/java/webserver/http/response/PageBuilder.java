package webserver.http.response;

import webserver.http.HtmlFiles;
import webserver.http.enums.StatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PageBuilder {

    public static byte[] buildLoggedinPage(String username) throws IOException {
        String page = Files.readString(new File(HtmlFiles.login_success).toPath());
        page = page.replace("{USERNAME}", username );
        return page.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] buildErrorPage(StatusCode errorcode) {
        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>" + errorcode.getDescription() + "</title>" +
                "</head>" +
                "<body>" +
                "<h1 style=\"text-align: center\">"+ errorcode.getCode() + " " + errorcode.getDescription() + "</h1>" +
                "</body>" +
                "</html>";
       return body.getBytes(StandardCharsets.UTF_8);
    }
}
