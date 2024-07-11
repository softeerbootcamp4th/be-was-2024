package util;

import model.User;

import java.io.IOException;
import java.util.List;

public class DynamicHtmlUtil {

    private DynamicHtmlUtil() {
    }

    public static final String USER_NAME_TAG = "<!--USER-ID-->";
    public static final String USER_LIST_TAG = "<!--USER-LIST-->";
    public static final String LOGIN_BUTTON_TAG = "<!--LOGIN-BUTTON-->";
    public static final String LOGIN_BUTTON_HTML = "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>";

    public static String generateUserListHtml(String path, List<User> users) throws IOException {
        String body = new String(IOUtil.readBytesFromFile(false, path));
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
        return body.replace(USER_LIST_TAG, sb.toString());
    }

    public static String generateUserIdHtml(String userId){
        return "<li>" + userId + "님, 환영합니다.</a></li>";
    }
}
