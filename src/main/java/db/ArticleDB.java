package db;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArticleDB implements LongIdDatabase<Article> {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDB.class);
    private static final ArticleDB instance = new ArticleDB();
    private static Map<Long, Article> articles = new HashMap<>();

    private ArticleDB() {}

    // 싱글턴 인스턴스를 반환하는 메서드
    public static ArticleDB getInstance() {
        return instance;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articles.get(id));

    }

    @Override
    public List<Article> findAll() {
        return articles.values().stream().toList();
    }

    @Override
    public void save(Article article) {
        logger.info("Adding Article: " + article.getTitle());
        articles.put(article.getArticleId(), article);
    }

    @Override
    public void delete(Article article) {

    }
}
