package webserver.http.multipart;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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

    public String write(byte[] body) throws IOException {
        String fileName = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(body);
        String filePath = "src/main/resources/upload/" + fileName + fileExtension;

        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));

        bos.write(body);

        bos.flush();
        bos.close();

        return filePath;
    }

    private String getFileExtension(byte[] body) {
        if (body.length < 4) {
            return ""; // 파일이 너무 작아 시그니처를 확인할 수 없음
        }

        // 파일 시그니처 확인
        if (body[0] == (byte) 0xFF && body[1] == (byte) 0xD8 && body[2] == (byte) 0xFF) {
            return ".jpg";
        } else if (body[0] == (byte) 0x89 && body[1] == (byte) 0x50 && body[2] == (byte) 0x4E && body[3] == (byte) 0x47) {
            return ".png";
        } else if (body[0] == (byte) 0x47 && body[1] == (byte) 0x49 && body[2] == (byte) 0x46 && body[3] == (byte) 0x38) {
            return ".gif";
        } else if (body[0] == (byte) 0x25 && body[1] == (byte) 0x50 && body[2] == (byte) 0x44 && body[3] == (byte) 0x46) {
            return ".pdf";
        } else if (body[0] == (byte) 0x50 && body[1] == (byte) 0x4B && body[2] == (byte) 0x03 && body[3] == (byte) 0x04) {
            return ".zip";
        }

        return ""; // 알 수 없는 파일 형식
    }
}
