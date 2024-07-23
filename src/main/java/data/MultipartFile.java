package data;

import java.util.HashMap;
import java.util.Map;

/**
 * 멀티파트 정보
 */
public class MultipartFile {
    private final Map<String, String> contentDisposition;
    private String contentType;
    private final byte[] data;

    /**
     * 멀티파트 파일 생성
     * @param headers
     * @param data
     */
    public MultipartFile(Map<String, String> headers, byte[] data) {
        contentDisposition = new HashMap<>();
        parseHeader(headers);
        contentType = headers.get("Content-Type");
        this.data = data;
    }

    private void parseHeader(Map<String, String> headers){
        String contentDispositionHeaders = headers.get("Content-Disposition");
        String[] contentDisposSplit = contentDispositionHeaders.split(";");
        for (String splitted : contentDisposSplit){
            String[] keyValue = splitted.split("=");
            if (keyValue.length == 1){continue;}
            String strippedValue = keyValue[1].strip();
            contentDisposition.put(keyValue[0].strip(), strippedValue.substring(1,strippedValue.length()-1));
        }
    }

    public Map<String, String> getContentDisposition() {
        return contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }
}
