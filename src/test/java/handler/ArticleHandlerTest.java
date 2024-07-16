package handler;

import db.Database;
import exception.ModelException;
import model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.ConstantUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleHandlerTest {

    ArticleHandler articleHandler;

    @BeforeEach
    void setUpEach() {
        articleHandler = ArticleHandler.getInstance();
        Database.clearUsers();
        Database.clearArticles();
    }

    private Map<String, String> createFields(String title, String content, String authorName) {
        return new HashMap<>
                (Map.of(ConstantUtil.TITLE, title, ConstantUtil.CONTENT, content, ConstantUtil.AUTHOR_NAME, authorName));
    }

    @DisplayName("create: requestBody로부터 Article 객체를 생성하고 필드값을 확인한다.")
    @ParameterizedTest(name = "title: {0}, contents: {1}, authorName: {2}")
    @CsvSource({
            "소프티어, 부트캠프, 현대자동차",
            "자바, 를 자바, 자바지기",
            "웹서버는, 너무 어려워, 채승운"
    })
    void create(String title, String content, String authorName) {
        // given
        Map<String, String> fields = createFields(title, content, authorName);

        // when
        Article article = articleHandler.create(fields).get();

        // then
        assertThat(article.getId()).isNotNull();
        assertThat(article)
                .extracting(Article::getTitle, Article::getContent, Article::getAuthorName)
                .containsExactly(title, content, authorName);
    }

    @DisplayName("create: 잘못된 필드가 입력되었다면 예외가 발생해야 한다.")
    @Test
    void createWithBlankFields() {
        // given
        Map<String, String> fields = createFields("", " ", "authorName");

        // when & then
        assertThatThrownBy(() -> articleHandler.create(fields))
                .isInstanceOf(ModelException.class)
                .hasMessageContaining(ConstantUtil.INVALID_BODY);
    }

    @DisplayName("create: 잘못된 필드가 입력되었다면 예외가 발생해야 한다.")
    @Test
    void createWithWrongField() {
        // given
        Map<String, String> fields = Map.of("wrong", "wrong", "wrong", "wrong", "wrong", "wrong");

        // when & then
        assertThatThrownBy(() -> articleHandler.create(fields))
                .isInstanceOf(ModelException.class)
                .hasMessageContaining(ConstantUtil.INVALID_BODY);
    }

    @DisplayName("findById: Article ID로 Article을 조회한다.")
    @ParameterizedTest(name = "title: {0}, contents: {1}, authorName: {2}")
    @CsvSource({
            "6조는, 오렌지, 채승운",
            "그대 이제, 헤어~~지자 말해요~~, 박재정",
            "검정색, 하트, 릴러말즈"
    })
    void findById(String title, String content, String authorName) {
        // given
        Map<String, String> fields = createFields(title, content, authorName);
        String id = Database.addArticle(Article.from(fields)).getId();

        // when
        Article foundArticle = articleHandler.findById(id).get();

        // then
        assertThat(foundArticle)
                .extracting(Article::getId, Article::getTitle, Article::getContent, Article::getAuthorName)
                .containsExactly(id, title, content, authorName);
    }

    @DisplayName("findAll: 모든 Article을 반환하고 값을 확인한다.")
    @Test
    void findAll() {
        // given
        Database.addArticle(Article.from(createFields("title1", "content1", "authorName1")));
        Database.addArticle(Article.from(createFields("title2", "content2", "authorName2")));

        // when
        List<Article> articles = articleHandler.findAll();

        // then
        assertThat(articles).hasSize(2);
        assertThat(articles.get(0))
                .extracting(Article::getTitle, Article::getContent, Article::getAuthorName)
                .containsExactly("title1", "content1", "authorName1");
        assertThat(articles.get(1))
                .extracting(Article::getTitle, Article::getContent, Article::getAuthorName)
                .containsExactly("title2", "content2", "authorName2");
    }

    @DisplayName("findAll: Article이 없으면 빈 Collection을 반환한다.")
    @Test
    void findAllWithEmptyArticles() {
        // when
        List<Article> articles = articleHandler.findAll();

        // then
        assertThat(articles).isEmpty();
    }
}
