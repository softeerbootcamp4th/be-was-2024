package webserver.http.response;

import model.post.Post;
import model.post.PostDAO;
import model.user.User;
import model.user.UserDAO;
import webserver.http.enums.StatusCode;
import webserver.util.HtmlFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        page = page.replace("{MAIN}", buildMainBody(postid) );

        return page.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] buildLoggedoutPage(String postid) throws IOException {
        String page = HtmlFiles.readHtmlString(HtmlFiles.MAIN_PAGE);

        page = page.replace("{MAIN}", buildMainBody(postid) );

        return page.getBytes(StandardCharsets.UTF_8);
    }

    public static String buildMainBody(String postid) throws IOException {
        PostDAO postDAO = new PostDAO();
        int id = isNumeric(postid) ? Integer.parseInt(postid) : postDAO.getLastIndex();
        Post post = postDAO.getPost(id);
        if(post == null){
            return "글이 존재하지 않습니다.";
        }
        String postbody = buildPostBody(post);

        int previd = postDAO.getPrevPostIndex(id);
        int nextid = postDAO.getNextPostIndex(id);
        String nav = buildNav(previd,nextid);
        return postbody + nav;
    }

    public static String buildPostBody(Post post) throws IOException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(post.getUserid());
        String postbody = HtmlFiles.readHtmlString(HtmlFiles.POST)
                .replace("{POST_ACCOUNT}", user.getName());
        if(post.getImgpath() !=null){
            String path = post.getImgpath().replace("./src/main/resources/static","/resource");
            postbody = postbody.replace("{IMG_FIELD}",
                    "<img class=\"post__img\" src = "+path +" />");
        }
        else postbody = postbody.replace("{IMG_FIELD}","");
        postbody = postbody.replace("{POST_TEXT}", post.getText());
        return postbody;
    }

    public static String buildNav(int previd, int nextid) throws IOException {
        String nav = HtmlFiles.readHtmlString(HtmlFiles.NAV);
        if (previd ==0){
            // 정규 표현식 패턴 정의
            String regex = "<a\\s+class=\"nav__menu__item__btn\"\\s+href=\\s*\\{PREV_POST\\}\\s*>.*?<\\/a>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

            // 정규 표현식을 사용하여 내용을 지우고 클릭 불가능하게 변경
            Matcher matcher = pattern.matcher(nav);
            nav = matcher.replaceAll("<a class=\"nav__menu__item__btn\" href=\"#\" style=\"pointer-events: none;\"></a>");
        } else  nav = nav.replace("{PREV_POST}", "/post/"+ previd);

        if(nextid == 0){
            // 정규 표현식 패턴 정의
            String regex = "<a\\s+class=\"nav__menu__item__btn\"\\s+href=\\s*\\{NEXT_POST\\}\\s*>.*?<\\/a>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

            // 정규 표현식을 사용하여 내용을 지우고 클릭 불가능하게 변경
            Matcher matcher = pattern.matcher(nav);
            nav = matcher.replaceAll("<a class=\"nav__menu__item__btn\" href=\"#\" style=\"pointer-events: none;\"></a>");
        }else nav = nav = nav.replace("{NEXT_POST}", "/post/"+ nextid);

        return nav;
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