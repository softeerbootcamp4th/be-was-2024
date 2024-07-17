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
        if (fields.size() != 4 || fields.values().stream().anyMatch(String::isBlank)) {
            throw new ModelException(ConstantUtil.INVALID_BODY);
        }
        if(fields.get(ConstantUtil.TITLE) == null || fields.get(ConstantUtil.CONTENT) == null || fields.get(ConstantUtil.AUTHOR_NAME) == null){
            throw new ModelException(ConstantUtil.INVALID_BODY);
        }

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
}
