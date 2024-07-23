package webserver.http.response;

import model.post.Post;
import model.post.PostDAO;
import model.user.User;
import model.user.UserDAO;
import webserver.http.enums.StatusCode;
import webserver.util.HtmlFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PageBuilder {
    public static byte[] buildUserList() throws IOException {
        UserDAO userDAO = new UserDAO();
        StringBuilder userlist = new StringBuilder();
        for( User user : userDAO.getUserList()){
            userlist.append("<li>User : ").append(user.getName());
            userlist.append("<ul>");
            userlist.append("<li>ID : ").append(user.getUserId()).append("</li>");
            userlist.append("<li>Password : ").append(user.getPassword()).append("</li>");
            userlist.append("<li>Email : ").append(user.getEmail()).append("</li>");
            userlist.append("</ul>");
            userlist.append("</li>\r\n");
        }

        String page = HtmlFiles.readHtmlString(HtmlFiles.USER_LIST);
        page = page.replace("{Userlist}", userlist );

        return page.getBytes("UTF-8");
    }

    public static byte[] buildRegistrationFailedPage(String errorMessage) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.REGISTER_FAILED);
        page = page.replace("{ErrorMessage}", errorMessage );
        return page.getBytes("UTF-8");
    }

    public static byte[] buildLoggedinPage(String username, String postid) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.LOGIN_SUCCESS);
        page = page.replace("{USERNAME}", username );

        PostDAO postDAO = new PostDAO();
        int id = isNumeric(postid) ? Integer.parseInt(postid) : postDAO.getLastIndex();
        Post post = postDAO.getPost(id);


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

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}