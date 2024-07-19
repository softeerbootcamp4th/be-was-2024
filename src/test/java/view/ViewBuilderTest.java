package view;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewBuilderTest {
    @Test
    @DisplayName("정상적인 결과에는 my-template 태그가 사라져야 한다")
    void mustNoMy_TemplateTagInResult() {
        // 입력 문자열
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "<my-template if='@auth_user'> <div>login</div> </my-template>\n" +
                "<my-template if-not='@auth_user'> logout </my-template>\n" +
                "    <h1>main page</h1>\n" +
                "    <p>hello world this is hello</p>\n" +
                "<my-template if='@auth_user'> " +
                "    <div>\n" +
                "        <h2>user info</h2>\n" +
                "        <ul>\n" +
                "            <li>userId: {user.userId}</li>\n" +
                "            <li>password: {user.password}</li>\n" +
                "            <li>name: {@auth_user.name}\n</li>" +
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
        Assertions.assertThat(result).doesNotContain("my-template");

        // 정상적으로 변환되었다면 my-template 정보가 모두 사라져야 한다.
    }

    @Test
    @DisplayName("if 조건을 만족하면 태그 내 결과가 템플릿이 채워져 출력된다")
    void returnTemplatedMessageIf_IfConditionSatisfied() {
        String template = "<div>not template</div>" +
                "< my-template if='user' >" +
                "<div>{user.name}</div>" +
                "<div>{user.email}</div>" +
                "</ my-template >";

        Map<String, Object> items = new HashMap<>();
        items.put("user", new User("test1234", "test123", "super-shy", "super@gmail.com"));
        String expected = "<div>not template</div><div>super-shy</div><div>super@gmail.com</div>";

        String result = ViewBuilder.build(template, items);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("if 조건을 만족하지 않으면 해당 구간은 출력되지 않는다")
    void returnEmptyStringIf_IfConditionNotSatisfied() {
        String template = "<div>not template</div>" +
                "< my-template if='invalid' >" +
                "<div>{user.name}</div>" +
                "<div>{user.email}</div>" +
                "</ my-template >";

        Map<String, Object> items = new HashMap<>();
        items.put("user", new User("test1234", "test123", "super-shy", "super@gmail.com"));
        String expected = "<div>not template</div>";

        String result = ViewBuilder.build(template, items);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("if-not 조건을 만족하면 태그 내 결과가 템플릿이 채워져 출력된다")
    void returnTemplatedMessageIf_IfNotConditionSatisfied() {
        String template = "<div>not template</div>" +
                "< my-template if-not='@auth_user' >" +
                "<div>login</div>" +
                "</ my-template >";

        Map<String, Object> items = new HashMap<>();
        String expected = "<div>not template</div><div>login</div>";

        String result = ViewBuilder.build(template, items);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("if-not 조건을 만족하지 않으면 해당 구간은 출력되지 않는다")
    void returnEmptyStringIf_IfNotConditionNotSatisfied() {
        String template = "<div>not template</div>" +
                "< my-template if-not='user' >" +
                "<div>login</div>" +
                "</ my-template >";

        Map<String, Object> items = new HashMap<>();
        items.put("user", new User("test1234", "test123", "super-shy", "super@gmail.com"));
        String expected = "<div>not template</div>";

        String result = ViewBuilder.build(template, items);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("템플릿에는 여러 템플릿 태그가 포함될 수 있다")
    void testMultipleTemplateTags() {
        String template = "<div>start-line</div>" +
                "<my-template if='login'><div>login</div></my-template>" +
                "<my-template if-not='login'><div>logout</div></my-template>" +
                "<my-template if='login' foreach='u: userList'>" +
                "<ul>" +
                "<li>{u.name}</li>" +
                "<li>{u.email}</li>" +
                "</ul>" +
                "</my-template>";

        Map<String, Object> items = new HashMap<>();
        items.put("login", true);
        items.put("userList", List.of(
                new User("1", "1", "user1", "user1@test.com"),
                new User("2", "2", "user2", "user2@test.com")
        ));

        String expected = "<div>start-line</div>" +
                "<div>login</div>" +
                "<ul>" +
                "<li>user1</li>" +
                "<li>user1@test.com</li>" +
                "</ul>" +
                "<ul>" +
                "<li>user2</li>" +
                "<li>user2@test.com</li>" +
                "</ul>";

        String result = ViewBuilder.build(template, items);
        Assertions.assertThat(result).isEqualTo(expected);
    }
}