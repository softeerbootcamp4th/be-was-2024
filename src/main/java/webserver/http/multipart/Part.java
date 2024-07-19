package webserver.http.multipart;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class Part {
    private final ContentDisposition contentDisposition;
    private final String filename;
    private final Map<String, String> headerMap;
    private final byte[] body;


    public Part(ContentDisposition contentDisposition, String filename, Map<String, String> headerMap, byte[] body) {
        this.contentDisposition = contentDisposition;
        this.filename = filename;
        this.headerMap = headerMap;
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public String getFilename() {
        return filename;
    }

    public void write(String fileName, byte[] body) throws IOException {
        String filePath = "src/main/resources/upload/" + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));

        bos.write(body);

        bos.flush();
        bos.close();
    }
}
