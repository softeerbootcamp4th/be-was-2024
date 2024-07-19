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
import webserver.http.multipart.Part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class CreateArticleMapper implements webserver.mapping.mapper.HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(CreateArticleMapper.class);

    public static final String HEADER_COOKIE = "cookie";
    public static final String COOKIE_SESSION_ID = "sId";
    public static final String BODY_CONTENT = "content";

    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public synchronized MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        UUID uuid = UUID.fromString(httpRequestParser.parseCookie(httpRequest.getHeaders().get(HEADER_COOKIE)).get(COOKIE_SESSION_ID));
        String userId = SessionTable.findUserIdBySessionId(uuid);

        if (httpRequest.getHeaders().get("content-type").startsWith("multipart/form-data")) {
            String boundary = httpRequest.getHeaders().get("content-type").split("boundary=")[1];
            ArrayList<Part> parts = httpRequestParser.parseMultipartFormData(httpRequest.getBody(), boundary);

            // 파일 로컬 저장
            Part body = parts.get(1);
            body.write("test.png", body.getBody());
        } else {
            Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));
            String content = body.get(BODY_CONTENT);

            Article article = new Article(userId, content);
            Database.addArticle(article);
            logger.debug("Article created: {}", article);
        }

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"), null);

        return response;
    }
}
