package dto.multipart;

import constant.FileExtensionType;

/**
 * multipart/form-data 형식의 데이터 중, 텍스트 형식의 데이터를 저장하는 클래스
 */
public class MultiPartDataOfText implements MultiPartData{
    private final FileExtensionType contentType;
    private final String text;

    public MultiPartDataOfText(FileExtensionType contentType, String text) {
        this.contentType = contentType;
        this.text = text;
    }

    public FileExtensionType getContentType() {
        return contentType;
    }

    public String getText() {
        return text;
    }
}
