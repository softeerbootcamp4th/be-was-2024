package model;

/**
 * 게시글을 저장하는 클래스입니다.
 */
public class Article {
    private final int articleId;
    private final String userName;
    private final String text;
    private final byte[] pic;

    /**
     * Article 인스턴스를 생성하는 생성자입니다.
     * @param articleId 게시글 id를 표현하는 필드입니다.
     * @param userName 작성자의 이름을 표현하는 필드입니다.
     * @param text 게시글 본문을 표현하는 필드입니다.
     * @param pic 게시글 이미지를 표현하는 필등비니다.
     */
    public Article(int articleId, String userName, String text, byte[] pic) {
        this.articleId = articleId;
        this.userName = userName;
        this.text = text;
        this.pic = pic;
    }

    /**
     * 게시글의 id를 반환하는 메서드입니다.
     * @return 정수값으로 이루어진 articleId를 반환합니다.
     */
    public int getArticleId() {
        return articleId;
    }

    /**
     * 작성자의 이름을 반환하는 메서드입니다.
     * @return 문자열로 이루어진 userName을 반환합니다.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 게시글의 본문을 반환하는 메서드입니다.
     * @return 문자열로 이루어진 text를 반환합니다.
     */
    public String getText() {
        return text;
    }

    /**
     * 게시글의 이미지를 반환하는 메서드입니다.
     * @return 바이트 배열로 이루어진 pic을 반환합니다.
     */
    public byte[] getPic() {
        return pic;
    }
}
