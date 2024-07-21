package dto.multipart;

import constant.FileExtensionType;

/**
 * multipart/form-data 형식의 데이터를 저장하는 클래스들의 인터페이스
 */
public interface MultiPartData {

    FileExtensionType getContentType();

}
