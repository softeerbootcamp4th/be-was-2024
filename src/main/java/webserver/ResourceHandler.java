package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceHandler {
    public byte[] getByteArray(String url) throws IOException {
        File html = new File("src/main/resources/static" + url);
        byte[] body = new byte[(int) html.length()];

        try (FileInputStream fileInputStream = new FileInputStream(html)) {
            fileInputStream.read(body);
        }

        return body;
    }
}
