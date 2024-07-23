package webserver.http.multipart;

import java.util.Map;

public class StandardMultipartFile {
    private final ContentDisposition contentDisposition;
    private final String filename;

    private final Map<String, String> headerMap;
    private final byte[] body;


    public StandardMultipartFile(ContentDisposition contentDisposition, String filename, Map<String, String> headerMap, byte[] body) {
        this.contentDisposition = contentDisposition;
        this.filename = filename;
        this.headerMap = headerMap;
        this.body = body;
    }
}
