package webserver;

import enums.FileType;
import enums.Status;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StaticResourceHandler {
    private final String staticResourcePath;

    public StaticResourceHandler(String staticResourcePath) {
        this.staticResourcePath = staticResourcePath;
    }

    public void handle(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException {
        // 정적 리소스 반환
        String path = httpRequestParser.getPath();
        String extension = httpRequestParser.getExtension();
        String filePath = staticResourcePath + path;
        byte[] body = readFileToByteArray(filePath);
        httpResponseHandler
                .setStatus(Status.OK)
                .addHeader("Content-Type", FileType.getContentTypeByExtension(extension))
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .respond();
    }

    private byte[] readFileToByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
}
