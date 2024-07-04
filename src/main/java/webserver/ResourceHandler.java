package webserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceHandler {
    public byte[] getByteArray(String url) throws IOException {
        // 파일
        File file = new File("src/main/resources/static" + url);
        byte[] body = new byte[(int) file.length()];

        // 얘는 한글자씩 가져오는 방법임
        // 버퍼를 사용하면 빠르게 가져올 수 있음
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);) {
            bufferedInputStream.read(body);
        }

        return body;
    }
}
