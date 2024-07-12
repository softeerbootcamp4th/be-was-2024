package view;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateBlockBuilderTest {
    @Test
    void testForEach() {
        String templateString =
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

        String view = TemplateBlockBuilder.build(attributes, templateString, finder);
        Assertions.assertThat(view).isEqualTo(expected);
    }

}