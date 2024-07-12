package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Constants.STATIC_PATH;

public class ConstantsTest {

    @Test
    @DisplayName("STATIC_PATH 로드 테스트")
    void STATIC_PATH_로드_테스트(){
        assertEquals(STATIC_PATH, "./src/main/resources/static");
    }
}
