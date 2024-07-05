package user.common;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserJsonBuilder {

    public static User[] parseJsonToUsers(String response) {
        // JSON 부분만 추출
        String jsonResponse = extractJson(response);

        // JSON을 User 배열로 변환
        User[] users = parseJsonToUserArray(jsonResponse);

        return users;
    }

    private static String extractJson(String response) {
        // "HTTP/1.1" 이전의 JSON 부분 추출
        int index = response.indexOf("[");
        return response.substring(index, response.indexOf("]") + 1);
    }

    private static User[] parseJsonToUserArray(String jsonResponse) {
        List<User> userList = new ArrayList<>();

        // JSON 배열 문자열을 파싱
        String[] jsonObjects = jsonResponse.substring(1, jsonResponse.length() - 1).split("\\},\\{");
        for (String jsonObject : jsonObjects) {
            jsonObject = jsonObject.replace("{", "").replace("}", "").replace("\"", "");
            String[] fields = jsonObject.split(",");
            String userId = null, name = null, password = null;

            for (String field : fields) {
                String[] keyValue = field.split(":");
                if (keyValue[0].trim().equals("userId")) {
                    userId = keyValue[1].trim();
                } else if (keyValue[0].trim().equals("name")) {
                    name = keyValue[1].trim();
                } else if (keyValue[0].trim().equals("password")) {
                    password = keyValue[1].trim();
                }
            }

            // email은 JSON에 없으므로 null로 설정
            userList.add(new User(userId, password, name, null));
        }

        return userList.toArray(new User[0]);
    }
}
