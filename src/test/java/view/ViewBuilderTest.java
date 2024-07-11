package view;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewBuilderTest {
    @Test
    void testBuildOk() {
        // 입력 문자열
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "<my-template if='user'> <div>login</div> </my-template>\n" +
                "<my-template if-not='user'> logout </my-template>\n" +
                "    <h1>main page</h1>\n" +
                "    <p>hello world this is hello</p>\n" +
                "<my-template if='user'> " +
                "    <div>\n" +
                "        <h2>user info</h2>\n" +
                "        <ul>\n" +
                "            <li>userId: {user.userId}</li>\n" +
                "            <li>password: {user.password}</li>\n" +
                "            <li>name: {user.name}\n</li>" +
                "            <li>email: {user.email}\n</li>" +
                "        </ul>\n" +
                "    </div>\n" +
                "</my-template>"+
                "</div>\n" +
                "</body>\n" +
                "</html>";
        Map<String, Object> items = new HashMap<>();
        items.put("user", new User("test1234", "test123", "super-shy", "super@gmail.com"));

        String result = ViewBuilder.build(template, items);
        System.out.println(result);
    }
}