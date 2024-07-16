package handler;

import db.Database;
import exception.ModelException;
import model.Article;
import util.ConstantUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArticleHandler implements ModelHandler<Article> {

    private ArticleHandler() {}

    public static ArticleHandler getInstance() {
        return ArticleHandler.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final ArticleHandler INSTANCE = new ArticleHandler();
    }

    @Override
    public Optional<Article> create(Map<String, String> fields) {
        if (fields.size() != 3 || fields.values().stream().anyMatch(String::isBlank)) {
            throw new ModelException(ConstantUtil.INVALID_BODY);
        }

        Article article = Article.from(fields);
        Database.addArticle(article);
        return Optional.of(article);
    }

    @Override
    public Optional<Article> findById(String id) {
        return Database.findArticleById(Integer.parseInt(id));
    }

    @Override
    public List<Article> findAll() {
        return Database.findAllArticles().stream().toList();
    }
}
