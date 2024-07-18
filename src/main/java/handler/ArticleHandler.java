package handler;

import db.Database;
import exception.ModelException;
import model.Article;
import util.ConstantUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Article 객체를 관리하는 Handler
 */
public class ArticleHandler implements ModelHandler<Article> {

    private ArticleHandler() {}

    public static ArticleHandler getInstance() {
        return ArticleHandler.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final ArticleHandler INSTANCE = new ArticleHandler();
    }

    /**
     * 매개변수 검증 후 Article 객체 생성
     * @param fields
     * @return
     */
    @Override
    public Optional<Article> create(Map<String, String> fields) {
        if (fields.size() < 3 || fields.size() > 4){
            throw new ModelException(ConstantUtil.INVALID_BODY);
        }

        validateValue(fields.get(ConstantUtil.TITLE));
        validateValue(fields.get(ConstantUtil.CONTENT));
        validateValue(fields.get(ConstantUtil.AUTHOR_NAME));

        Article article = Article.from(fields);
        return Optional.ofNullable(Database.addArticle(article));
    }

    /**
     * Article 객체 조회
     * @param id
     * @return
     */
    @Override
    public Optional<Article> findById(String id) {
        return Database.findArticleById(Integer.parseInt(id));
    }

    /**
     * 모든 Article 객체 조회
     * @return
     */
    @Override
    public List<Article> findAll() {
        return Database.findAllArticles().stream().toList();
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new ModelException(ConstantUtil.INVALID_BODY);
        }
    }
}
