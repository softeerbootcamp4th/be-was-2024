package model;

import util.ConstantUtil;

import java.util.Map;

/**
 * 게시글 정보를 저장하는 클래스
 */
public class Article {

    private String id;
    private String title;
    private String content;
    private String authorName;
    private String imagePath;

    protected Article(String title, String content, String authorName, String imagePath){
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.imagePath = imagePath;
    }

    protected Article(String id, String title, String content, String authorName, String imagePath){
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.imagePath = imagePath;
    }

    /**
     * 요청 Layer에서 Map을 객체로 변환하기 위해 사용
     * @param params
     * @return Article
     */
    public static Article from(Map<String, String> params){
        String title = params.get(ConstantUtil.TITLE);
        String content = params.get(ConstantUtil.CONTENT);
        String authorName = params.get(ConstantUtil.AUTHOR_NAME);
        String imagePath = params.get(ConstantUtil.IMAGE_PATH);
        return new Article(title, content, authorName, imagePath);
    }

    /**
     * DB Layer에서, 테이블을 객체로 변환하기 위해 사용
     * @param id
     * @param title
     * @param content
     * @param authorName
     * @return
     */
    public static Article of(String id, String title, String content, String authorName, String imagePath){
        return new Article(id, title, content, authorName, imagePath);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authorName='" + authorName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
