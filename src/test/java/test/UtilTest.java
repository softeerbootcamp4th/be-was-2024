package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Utils.ResponseWithStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Constants.FILE_INDEX;
import static util.Constants.STATIC_PATH;
import static util.Utils.*;

public class UtilTest {

    @Test
    @DisplayName("컨텐츠 타입 반환 테스트")
    void 컨텐츠_타입_반환_테스트() {
        String html = getContentType("html");
        String css = getContentType("css");
        String js = getContentType("js");

        assertEquals(html, "text/html");
        assertEquals(css, "text/css");
        assertEquals(js, "text/javascript");
    }

    @Test
    @DisplayName("정적 파일 반환 테스트")
    void 정적_파일_반환_테스트() throws IOException {
        String path = STATIC_PATH + FILE_INDEX;
        ResponseWithStatus fileContent = getFileContent(path);

        assertEquals(fileContent.status.getStatus(), 200);
        assertEquals(fileContent.status.getMessage(), "Ok");
        assertTrue(fileContent.body.length > 0);
    }

    @Test
    @DisplayName("정적 파일 반환 실패 테스트")
    void 정적_파일_반환_실패_테스트() throws IOException {
        String path = STATIC_PATH + "NOT_EXIST";
        ResponseWithStatus fileContent = getFileContent(path);

        assertEquals(fileContent.status.getStatus(), 404);
        assertEquals(fileContent.status.getMessage(), "Not found");
    }

    @Test
    @DisplayName("바운더리 값 반환 테스트")
    void getBoundaryTest(){
        String Content_Type = "multipart/form-data; boundary=---011000010111000001101001";

        String boundary = getBoundary(Content_Type);
        assertEquals(boundary, "-----011000010111000001101001");
    }
}
