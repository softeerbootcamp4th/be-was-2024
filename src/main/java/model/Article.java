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

    protected Article(String title, String content, String authorName){
        this.title = title;
        this.content = content;
        this.authorName = authorName;
    }

    protected Article(String id, String title, String content, String authorName){
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
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
        return new Article(title, content, authorName);
    }

    /**
     * DB Layer에서, 테이블을 객체로 변환하기 위해 사용
     * @param id
     * @param title
     * @param content
     * @param authorName
     * @return
     */
    public static Article of(String id, String title, String content, String authorName){
        return new Article(id, title, content, authorName);
    }

    public String getArticleId() {
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
}
