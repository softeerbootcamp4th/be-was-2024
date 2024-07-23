package utils;

import enums.MimeType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceUtil {
    public byte[] getByteArray(String url) throws IOException {
        String path = "src/main/resources/static" + url;
        // 파일
        File file = new File(path);
        byte[] body = new byte[(int) file.length()];

        // 얘는 한글자씩 가져오는 방법임
        // 버퍼를 사용하면 빠르게 가져올 수 있음
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);) {
            bufferedInputStream.read(body);
        }

        return body;
    }

    // content-type을 반환하기 위한 메소드
    public String getContentType(String url) {
        String type = url.substring(url.lastIndexOf(".") + 1);

        // TYPE을 순회하면서 type에 해당하는 mime type 찾기
        for (MimeType t : MimeType.values()) {
            if (t.getType().equals(type)) {
                return t.getMime();
            }
        }

        return "text/plain";
    }

    public boolean isStaticResource(String url) {
        if (url.contains(".")) {
            return !url.endsWith("html");
        } else {
            return false;
        }
    }
}
