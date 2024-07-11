package view;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemFinderTest {
    @Test
    @DisplayName("존재하는 객체의 값을 요청하면 값을 반환, 존재하는 필드면 null 값 그대로 반환")
    void returnItemIfExist() {
        String v1 = "test1";

        int v2_field1 = 100;
        String v2_field2 = "test2";

        TestItem v2 = new TestItem(v2_field1, v2_field2);

        var testClass = new TestClass(v1, v2);

        Object item1 = ItemFinder.getItem(testClass, "v1");
        Object item2 = ItemFinder.getItem(testClass, "v2");
        Object item3 = ItemFinder.getItem(testClass, "v2.field1");
        Object item4 = ItemFinder.getItem(testClass, "v2.field2");
        Object item5 = ItemFinder.getItem(testClass, "v3");

        Assertions.assertThat(item1).isEqualTo(v1);
        Assertions.assertThat(item2).isSameAs(v2);
        Assertions.assertThat(item3).isEqualTo(v2_field1);
        Assertions.assertThat(item4).isEqualTo(v2_field2);
        Assertions.assertThat(item5).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 값을 요청하면 예외")
    void throwIfItemNotExist() {

        int field1 = 100;
        String field2 = null;

        TestItem item = new TestItem(field1, field2);

        Object item1 = ItemFinder.getItem(item, "field1");
        Object item2 = ItemFinder.getItem(item, "field2");

        Assertions.assertThat(item1).isEqualTo(field1);
        Assertions.assertThat(item2).isNull();
        Assertions.assertThatThrownBy(() -> ItemFinder.getItem(item, "v2.field1"));
        Assertions.assertThatThrownBy(() -> ItemFinder.getItem(item, "v3"));
    }

    private class TestClass {
        public String v1;
        private TestItem v2;
        private Integer v3;

        public TestClass(String v1, TestItem v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    private class TestItem {
        private Integer field1;
        private String field2;
        public TestItem(Integer field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}