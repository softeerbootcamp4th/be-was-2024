package view;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateBlockBuilderTest {
    @Test
    @DisplayName("정상적인 foreach 템플릿 들어오면 정상적인 출력 반환")
    void testForEach() {
        String templateBody =
                "<div>{user.userId}</div>" +
                "<div>{user.password}</div>" +
                "<div>{user.name}</div>" +
                "<div>{common}</div>";

        List<User> users = List.of(
                new User("user1", "test123", "one", ""),
                new User("user2", "test123", "two", "")
        );

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);
        model.put("common", "common");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("foreach", "user: users");

        String expected = "<div>user1</div>" +
                "<div>test123</div>" +
                "<div>one</div>" +
                "<div>common</div>" +
                "<div>user2</div>" +
                "<div>test123</div>" +
                "<div>two</div>" +
                "<div>common</div>";

        String view = TemplateBlockBuilder.build(attributes, templateBody, finder);
        Assertions.assertThat(view).isEqualTo(expected);
    }


    @Test
    @DisplayName("foreach 속성 값이 key: value 형태로 표현되지 않으면 예외")
    void throwIfForEachAttrInvalid() {
        String templateBody =
                "<div>{user.userId}</div>" +
                        "<div>{user.password}</div>" +
                        "<div>{user.name}</div>" +
                        "<div>{common}</div>";

        List<User> users = List.of(
                new User("user1", "test123", "one", ""),
                new User("user2", "test123", "two", "")
        );

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);
        model.put("common", "common");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();

        Assertions.assertThatThrownBy(() -> {
            // obj : objs 가 :가 아닌 기호로 연결됨
            attributes.put("foreach", "user = users");
            TemplateBlockBuilder.build(attributes, templateBody, finder);
        });
        Assertions.assertThatThrownBy(() -> {
            // 속성값에 해당하는 객체가 없음. invalid.~ 로 지정되지 않아 user 객체를 탐색하나, user 객체가 없으므로 예외 던짐
            attributes.put("foreach", "invalid: users");
            TemplateBlockBuilder.build(attributes, templateBody, finder);
        });

        Assertions.assertThatThrownBy(() -> {
            // 아이템 객체가 지정되지 않음
            attributes.put("foreach", "users");
            TemplateBlockBuilder.build(attributes, templateBody, finder);
        });
    }


    @Test
    @DisplayName("If 조건을 만족하면 템플릿 값 채워서 반환")
    void testIfConditionIsSatisfied() {
        String templateBody = "<div> {username}님, 안녕하세요! </div>";

        Map<String, Object> model = new HashMap<>();
        model.put("userExist", true);
        model.put("username", "테스트");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("if", "userExist");

        String expected = "<div> 테스트님, 안녕하세요! </div>";

        String view = TemplateBlockBuilder.build(attributes, templateBody, finder);
        Assertions.assertThat(view).isEqualTo(expected);
    }

    @Test
    @DisplayName("If 조건을 만족하지 않으면 빈 문자열 반환")
    void testIfConditionIsNotSatisfied() {
        String templateBody = "<div> {username}님, 안녕하세요! </div>";

        Map<String, Object> model = new HashMap<>();
//        model.put("userExist", true);
        model.put("username", "테스트");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("if", "userExist");

        String view = TemplateBlockBuilder.build(attributes, templateBody, finder);
        Assertions.assertThat(view).isEmpty();
    }

    @Test
    @DisplayName("If-not 조건을 만족하면 = 대상 객체가 없다면 템플릿 채워 반환")
    void testIfNotConditionIsSatisfied() {
        String templateBody = "<div> state = {state} </div>";

        Map<String, Object> model = new HashMap<>();
//        model.put("userExist", true);
        model.put("state", "유저없음");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("if-not", "userExist");

        String expected = "<div> state = 유저없음 </div>";

        String view = TemplateBlockBuilder.build(attributes, templateBody, finder);
        Assertions.assertThat(view).isEqualTo(expected);
    }

    @Test
    @DisplayName("If-not 조건을 만족하지 않으면 = 대상 객체가 있다면 빈 문자열 반환")
    void testIfNotConditionIsNotSatisfied() {
        String templateBody = "<div> state = {state} </div>";

        Map<String, Object> model = new HashMap<>();
        model.put("userExist", "아무 값"); // 단지 객체가 지정되기만 하면 됨. 값 검사 안함.
        model.put("state", "유저없음");
        ItemFinder finder = new ItemFinder(model);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("if-not", "userExist");

        String view = TemplateBlockBuilder.build(attributes, templateBody, finder);
        Assertions.assertThat(view).isEmpty();
    }
}