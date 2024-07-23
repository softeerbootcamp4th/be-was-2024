package model;

import constant.FileExtensionType;
import exception.InvalidHttpRequestException;
import dto.multipart.MultiPartData;
import dto.multipart.MultiPartDataOfFile;
import dto.multipart.MultiPartDataOfText;
import java.util.Map;

/**
 * 게시글 정보를 저장하는 클래스
 */
public class Post{
    private int id;
    private final String title;
    private final String content;
    private final byte[] image;
    private final FileExtensionType fileType;
    private final int userId;

    public Post(String title, String content, byte[] image, FileExtensionType fileType, int userId) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.fileType = fileType;
        this.userId = userId;
    }

    public Post(int id, String title, String content, byte[] image, FileExtensionType fileType, int userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.fileType = fileType;
        this.userId = userId;
    }

    public Post(Map<String, MultiPartData> multiPartMap, int userId) {
        this(
                getStringValueFromMap(multiPartMap, "title"),
                getStringValueFromMap(multiPartMap, "content"),
                getByteValueFromMap(multiPartMap, "image"),
                getFileExtentionTypeValueFromMap(multiPartMap, "image"),
                userId
        );
    }

    private static String getStringValueFromMap(Map<String, MultiPartData> multiPartMap, String key){
        MultiPartDataOfText multiPartDataOfText = (MultiPartDataOfText) multiPartMap.get(key);
        if (multiPartDataOfText == null) {
            throw new InvalidHttpRequestException("Post information cannot be null");
        }

        return multiPartDataOfText.getText();

    }

    private static byte[] getByteValueFromMap(Map<String, MultiPartData> multiPartMap, String key){
        MultiPartDataOfFile multiPartDataOfFile = (MultiPartDataOfFile) multiPartMap.get(key);
        if (multiPartDataOfFile == null) {
            return new byte[0];
        }

        return multiPartDataOfFile.getContent();
    }

    private static FileExtensionType getFileExtentionTypeValueFromMap(Map<String, MultiPartData> multiPartMap, String key){
        MultiPartDataOfFile multiPartDataOfFile = (MultiPartDataOfFile) multiPartMap.get(key);
        if (multiPartDataOfFile == null) {
            return FileExtensionType.NULL;
        }

        return multiPartDataOfFile.getContentType();
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public byte[] getImage() {
        return image;
    }

    public FileExtensionType getFileType() {
        return fileType;
    }

    public int getUserId() {
        return userId;
    }
}
