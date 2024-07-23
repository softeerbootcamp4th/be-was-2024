package http.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseTest {
    @Test
    public void testParse() {
        // 예제로 사용할 byte 배열 (예: "Hello\r\nWorld\n" 문자열을 바이트 배열로 변환)
        byte[] byteArray = ("POST /test HTTP/1.1\n" +
                "Host: foo.example\n" +
                "Content-Type: multipart/form-data;boundary=\"boundary\"\n" +
                "\n" +
                "--boundary\n" +
                "Content-Disposition: form-data; name=\"field1\"\n" +
                "\n" +
                "value1\n" +
                "--boundary\n" +
                "Content-Disposition: form-data; name=\"field2\"; filename=\"example.txt\"\n" +
                "\n" +
                "value2\n" +
                "--boundary--").getBytes();

        try (ByteArrayInputStream in = new ByteArrayInputStream(byteArray)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int data;

            while ((data = in.read()) != -1) {
                // 개행 문자('\n')를 만나면 현재까지 구성한 라인을 처리
                if (data == '\n') {
                    System.out.println("line" + bos.toString());
                    bos.reset();
                } else if (data == '\r') {
                    // '\r'은 무시 (Windows 스타일의 '\r\n' 처리)
                    continue;
                } else {
                    // 바이트를 문자열로 변환하여 라인에 추가
                    bos.write(data);
                }
            }

            System.out.println(bos.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}

