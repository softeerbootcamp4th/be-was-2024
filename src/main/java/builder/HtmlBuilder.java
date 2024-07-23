package builder;

import db.PostDatabase;
import db.UserDatabase;
import handler.SessionHandler;
import model.Post;
import model.User;
import utils.ResourceUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class HtmlBuilder {
    public String generateIndexHtml(boolean isLoggedIn, String sessionId) throws IOException {
        if (isLoggedIn) {
            return generateLogInIndexHtml(sessionId);
        } else {
            return generateDefaultIndexHtml();
        }
    }

    private String generateLogInIndexHtml(String sessionId) throws IOException {
        String templateFilePath = "/index.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        User user = SessionHandler.getUser(sessionId);
        String userId = user.getUserId();
        String username = user.getName();

        String loginButtonHtml, registrationButtonText, registrationButtonHref, userNameHtml, userListHtml;

        loginButtonHtml = ""; // 로그인된 상태에서는 로그인 버튼을 숨김
        registrationButtonText = "로그아웃";
        registrationButtonHref = "/logout";
        userNameHtml = "<p class=\"user-name\">" + username + " 님</p>";
        userListHtml = "<li class=\"header__menu__item\">\n" +
                "        <a class=\"btn btn_ghost btn_size_s\" href=\"/user/list\">\n" +
                "          사용자 목록\n" +
                "        </a>\n" +
                "      </li>";

        // 문자열 대체
        template = template.replace("{username_placeholder}", userNameHtml)
                .replace("{login_button_placeholder}", loginButtonHtml)
                .replace("{registration_button_text}", registrationButtonText)
                .replace("{registration_button_href}", registrationButtonHref)
                .replace("{user_list_placeholder}", userListHtml);

        return template;
    }

    private String generateDefaultIndexHtml() throws IOException {
        String templateFilePath = "/index.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        String loginButtonHtml, registrationButtonText, registrationButtonHref, userNameHtml;

        loginButtonHtml = "<li class=\"header__menu__item\"><a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a></li>";
        registrationButtonText = "회원가입";
        registrationButtonHref = "/registration";
        userNameHtml = "";

        // 문자열 대체
        template = template.replace("{username_placeholder}", userNameHtml)
                .replace("{login_button_placeholder}", loginButtonHtml)
                .replace("{registration_button_text}", registrationButtonText)
                .replace("{registration_button_href}", registrationButtonHref)
                .replace("{user_list_placeholder}", "")
                .replace("{title_placeholder}", "");

        return template;
    }

    public String generateUserListHtml() throws IOException {
        String templateFilePath = "/user/list.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        Collection<User> userList = UserDatabase.findAll();

        // 사용자 목록을 HTML 형식으로 빌드
        StringBuilder userRows = new StringBuilder();
        int index = 1;
        for (User user : userList) {
            userRows.append("<tr>");
            userRows.append("<th scope=\"row\">").append(index).append("</th>");
            userRows.append("<td>").append(user.getUserId()).append("</td>");
            userRows.append("<td>").append(user.getName()).append("</td>");
            userRows.append("<td>").append(user.getEmail()).append("</td>");
            userRows.append("</tr>");
            index++;
        }

        // 문자열 대체
        template = template.replace("{user_list_placeholder}", userRows.toString());

        return template;
    }

    public String generatePostHtml(String userId, String title) throws IOException {
        String templateFilePath = "/post/index.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        Post post = PostDatabase.findPostByUserIdAndTitle(userId, title);

        // 문자열 대체
        template = template.replace("{title_placeholder}", post.getTitle())
                .replace("{content_placeholder}", post.getContent());

        return template;
    }

    public String generatePostListHtml() throws IOException {
        String templateFilePath = "/post/list.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        Collection<Post> postList = PostDatabase.findAll();

        // 글 목록을 HTML 형식으로 빌드
        StringBuilder postRows = new StringBuilder();
        int index = 1;
        for (Post post : postList) {
            postRows.append("<tr>");
            postRows.append("<th scope=\"row\">").append(index).append("</th>");
            postRows.append("<td>").append("<a href=\"/posts?title=").append(post.getTitle()).append("&userId=").append(post.getUserId()).append("\">").append(post.getTitle()).append("</a>").append("</td>");
            postRows.append("<td>").append(post.getUserId()).append("</td>");
            postRows.append("</tr>");
            index++;
        }

        // 문자열 대체
        template = template.replace("{post_list_placeholder}", postRows.toString());

        return template;
    }
}
