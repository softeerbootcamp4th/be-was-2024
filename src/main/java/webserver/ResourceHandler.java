package webserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

enum TYPE {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    JPG("jpg", "image/jpeg");

    private final String type;
    private final String mime;

    TYPE(String type, String mime) {
        this.type = type;
        this.mime = mime;
    }

    public String getType() {
        return this.type;
    }

    public String getMime() {
        return this.mime;
    }
}

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

    //
    public String getContentType(String url) {
        String type = url.split("\\.")[1];
        System.out.println(type);

        for (TYPE t : TYPE.values()) {
            if (t.getType().equals(type)) {
                return t.getMime();
            }
        }

        return "text/html";
    }
}
