package dto;

import constant.FileExtensionType;
import model.MyTagDomain;
import model.Post;

/**
 * 동적 바인딩 기능에 사용되는 게시글 데이터를 저장하는 클래스
 * Post 클래스의 필드에 작성자의 userId를 포함한다.
 */
public class PostDto implements MyTagDomain {
    private final int id;
    private final String title;
    private final String content;
    private final byte[] image;
    private final FileExtensionType fileType;
    private final String userId;

    public PostDto(int id, String title, String content, byte[] image, FileExtensionType fileType, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.fileType = fileType;
        this.userId = userId;
    }

    public PostDto(Post post, String userId){
        this(post.getId(), post.getTitle(), post.getContent(), post.getImage(), post.getFileType(), userId);
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
}
