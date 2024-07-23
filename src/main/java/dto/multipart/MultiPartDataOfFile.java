package dto.multipart;

import constant.FileExtensionType;

/**
 * multipart/form-data 형식의 데이터 중, 파일 형식의 데이터를 저장하는 클래스
 */
public class MultiPartDataOfFile implements MultiPartData{
    private final String fileName;
    private final FileExtensionType contentType;
    private final byte[] content;

    public MultiPartDataOfFile(String fileName, FileExtensionType contentType, byte[] content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public FileExtensionType getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

}
