package webserver.mapping.mapper.post;

import db.Database;
import db.SessionTable;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class CreateArticleMapper implements webserver.mapping.mapper.HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(CreateArticleMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public synchronized MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));

        UUID uuid = UUID.fromString(httpRequestParser.parseCookie(httpRequest.getHeaders().get("Cookie")).get("sId"));
        String userId = SessionTable.findUserIdBySessionId(uuid);
        String content = body.get("content");

        Article article = new Article(userId, content);
        Database.addArticle(article);
        logger.debug("Article created: {}", article);

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"), null);

        return response;
    }
}
