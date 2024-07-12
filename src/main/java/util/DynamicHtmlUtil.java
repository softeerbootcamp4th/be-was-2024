package util;

import model.User;

import java.util.List;

public class DynamicHtmlUtil {

    private DynamicHtmlUtil() {
    }

    public static final String USER_NAME_TAG = "<!--USER-ID-->";
    public static final String USER_LIST_TAG = "<!--USER-LIST-->";
    public static final String LOGIN_BUTTON_TAG = "<li><a id=\"optional_login_button\"";
    public static final String LOGIN_BUTTON_INVISIBLE = "<li><a id=\"optional_login_button\" style=\"display:none;\"";

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

    public static String generateUserIdHtml(String userId){
        return "<li>" + userId + "님, 환영합니다.</a></li>";
    }
}
