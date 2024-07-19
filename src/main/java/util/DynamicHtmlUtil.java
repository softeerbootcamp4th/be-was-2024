package util;

import model.Article;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class DynamicHtmlUtil {

    private DynamicHtmlUtil() {
    }

    public static final String USER_NAME_TAG = "<!--USER-ID-->";
    public static final String USER_LIST_TAG = "<!--USER-LIST-->";
    public static final String ARTICLES_TAG =  "<!--ARTICLES-->";
    public static final String LOGIN_BUTTON_TAG = "<li><a id=\"optional_login_button\"";
    public static final String LOGIN_BUTTON_INVISIBLE = "<li><a id=\"optional_login_button\" style=\"display:none;\"";

    /**
     * 유저 목록 조회 후 HTML로 변환
     * @param users
     * @return String
     */
    public static String generateUserListHtml(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append("<tr>")
                    .append("<th scope=\"row\">").append(users.indexOf(user) + 1).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    /**
     * 게시글 목록 조회 후 HTML로 변환
     * @param articles
     * @return String
     */
    public static String generateArticlesHtml(List<Article> articles) {
        StringBuilder sb = new StringBuilder();
        try{
            for(Article article : articles){
                sb.append("<tr>")
                        .append("<th scope=\"row\">").append(articles.indexOf(article) + 1).append("</th>")
                        .append("<td>").append(article.getAuthorName()).append("</td>")
                        .append("<td>").append(article.getTitle()).append("</td>")
                        .append("<td>").append(article.getContent()).append("</td>");

                File file = new File(article.getImagePath());
                if(file.exists()){
                    byte[] imageBytes = Files.readAllBytes(file.toPath());
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    sb.append("<td>").append("<img src=\"data:image/png;base64,").append(imageBase64)
                            .append("\" alt=\"Article Image\" style=\"width:100px; height:auto;\">").append("</td>");
                }
                sb.append("</tr>");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 로그인된 사용자에게 제공되는 HTML 반환
     * @param userId
     * @return String
     */
    public static String generateUserIdHtml(String userId){
        return "<li>" + userId + "님, 환영합니다.</a></li>";
    }
}
