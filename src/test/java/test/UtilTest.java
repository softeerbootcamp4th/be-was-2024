package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Utils;

public class UtilTest {

    @Test
    @DisplayName("staticPath가 올바르게 로딩되었는지 확인하는 테스트입니다.")
    void isStaticPathLoaded(){
        String staticPath = Utils.getStaticPath();

        Assertions.assertEquals(staticPath, "./src/main/resources/static");
    }

}
