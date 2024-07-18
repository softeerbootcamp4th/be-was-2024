package db;

import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ArticleDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ArticleDatabase.class);

    private static Map<Long, Article> articles = new HashMap<>();

    public static void addArticle(Article article) {
        logger.info("Adding Article: " + article.getTitle());
        articles.put(article.getArticleId(), article);
    }

//    public static Optional<User> findUserById(String userId) {
//        return Optional.ofNullable(users.get(userId));
//    }
//
//    public static Collection<User> findAll() {
//        return users.values();
//    }
}
