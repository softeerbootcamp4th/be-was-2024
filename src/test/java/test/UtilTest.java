package test;

import http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Utils;

import java.io.IOException;

import static util.Utils.*;
import static util.Utils.getFileContent;

public class UtilTest {

    @Test
    @DisplayName("staticPath가 올바르게 로딩되었는지 확인하는 테스트입니다.")
    void isStaticPathLoaded() {
        String staticPath = getStaticPath();

        Assertions.assertEquals(staticPath, "./src/main/resources/static");
    }

    @Test
    @DisplayName("올바르지 않은 경로가 들어왔을 때 404에러가 반환되는지 확인하는 테스트입니다.")
    void is404returned() throws IOException {
        String path = "/nothing";
        ResponseWithStatus fileContent = getFileContent(path);

        Assertions.assertEquals(fileContent.status, HttpStatus.NOT_FOUND);
    }
}
