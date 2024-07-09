package common;

import model.User;

import java.util.Collection;

/**
 * User 리스트를 Json으로 반환하기 위한 빌터 클래스
 */
public class JsonBuilder {

    public static String buildJsonResponse(Collection<User> users) {
        StringBuilder jsonResponse = new StringBuilder("[");
        for (User user : users) {
            jsonResponse.append("{")
                    .append("\"userId\":\"").append(user.getUserId()).append("\",")
                    .append("\"name\":\"").append(user.getName()).append("\",")
                    .append("\"password\":\"").append(user.getPassword()).append("\"")
                    .append("},");
        }
        if (jsonResponse.length() > 1) {
            jsonResponse.setLength(jsonResponse.length() - 1); // 마지막 쉼표 제거
        }
        jsonResponse.append("]");
        return jsonResponse.toString();
    }
}
