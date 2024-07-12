package webserver.http.response;

import db.Database;
import model.User;
import webserver.http.enums.StatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PageBuilder {
    public static byte[] buildUserList() throws IOException {

        StringBuilder userlist = new StringBuilder();
        for( User user : Database.findAll()){
            userlist.append("<li>User : ").append(user.getName());
            userlist.append("<ul>");
            userlist.append("<li>ID : ").append(user.getUserId()).append("</li>");
            userlist.append("<li>Password : ").append(user.getPassword()).append("</li>");
            userlist.append("<li>Email : ").append(user.getEmail()).append("</li>");
            userlist.append("</ul>");
            userlist.append("</li>\r\n");
        }

        String page = Files.readString(new File(HtmlFiles.user_list).toPath());
        page = page.replace("{Userlist}", userlist );

        return page.getBytes("UTF-8");
    }

    public static byte[] buildRegistrationFailedPage(String errorMessage) throws IOException {
        String page = Files.readString(new File(HtmlFiles.register_failed).toPath());
        page = page.replace("{ErrorMessage}", errorMessage );
        return page.getBytes("UTF-8");
    }

    public static byte[] buildRegistrationPage() throws IOException{
        return Files.readAllBytes(new File(HtmlFiles.register).toPath());
    }

    public static byte[] buildFailedLoginPage() throws IOException {
        return Files.readAllBytes(new File(HtmlFiles.login_failed).toPath());
    }

    public static byte[] buildLoginPage() throws IOException {
        return Files.readAllBytes(new File(HtmlFiles.login).toPath());
    }
    public static byte[] buildMainPage() throws IOException {
        return Files.readAllBytes(new File(HtmlFiles.main_page).toPath());
    }

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
